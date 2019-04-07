package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;



/**
 * Reads input file, and constructs t-map and search structure
 * @author aguestuser
 *
 */
public class StructureConstructor{
	private Scanner in; 
	private String fileName; 
	private ArrayList<Segment> segments; 
	/**
	 * 0 = original, 1 = shuffle
	 */
	private int order; 
	
	private Node root; 
	
	int minX = Integer.MAX_VALUE; 
	int maxX = Integer.MIN_VALUE; 
	int minY = Integer.MAX_VALUE; 
	int maxY = Integer.MIN_VALUE; 
	
	public StructureConstructor(String fileName, int order) throws FileNotFoundException {
		this.fileName = fileName; 
		File file = new File(fileName); 
		in = new Scanner(file); 
		this.order = order; 
		construct(); 
	}
	
	private void construct() {
		
		segments = new ArrayList<>(); 
		
		  while(in.hasNext()) {
			  String next = in.next(); 
			  String trim = next.substring(1, next.length()-1); 
			  String[] parse = trim.split(","); 
			  Point pointa = new Point(Integer.parseInt(parse[0]), Integer.parseInt(parse[1])); 
			  next = in.next(); 
			  trim = next.substring(1, next.length()-1); 
			  parse = trim.split(","); 
			  Point pointb = new Point(Integer.parseInt(parse[0]), Integer.parseInt(parse[1])); 
			  
			  minX = Math.min(minX, Math.min(pointa.getX(), pointb.getX())); 
			  maxX = Math.max(maxX,  Math.max(pointa.getX(), pointb.getX())); 
			  
			  minY = Math.min(minY, Math.min(pointa.getY(), pointb.getY())); 
			  maxY = Math.max(maxY,  Math.max(pointa.getY(), pointb.getY())); 
			  
			  Segment seg = new Segment(pointa, pointb); 
			  segments.add(seg); 
		  }
		  
		  if(order == 1)
			  Collections.shuffle(segments);
		  
		  makeStruct();  
	}	
	
	public Trapezoid query(Point point) {
		
		return findPoint(point, null).getData(); 
	}
	
	public Set<Trapezoid> getTrapezoids() {
		Set<Trapezoid> leaves = new HashSet<Trapezoid>(); 
		helper(root, leaves); 
		
		return leaves; 
	}
	
	private void helper(Node current, Set<Trapezoid> leaves) {
		if(current.getLeftChildNode() == null && current.getRightChildNode() == null) {
			leaves.add(((Leaf)current).getData()); 
		}
		if(current.getLeftChildNode() != null)
			helper(current.getLeftChildNode(), leaves); 
		
		if(current.getRightChildNode() != null) 
			helper(current.getRightChildNode(), leaves); 
	}
	
	
	private void makeStruct() {
		 //create a trapezoid using the bounding box
        Point left = new Point(minX, minY);
        Point right = new Point(maxX, maxY);
        Trapezoid t = new Trapezoid(left, right, new Segment(new Point(minX, maxY), new Point(maxX, maxY)),
                new Segment(new Point(minX, minY), new Point(maxX, minY)));
        Leaf f = new Leaf(t);
        t.setLeaf(f);
        root = f;
        
        // 3. incrementally make the trapezoidal map
        //System.out.println("Ready to construct trapezoidal map");
        for (int i = 0; i < segments.size() && segments.get(i) != null; i++) {
            //find the trapezoids intersected by arr[i]
            //System.out.println("in loop");
            Leaf[] list = followSegment(segments.get(i));
            
            if(list.length == 1) {
            	handleContainedInSingleTrapezoid(list, i); 
            }else {
            	cutsThroughTrapezoid(list, i); 
            }
            
        }
        
	}
	
	private void handleContainedInSingleTrapezoid(Leaf[] list, int i) {
		 //System.out.println("Case I");
        //split into 4 sections
        Trapezoid old = list[0].getData();
        Trapezoid lefty = new Trapezoid(old.getLeftPoint(), segments.get(i).getLeftEndPoint(), old.getTopSegment(), old.getBottomSegment());
        Trapezoid righty = new Trapezoid(segments.get(i).getRightEndPoint(), old.getRightPoint(), old.getTopSegment(), old.getBottomSegment());
        Trapezoid top = new Trapezoid(segments.get(i).getLeftEndPoint(), segments.get(i).getRightEndPoint(), old.getTopSegment(), segments.get(i));
        Trapezoid bottom = new Trapezoid(segments.get(i).getLeftEndPoint(), segments.get(i).getRightEndPoint(), segments.get(i), old.getBottomSegment());
        XNode ll = new XNode(segments.get(i).getLeftEndPoint());
        XNode rr = new XNode(segments.get(i).getRightEndPoint());
        YNode ss = new YNode(segments.get(i));

        Leaf leftyN = new Leaf(lefty);
        lefty.setLeaf(leftyN);
        Leaf rightyN = new Leaf(righty);
        righty.setLeaf(rightyN);
        Leaf topN = new Leaf(top);
        top.setLeaf(topN);
        Leaf bottomN = new Leaf(bottom);
        bottom.setLeaf(bottomN);
        if (!(lefty.hasZeroWidth() || righty.hasZeroWidth())) {

            //link all the nodes for the trapezoids
            ll.setLeftChildNode(leftyN);
            ll.setRightChildNode(rr);
            rr.setRightChildNode(rightyN);
            rr.setLeftChildNode(ss);
            ss.setLeftChildNode(topN);
            ss.setRightChildNode(bottomN);

            //connect the nodes to the old structure
            if (list[0].getParentNode() == null) {
                root = ll;
            } else {
                //the previous node might have more than one parent node
                ArrayList<Node> parents = list[0].getParentNodes();
                for (int j = 0; j < parents.size(); j++) {
                    Node tempParent = parents.get(j);
                    if (tempParent.getLeftChildNode() == list[0]) {
                        tempParent.setLeftChildNode(ll);
                    } else {
                        tempParent.setRightChildNode(ll);
                    }
                }
            }

            //link the trapezoids together
            lowerLink(lefty, bottom);
            lowerLink(old.getLowerLeftNeighbor(), lefty);
            upperLink(lefty, top);
            upperLink(old.getUpperLeftNeighbor(), lefty);

            lowerLink(righty, old.getLowerRightNeighbor());
            lowerLink(bottom, righty);
            upperLink(righty, old.getUpperRightNeighbor());
            upperLink(top, righty);
        } else if (lefty.hasZeroWidth() && !righty.hasZeroWidth()) {//only left has zero width
            //link all the nodes for the trapezoids
            rr.setLeftChildNode(ss);
            rr.setRightChildNode(rightyN);
            ss.setLeftChildNode(topN);
            ss.setRightChildNode(bottomN);

            //connect the nodes to the old structure
            if (list[0].getParentNode() == null) {
                root = rr;
            } else {
                //the previous node might have more than one parent node
                ArrayList<Node> parents = list[0].getParentNodes();
                for (int j = 0; j < parents.size(); j++) {
                    Node tempParent = parents.get(j);
                    if (tempParent.getLeftChildNode() == list[0]) {
                        tempParent.setLeftChildNode(rr);
                    } else {
                        tempParent.setRightChildNode(rr);
                    }
                }
            }

            //link the trapezoids together
            lowerLink(old.getLowerLeftNeighbor(), bottom);
            upperLink(old.getUpperLeftNeighbor(), top);

            lowerLink(righty, old.getLowerRightNeighbor());
            lowerLink(bottom, righty);
            upperLink(righty, old.getUpperRightNeighbor());
            upperLink(top, righty);
        } else if (righty.hasZeroWidth() && !lefty.hasZeroWidth()) {//only right has zero width
            //link all the nodes for the trapezoids
            ll.setLeftChildNode(leftyN);
            ll.setRightChildNode(ss);
            ss.setLeftChildNode(topN);
            ss.setRightChildNode(bottomN);

            //connect the nodes to the old structure
            if (list[0].getParentNode() == null) {
                root = ll;
            } else {
                //the previous node might have more than one parent node
                ArrayList<Node> parents = list[0].getParentNodes();
                for (int j = 0; j < parents.size(); j++) {
                    Node tempParent = parents.get(j);
                    if (tempParent.getLeftChildNode() == list[0]) {
                        tempParent.setLeftChildNode(ll);
                    } else {
                        tempParent.setRightChildNode(ll);
                    }
                }
            }

            //link the trapezoids together
            lowerLink(lefty, bottom);
            lowerLink(old.getLowerLeftNeighbor(), lefty);
            upperLink(lefty, top);
            upperLink(old.getUpperLeftNeighbor(), lefty);

            lowerLink(bottom, old.getLowerRightNeighbor());
            upperLink(top, old.getUpperRightNeighbor());
        } else {
            //both have zero width

            //build the search structure
            ss.setLeftChildNode(topN);
            ss.setRightChildNode(bottomN);

            //connect the nodes to the old structure
            if (list[0].getParentNode() == null) {
                root = ss;
            } else {
                //the previous node might have more than one parent node
                ArrayList<Node> parents = list[0].getParentNodes();
                for (int j = 0; j < parents.size(); j++) {
                    Node tempParent = parents.get(j);
                    if (tempParent.getLeftChildNode() == list[0]) {
                        tempParent.setLeftChildNode(ss);
                    } else {
                        tempParent.setRightChildNode(ss);
                    }
                }
            }

            //link the trapezoids together (this is nontrivial in degenerates cases)
            lowerLink(old.getLowerLeftNeighbor(), bottom);
            lowerLink(bottom, old.getLowerRightNeighbor());
            upperLink(old.getUpperLeftNeighbor(), top);
            upperLink(top, old.getUpperRightNeighbor());
        }
	}
	
	
	private void cutsThroughTrapezoid(Leaf[] list, int i) {
	       //System.out.println("Case II");
        //the first and last cases get broken into 3 parts
        //the middle ones are different

        //if the left segment endpoint is not leftp of list[0].getData(), then
        //there is an extra trapezoid at the left end.  Likewise for rightp of list[n-1].getData()

        //for everything in the middle, we start with a single top and bottom trap for both
        //then we merge trapezoids together as needed
        //note that before merging, some trapezoids may have an endpoint which is null
        Trapezoid[] topArr = new Trapezoid[list.length];
        Trapezoid[] botArr = new Trapezoid[list.length];
        for (int j = 0; j < list.length; j++) {
            //top is defined by the original upper segment, the new segment & two endpoints
            //left endpoint:
                /*
             * if j==0, is segment's left endpoint
             * else is old trap's left endpoint if it is above the segment
             */
            //right endpoint is similar
            if (j == 0) {
                Point rtP = null;
                if (isPointAboveLine(list[j].getData().getRightPoint(), segments.get(i))) {
                    rtP = list[j].getData().getRightPoint();
                }
                topArr[j] = new Trapezoid(segments.get(i).getLeftEndPoint(), rtP, list[j].getData().getTopSegment(), segments.get(i));
            } else if (j == list.length - 1) {
                Point ltP = null;
                if (isPointAboveLine(list[j].getData().getLeftPoint(), segments.get(i))) {
                    ltP = list[j].getData().getLeftPoint();
                }
                topArr[j] = new Trapezoid(ltP, segments.get(i).getRightEndPoint(), list[j].getData().getTopSegment(), segments.get(i));
            } else {
                Point rtP = null;
                if (isPointAboveLine(list[j].getData().getRightPoint(), segments.get(i))) {
                    rtP = list[j].getData().getRightPoint();
                }
                Point ltP = null;
                if (isPointAboveLine(list[j].getData().getLeftPoint(), segments.get(i))) {
                    ltP = list[j].getData().getLeftPoint();
                }
                topArr[j] = new Trapezoid(ltP, rtP, list[j].getData().getTopSegment(), segments.get(i));
            }

            //the bottom array is constructed using a similar strategy
            if (j == 0) {
                Point rtP = null;
                if (!isPointAboveLine(list[j].getData().getRightPoint(), segments.get(i))) {
                    rtP = list[j].getData().getRightPoint();
                }
                botArr[j] = new Trapezoid(segments.get(i).getLeftEndPoint(), rtP, segments.get(i), list[j].getData().getBottomSegment());
            } else if (j == list.length - 1) {
                Point ltP = null;
                if (!isPointAboveLine(list[j].getData().getLeftPoint(), segments.get(i))) {
                    ltP = list[j].getData().getLeftPoint();
                }
                botArr[j] = new Trapezoid(ltP, segments.get(i).getRightEndPoint(), segments.get(i), list[j].getData().getBottomSegment());
            } else {
                Point rtP = null;
                if (!isPointAboveLine(list[j].getData().getRightPoint(), segments.get(i))) {
                    rtP = list[j].getData().getRightPoint();
                }
                Point ltP = null;
                if (!isPointAboveLine(list[j].getData().getLeftPoint(), segments.get(i))) {
                    ltP = list[j].getData().getLeftPoint();
                }
                botArr[j] = new Trapezoid(ltP, rtP, segments.get(i), list[j].getData().getBottomSegment());
            }
        }

        //then merge degenerate trapezoids together (those with a null bounding point)
        int aTop = 0;
        int bTop;
        int aBot = 0;
        int bBot;
        boolean topHasRightP = false;
        boolean botHasRightP = false;
        for (int j = 0; j < list.length; j++) {
            if (topArr[j].getRightPoint() != null) {
                bTop = j;
                //merge trapezoids aTop through bTop
                //we only want one trapezoid, so we just have bTop-aTop+1 pointers to it for now
                Trapezoid tempMerge = new Trapezoid(topArr[aTop].getLeftPoint(), topArr[bTop].getRightPoint(), topArr[aTop].getTopSegment(), segments.get(i));
                for (int k = aTop; k <= bTop; k++) {
                    //now there are duplicates of the same trapezoid unfortunately, but I think if we link them together left to right
                    //this shouldn't cause problems later...it just means a bit more storage use
                    topArr[k] = tempMerge;
                }
                aTop = j + 1;
            }

            if (botArr[j].getRightPoint() != null) {
                bBot = j;
                //merge trapezoids aBot through bBot
                Trapezoid tempMerge = new Trapezoid(botArr[aBot].getLeftPoint(), botArr[bBot].getRightPoint(), segments.get(i), botArr[aBot].getBottomSegment());
                for (int k = aBot; k <= bBot; k++) {
                    botArr[k] = tempMerge;
                }
                aBot = j + 1;
            }
        }

        //do trapezoid links...this should unlink the original trapezoids from the physical structure except at the ends
        //do all left links before doing right links in order to avoid linking errors
        for (int j = 0; j < list.length; j++) {
            if (j != 0) {
                //update left links
                //link right to left
                //only recycle old links if they are not in the list to be removed

                //only when the trapezoids do not repeat
                if (topArr[j] != topArr[j - 1]) {
                    lowerLink(topArr[j - 1], topArr[j]);
                }

                //leave the upper left neighbor null unless we have something to set it to
                Trapezoid temp2 = list[j].getData().getUpperLeftNeighbor();
                if (!list[j - 1].getData().equals(temp2)) {
                    upperLink(temp2, topArr[j]);
                }

                //only do this for non-repeating trapezoids
                if (botArr[j] != botArr[j - 1]) {
                    upperLink(botArr[j - 1], botArr[j]);
                }

                temp2 = list[j].getData().getLowerLeftNeighbor();
                if (!list[j - 1].getData().equals(temp2)) {
                    lowerLink(temp2, botArr[j]);
                }

            }

        }
        for (int j = 0; j < list.length; j++) {
            if (j != topArr.length - 1) {
                //update right links

                //only for non-repeats
                if (topArr[j] != topArr[j + 1]) {
                    lowerLink(topArr[j], topArr[j + 1]);
                }

                Trapezoid temp2 = list[j].getData().getUpperRightNeighbor();
                if (!list[j + 1].getData().equals(temp2)) {
                    upperLink(topArr[j], temp2);
                }

                //only for non-repeats
                if (botArr[j] != botArr[j + 1]) {
                    upperLink(botArr[j], botArr[j + 1]);
                }

                temp2 = list[j].getData().getLowerRightNeighbor();
                if (!list[j + 1].getData().equals(temp2)) {
                    lowerLink(botArr[j], temp2);
                }
            }
        }

        //deal with the possible extra end trapezoids
        Trapezoid leftmost = null;
        Trapezoid rightmost = null;
        Trapezoid oldLeft = list[0].getData();
        Trapezoid oldRight = list[list.length - 1].getData();
        if (!segments.get(i).getLeftEndPoint().equals(oldLeft.getLeftPoint())) {
            //there is a leftmost trapezoid
            leftmost = new Trapezoid(oldLeft.getLeftPoint(), segments.get(i).getLeftEndPoint(),
                    oldLeft.getTopSegment(), oldLeft.getBottomSegment());
        }
        if (!segments.get(i).getRightEndPoint().equals(list[list.length - 1].getData().getRightPoint())) {
            //there is a rightmost trapezoid
            rightmost = new Trapezoid(segments.get(i).getRightEndPoint(), oldRight.getRightPoint(),
                    oldRight.getTopSegment(), oldRight.getBottomSegment());
        }

        //add remaining trapezoid links at the end
        if (leftmost != null) {
            lowerLink(oldLeft.getLowerLeftNeighbor(), leftmost);
            upperLink(oldLeft.getUpperLeftNeighbor(), leftmost);

            lowerLink(leftmost, botArr[0]);
            upperLink(leftmost, topArr[0]);
        } else {
            //link top & bot arr with appropriate left links of oldLeft
            if (oldLeft.getTopSegment().getLeftEndPoint().equals(oldLeft.getBottomSegment().getLeftEndPoint())) {
                //triangles, so no neighbors to worry about
            } else if (oldLeft.getTopSegment().getLeftEndPoint().equals(oldLeft.getLeftPoint())) {
                //upper half degenerates to a triangle
                lowerLink(oldLeft.getLowerLeftNeighbor(), botArr[0]);
            } else if (oldLeft.getBottomSegment().getLeftEndPoint().equals(oldLeft.getLeftPoint())) {
                //lower half degenerates to a triangle
                upperLink(oldLeft.getUpperLeftNeighbor(), topArr[0]);
            } else {
                //neither degenerates to a triangle
                lowerLink(oldLeft.getLowerLeftNeighbor(), botArr[0]);
                upperLink(oldLeft.getUpperLeftNeighbor(), topArr[0]);
            }
        }
        if (rightmost != null) {
            lowerLink(rightmost, oldRight.getLowerRightNeighbor());
            upperLink(rightmost, oldRight.getUpperRightNeighbor());

            lowerLink(botArr[botArr.length - 1], rightmost);
            upperLink(topArr[topArr.length - 1], rightmost);
        } else {
            //link the top & bot arr with the appropriate right links of oldRight
            if (oldRight.getTopSegment().getRightEndPoint().equals(oldRight.getBottomSegment().getRightEndPoint())) {
                //triangles, hence no right neighbors
            } else if (oldRight.getTopSegment().getRightEndPoint().equals(oldRight.getRightPoint())) {
                //upper half degenerates to a triangle
                lowerLink(botArr[botArr.length - 1], oldRight.getLowerRightNeighbor());
            } else if (oldRight.getBottomSegment().getRightEndPoint().equals(oldRight.getRightPoint())) {
                //lower half degenerates to a triangle
                upperLink(topArr[topArr.length - 1], oldRight.getUpperRightNeighbor());
            } else {
                //neither degenerates to a triangle
                lowerLink(botArr[botArr.length - 1], oldRight.getLowerRightNeighbor());
                upperLink(topArr[topArr.length - 1], oldRight.getUpperRightNeighbor());
            }
        }

        //create leaf structures ahead of time to deal with the duplication problem
        Leaf[] topLeaf = new Leaf[topArr.length];
        Leaf[] botLeaf = new Leaf[botArr.length];
        Leaf aa;
        for (int j = 0; j < topLeaf.length; j++) {
            if (j == 0 || topArr[j] != topArr[j - 1]) {
                //create a new topLeaf
                aa = new Leaf(topArr[j]);
                topArr[j].setLeaf(aa);
                topLeaf[j] = aa;
            } else {
                //reuse the old Leaf
                topLeaf[j] = topLeaf[j - 1];
            }

            if (j == 0 || botArr[j] != botArr[j - 1]) {
                //create a new botLeaf
                aa = new Leaf(botArr[j]);
                botArr[j].setLeaf(aa);
                botLeaf[j] = aa;
            } else {
                //reuse the old Leaf
                botLeaf[j] = botLeaf[j - 1];
            }
        }

        //then add nodes and node links...this should unlink the original trapezoids from the physical structure
        Node[] newStructures = new Node[list.length];
        for (int j = 0; j < list.length; j++) {
            Node yy = new YNode(segments.get(i));
            if (j == 0 && leftmost != null) {
                XNode xx = new XNode(segments.get(i).getLeftEndPoint());
                aa = new Leaf(leftmost);
                leftmost.setLeaf(aa);
                xx.setLeftChildNode(aa);
                xx.setRightChildNode(yy);

                newStructures[j] = xx;
            } else if (j == newStructures.length - 1 && rightmost != null) {
                XNode xx = new XNode(segments.get(i).getRightEndPoint());
                aa = new Leaf(rightmost);
                rightmost.setLeaf(aa);
                xx.setRightChildNode(aa);
                xx.setLeftChildNode(yy);

                newStructures[j] = xx;
            } else {
                newStructures[j] = yy;
            }

            yy.setLeftChildNode(topLeaf[j]);

            yy.setRightChildNode(botLeaf[j]);

            //insert the new structure in place of the old one
            //Node parent = list[j].getParentNode();

            //now there may be many parents...
            ArrayList<Node> parents = list[j].getParentNodes();
            for (int k = 0; k < parents.size(); k++) {
                Node parent = parents.get(k);
                if (parent.getLeftChildNode() == list[j]) {
                    //replace left child
                    parent.setLeftChildNode(newStructures[j]);
                } else {
                    parent.setRightChildNode(newStructures[j]);
                }
            }
        }
	}
	
	
    /**
     * Link two neighboring trapezoids that are lower neighbors
     *
     * @param left The left trapezoid to link
     * @param right The right trapezoid to link
     */
    private void lowerLink(Trapezoid left, Trapezoid right) {
        if (left != null) {
            left.setLowerRightNeighbor(right);
        }
        if (right != null) {
            right.setLowerLeftNeighbor(left);
        }
    }

    /**
     * Link two neighboring trapezoids that are upper neighbors
     *
     * @param left The left trapezoid to link
     * @param right The right trapezoid to link
     */
    private void upperLink(Trapezoid left, Trapezoid right) {
        if (left != null) {
            left.setUpperRightNeighbor(right);
        }
        if (right != null) {
            right.setUpperLeftNeighbor(left);
        }
    }

    /**
     * Get the list of trapezoids in the current structure intersected by the
     * segment.
     *
     * @param s The query segment
     * @return An array of trapezoids (Leaf array) intersected by the segment
     */
    private Leaf[] followSegment(Segment s) {
        //System.err.println("Follow segment not yet implemented");
        ArrayList<Leaf> list = new ArrayList<Leaf>();
        Leaf previous = findPoint(s.getLeftEndPoint(), s);
        //shift over leftward to make sure we have the first of any repeated trapezoids

        list.add(previous);
        while (previous != null && s.getRightEndPoint().compareTo(previous.getData().getRightPoint()) > 0) {
            //choose the next trapezoid in the sequence
            if (this.isPointAboveLine(previous.getData().getRightPoint(), s)) {
                previous = previous.getData().getLowerRightNeighbor().getLeaf();
            } else {
            	if(previous.getData().getUpperRightNeighbor() != null)
            		previous = previous.getData().getUpperRightNeighbor().getLeaf();
            	else
            		previous = null; 
            }
            if(previous != null)
            	list.add(previous);
        }

        Leaf[] arr = new Leaf[list.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    /**
     * Find the trapezoid in the trapezoidal map which contains the search
     * point.
     *
     * @param p The point to search for
     * @return The trapezoid containing the query point
     */
    public Leaf findPoint(Point p, Segment s) {
        Node current = root;
        while (!(current instanceof Leaf)) {
            if (current instanceof XNode) {
                int val = p.compareTo(((XNode) current).getData());

                if (val < 0) {
                    current = current.getLeftChildNode();
                } else {
                    current = current.getRightChildNode();
                }
            } else {//YNode
                //we are searching for a point, without segment information
                if (s == null) {
                    if (isPointAboveLine(p, ((YNode) current).getData())) {
                        current = current.getLeftChildNode();
                    } else {
                        current = current.getRightChildNode();
                    }
                } else {
                    //we are searching for a point on one of the segments
                    if (isPointAboveLine2(p, ((YNode) current).getData(), s)) {
                        current = current.getLeftChildNode();
                    } else {
                        current = current.getRightChildNode();
                    }
                }
            }
        }
        //System.out.println("Found Trapezoid Region ");// + (current instanceof Trapezoid));
        return ((Leaf) current);
    }

    /**
     * Leverages the findPont method which finds a leaf, and returns the corresponding trapezoid.
     * @param p The point to query
     * @return The trapezoid containing the point
     */
    public Trapezoid findPointTrap(Point p) {
        return findPoint(p, null).getData();
    }

    /**
     * Checks to see if a point is above the segment. Works by calculating y of
     * the segment at x of the point
     *
     * @param p The point of interest
     * @param s The segment of interest
     * @return True if on or above the segment; false otherwise
     */
    public static boolean isPointAboveLine(Point p, Segment s) {
        int x = p.getX();
        int y = p.getY();
        return (x - s.getLeftEndPoint().getX()) * s.getRightEndPoint().getY()
                + (s.getRightEndPoint().getX() - x) * s.getLeftEndPoint().getY()
                < y * (s.getRightEndPoint().getX() - s.getLeftEndPoint().getX());
    }

    /**
     * Checks if the input point on the given old segment lies above or below the new segment.
     * If the input point lies on the new segment, we determine above/below by which
     * segment has the higher slope.
     * 
     * @TODO Check this for nondegenerate case?
     * @param p The point under consideration
     * @param old The segment which the point lies on
     * @param pseg The segment to compare the point to
     * @return True if the point lies above segment pseg, or the point lies on pseg, on a segment of higher slope
     */
    public static boolean isPointAboveLine2(Point p, Segment old, Segment pseg) {
        //check if p is on segment old
        /*long x1 = p.getX();
         long x2 = old.getLeftEndPoint().getX();
         long x3 = old.getRightEndPoint().getX();
         long y1 = p.getY();
         long y2 = old.getLeftEndPoint().getY();
         long y3 = old.getRightEndPoint().getY();
         long result = (x2-x1)*(y3-y1) - (x3-x1)*(y2-y1);*/
        //according to the textbook, p can only lie on segment old if it is the left endpoint
        if (p.equals(old.getLeftEndPoint())) {
            //compare slopes
            long x1 = p.getX();
            long x2 = old.getRightEndPoint().getX();
            long x3 = pseg.getRightEndPoint().getX();
            long y1 = p.getY();
            long y2 = old.getRightEndPoint().getY();
            long y3 = pseg.getRightEndPoint().getY();
            long result = (x2 - x1) * (y3 - y1) - (x3 - x1) * (y2 - y1);
            return result > 0;
        }
        //if not, call isPointAboveLine
        return isPointAboveLine(p, old);
    }
	
	
	
	
	
}