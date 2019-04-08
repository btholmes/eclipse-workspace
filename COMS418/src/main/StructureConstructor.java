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
	
	/**
	 * The Directed Acyclic Graph for this map
	 */
	private DAG dag; 
	
	int minX = Integer.MAX_VALUE; 
	int maxX = Integer.MIN_VALUE; 
	int minY = Integer.MAX_VALUE; 
	int maxY = Integer.MIN_VALUE; 
	
	public StructureConstructor(String fileName, int order) throws FileNotFoundException {
		this.fileName = fileName; 
		File file = new File(fileName); 
		in = new Scanner(file); 
		this.order = order; 
		dag = new DAG(); 
		
		construct(); 
	}
	
	private void construct() {
		
		segments = new ArrayList<>(); 
		int i = 1; 
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
			  seg.setName("s" + i);
			  segments.add(seg); 
			  i++; 
		  }
		  
		  if(order == 1)
			  Collections.shuffle(segments);
		  
		  makeStruct();  
	}	
	
	public Trapezoid query(Point point) {
		return dag.searchPointInDAG(point, null).getData(); 
	}
	
	public Set<Trapezoid> getTrapezoids() {
		return dag.getTrapezoids(); 
	}
	
	private void makeStruct() {
        Point left = new Point(minX, minY);
        Point right = new Point(maxX, maxY);
        /**
         * This is the bounding box
         */
        Trapezoid t = new Trapezoid(left, right, new Segment(new Point(minX, maxY), new Point(maxX, maxY)),
                new Segment(new Point(minX, minY), new Point(maxX, minY)));
        Leaf f = new Leaf(t);
        t.setLeaf(f);
        dag.setRoot(f);
        
        
        for (int i = 0; i < segments.size() && segments.get(i) != null; i++) {
            //find the trapezoids intersected by arr[i]
            //System.out.println("in loop");
            ArrayList<Leaf> list = dag.followSegment(segments.get(i));
            
            if(list.size() == 1) {
            	handleContainedInSingleTrapezoid(list, i); 
            }else {
            	cutsThroughTrapezoid(list, i); 
            }
        }
	}
	
	private void handleContainedInSingleTrapezoid(ArrayList<Leaf> list, int i) {
		 //System.out.println("Case I");
        //split into 4 sections
        Trapezoid old = list.get(0).getData();
        Trapezoid lefty = new Trapezoid(old.getLeftP(), segments.get(i).getP1(), old.getTopSeg(), old.getBottomSeg());
        Trapezoid righty = new Trapezoid(segments.get(i).getQ1(), old.getRightP(), old.getTopSeg(), old.getBottomSeg());
        Trapezoid top = new Trapezoid(segments.get(i).getP1(), segments.get(i).getQ1(), old.getTopSeg(), segments.get(i));
        Trapezoid bottom = new Trapezoid(segments.get(i).getP1(), segments.get(i).getQ1(), segments.get(i), old.getBottomSeg());
        XNode ll = new XNode(segments.get(i).getP1());
        XNode rr = new XNode(segments.get(i).getQ1());
        YNode ss = new YNode(segments.get(i));

        Leaf leftyN = new Leaf(lefty);
        lefty.setLeaf(leftyN);
        Leaf rightyN = new Leaf(righty);
        righty.setLeaf(rightyN);
        Leaf topN = new Leaf(top);
        top.setLeaf(topN);
        Leaf bottomN = new Leaf(bottom);
        bottom.setLeaf(bottomN);
        if (!(!lefty.hasWidth() || !righty.hasWidth())) {

            //link all the nodes for the trapezoids
            ll.setLeft(leftyN);
            ll.setRight(rr);
            rr.setRight(rightyN);
            rr.setLeft(ss);
            ss.setLeft(topN);
            ss.setRight(bottomN);

            //connect the nodes to the old structure
            if (list.get(0).getParent() == null) {
                dag.setRoot(ll);
            } else {
                //the previous node might have more than one parent node
                ArrayList<Node> parents = list.get(0).getParents();
                for (int j = 0; j < parents.size(); j++) {
                    Node tempParent = parents.get(j);
                    if (tempParent.getLeft() == list.get(0)) {
                        tempParent.setLeft(ll);
                    } else {
                        tempParent.setRight(ll);
                    }
                }
            }

            //link the trapezoids together
            lowerLink(lefty, bottom);
            lowerLink(old.getlLeftNeighbor(), lefty);
            upperLink(lefty, top);
            upperLink(old.getuLeftNeighbor(), lefty);

            lowerLink(righty, old.getlRightNeighbor());
            lowerLink(bottom, righty);
            upperLink(righty, old.getuRightNeighbor());
            upperLink(top, righty);
        } else if (!lefty.hasWidth() && righty.hasWidth()) {//only left has zero width
            //link all the nodes for the trapezoids
            rr.setLeft(ss);
            rr.setRight(rightyN);
            ss.setLeft(topN);
            ss.setRight(bottomN);

            //connect the nodes to the old structure
            if (list.get(0).getParent() == null) {
                dag.setRoot(rr);
            } else {
                //the previous node might have more than one parent node
                ArrayList<Node> parents = list.get(0).getParents();
                for (int j = 0; j < parents.size(); j++) {
                    Node tempParent = parents.get(j);
                    if (tempParent.getLeft() == list.get(0)) {
                        tempParent.setLeft(rr);
                    } else {
                        tempParent.setRight(rr);
                    }
                }
            }

            //link the trapezoids together
            lowerLink(old.getlLeftNeighbor(), bottom);
            upperLink(old.getuLeftNeighbor(), top);

            lowerLink(righty, old.getlRightNeighbor());
            lowerLink(bottom, righty);
            upperLink(righty, old.getuRightNeighbor());
            upperLink(top, righty);
        } else if (!righty.hasWidth() && lefty.hasWidth()) {//only right has zero width
            //link all the nodes for the trapezoids
            ll.setLeft(leftyN);
            ll.setRight(ss);
            ss.setLeft(topN);
            ss.setRight(bottomN);

            //connect the nodes to the old structure
            if (list.get(0).getParent() == null) {
                dag.setRoot(ll);
            } else {
                //the previous node might have more than one parent node
                ArrayList<Node> parents = list.get(0).getParents();
                for (int j = 0; j < parents.size(); j++) {
                    Node tempParent = parents.get(j);
                    if (tempParent.getLeft() == list.get(0)) {
                        tempParent.setLeft(ll);
                    } else {
                        tempParent.setRight(ll);
                    }
                }
            }

            //link the trapezoids together
            lowerLink(lefty, bottom);
            lowerLink(old.getlLeftNeighbor(), lefty);
            upperLink(lefty, top);
            upperLink(old.getuLeftNeighbor(), lefty);

            lowerLink(bottom, old.getlRightNeighbor());
            upperLink(top, old.getuRightNeighbor());
        } else {
            //both have zero width

            //build the search structure
            ss.setLeft(topN);
            ss.setRight(bottomN);

            //connect the nodes to the old structure
            if (list.get(0).getParent() == null) {
                dag.setRoot(ss);
            } else {
                //the previous node might have more than one parent node
                ArrayList<Node> parents = list.get(0).getParents();
                for (int j = 0; j < parents.size(); j++) {
                    Node tempParent = parents.get(j);
                    if (tempParent.getLeft() == list.get(0)) {
                        tempParent.setLeft(ss);
                    } else {
                        tempParent.setRight(ss);
                    }
                }
            }

            //link the trapezoids together (this is nontrivial in degenerates cases)
            lowerLink(old.getlLeftNeighbor(), bottom);
            lowerLink(bottom, old.getlRightNeighbor());
            upperLink(old.getuLeftNeighbor(), top);
            upperLink(top, old.getuRightNeighbor());
        }
	}
	
	
	private void cutsThroughTrapezoid(ArrayList<Leaf> list, int i) {
	       //System.out.println("Case II");
        //the first and last cases get broken into 3 parts
        //the middle ones are different

        //if the left segment endpoint is not leftp of list.get(0).getData(), then
        //there is an extra trapezoid at the left end.  Likewise for rightp of list[n-1].getData()

        //for everything in the middle, we start with a single top and bottom trap for both
        //then we merge trapezoids together as needed
        //note that before merging, some trapezoids may have an endpoint which is null
        Trapezoid[] topArr = new Trapezoid[list.size()];
        Trapezoid[] botArr = new Trapezoid[list.size()];
        for (int j = 0; j < list.size(); j++) {
            //top is defined by the original upper segment, the new segment & two endpoints
            //left endpoint:
                /*
             * if j==0, is segment's left endpoint
             * else is old trap's left endpoint if it is above the segment
             */
            //right endpoint is similar
            if (j == 0) {
                Point rtP = null;
                
                if (segments.get(i).isPointAboveSeg(list.get(j).getData().getRightP()) ) {
                    rtP = list.get(j).getData().getRightP();
                }
                topArr[j] = new Trapezoid(segments.get(i).getP1(), rtP, list.get(j).getData().getTopSeg(), segments.get(i));
            } else if (j == list.size() - 1) {
                Point ltP = null;
                if (segments.get(i).isPointAboveSeg(list.get(j).getData().getLeftP()) ) {
                    ltP = list.get(j).getData().getLeftP();
                }
                topArr[j] = new Trapezoid(ltP, segments.get(i).getQ1(), list.get(j).getData().getTopSeg(), segments.get(i));
            } else {
                Point rtP = null;
                if (segments.get(i).isPointAboveSeg(list.get(j).getData().getRightP()) ) {
                    rtP = list.get(j).getData().getRightP();
                }
                Point ltP = null;
                if (segments.get(i).isPointAboveSeg(list.get(j).getData().getLeftP()) ) {
                    ltP = list.get(j).getData().getLeftP();
                }
                topArr[j] = new Trapezoid(ltP, rtP, list.get(j).getData().getTopSeg(), segments.get(i));
            }

            //the bottom array is constructed using a similar strategy
            if (j == 0) {
                Point rtP = null;
                if (!segments.get(i).isPointAboveSeg(list.get(j).getData().getRightP()) ) {
                    rtP = list.get(j).getData().getRightP();
                }
                botArr[j] = new Trapezoid(segments.get(i).getP1(), rtP, segments.get(i), list.get(j).getData().getBottomSeg());
            } else if (j == list.size() - 1) {
                Point ltP = null;
                if (!segments.get(i).isPointAboveSeg(list.get(j).getData().getLeftP()) ) {
                    ltP = list.get(j).getData().getLeftP();
                }
                botArr[j] = new Trapezoid(ltP, segments.get(i).getQ1(), segments.get(i), list.get(j).getData().getBottomSeg());
            } else {
                Point rtP = null;
                if (!segments.get(i).isPointAboveSeg(list.get(j).getData().getRightP()) ) {
                    rtP = list.get(j).getData().getRightP();
                }
                Point ltP = null;
                if (!segments.get(i).isPointAboveSeg(list.get(j).getData().getLeftP()) ) {
                    ltP = list.get(j).getData().getLeftP();
                }
                botArr[j] = new Trapezoid(ltP, rtP, segments.get(i), list.get(j).getData().getBottomSeg());
            }
        }

        //then merge degenerate trapezoids together (those with a null bounding point)
        int aTop = 0;
        int bTop;
        int aBot = 0;
        int bBot;
        boolean topHasRightP = false;
        boolean botHasRightP = false;
        for (int j = 0; j < list.size(); j++) {
            if (topArr[j].getRightP() != null) {
                bTop = j;
                //merge trapezoids aTop through bTop
                //we only want one trapezoid, so we just have bTop-aTop+1 pointers to it for now
                Trapezoid tempMerge = new Trapezoid(topArr[aTop].getLeftP(), topArr[bTop].getRightP(), topArr[aTop].getTopSeg(), segments.get(i));
                for (int k = aTop; k <= bTop; k++) {
                    //now there are duplicates of the same trapezoid unfortunately, but I think if we link them together left to right
                    //this shouldn't cause problems later...it just means a bit more storage use
                    topArr[k] = tempMerge;
                }
                aTop = j + 1;
            }

            if (botArr[j].getRightP() != null) {
                bBot = j;
                //merge trapezoids aBot through bBot
                Trapezoid tempMerge = new Trapezoid(botArr[aBot].getLeftP(), botArr[bBot].getRightP(), segments.get(i), botArr[aBot].getBottomSeg());
                for (int k = aBot; k <= bBot; k++) {
                    botArr[k] = tempMerge;
                }
                aBot = j + 1;
            }
        }

        //do trapezoid links...this should unlink the original trapezoids from the physical structure except at the ends
        //do all left links before doing right links in order to avoid linking errors
        for (int j = 0; j < list.size(); j++) {
            if (j != 0) {
                //update left links
                //link right to left
                //only recycle old links if they are not in the list to be removed

                //only when the trapezoids do not repeat
                if (topArr[j] != topArr[j - 1]) {
                    lowerLink(topArr[j - 1], topArr[j]);
                }

                //leave the upper left neighbor null unless we have something to set it to
                Trapezoid temp2 = list.get(j).getData().getuLeftNeighbor();
                if (!list.get(j-1).getData().equals(temp2)) {
                    upperLink(temp2, topArr[j]);
                }

                //only do this for non-repeating trapezoids
                if (botArr[j] != botArr[j - 1]) {
                    upperLink(botArr[j - 1], botArr[j]);
                }

                temp2 = list.get(j).getData().getlLeftNeighbor();
                if (!list.get(j-1).getData().equals(temp2)) {
                    lowerLink(temp2, botArr[j]);
                }

            }

        }
        for (int j = 0; j < list.size(); j++) {
            if (j != topArr.length - 1) {
                //update right links

                //only for non-repeats
                if (topArr[j] != topArr[j + 1]) {
                    lowerLink(topArr[j], topArr[j + 1]);
                }

                Trapezoid temp2 = list.get(j).getData().getuRightNeighbor();
                if (!list.get(j + 1).getData().equals(temp2)) {
                    upperLink(topArr[j], temp2);
                }

                //only for non-repeats
                if (botArr[j] != botArr[j + 1]) {
                    upperLink(botArr[j], botArr[j + 1]);
                }

                temp2 = list.get(j).getData().getlRightNeighbor();
                if (!list.get(j + 1).getData().equals(temp2)) {
                    lowerLink(botArr[j], temp2);
                }
            }
        }

        //deal with the possible extra end trapezoids
        Trapezoid leftmost = null;
        Trapezoid rightmost = null;
        Trapezoid oldLeft = list.get(0).getData();
        Trapezoid oldRight = list.get(list.size() - 1).getData();
        if (!segments.get(i).getP1().equals(oldLeft.getLeftP())) {
            //there is a leftmost trapezoid
            leftmost = new Trapezoid(oldLeft.getLeftP(), segments.get(i).getP1(),
                    oldLeft.getTopSeg(), oldLeft.getBottomSeg());
        }
        if (!segments.get(i).getQ1().equals(list.get(list.size() - 1).getData().getRightP())) {
            //there is a rightmost trapezoid
            rightmost = new Trapezoid(segments.get(i).getQ1(), oldRight.getRightP(),
                    oldRight.getTopSeg(), oldRight.getBottomSeg());
        }

        //add remaining trapezoid links at the end
        if (leftmost != null) {
            lowerLink(oldLeft.getlLeftNeighbor(), leftmost);
            upperLink(oldLeft.getuLeftNeighbor(), leftmost);

            lowerLink(leftmost, botArr[0]);
            upperLink(leftmost, topArr[0]);
        } else {
            //link top & bot arr with appropriate left links of oldLeft
            if (oldLeft.getTopSeg().getP1().equals(oldLeft.getBottomSeg().getP1())) {
                //triangles, so no neighbors to worry about
            } else if (oldLeft.getTopSeg().getP1().equals(oldLeft.getLeftP())) {
                //upper half degenerates to a triangle
                lowerLink(oldLeft.getlLeftNeighbor(), botArr[0]);
            } else if (oldLeft.getBottomSeg().getP1().equals(oldLeft.getLeftP())) {
                //lower half degenerates to a triangle
                upperLink(oldLeft.getuLeftNeighbor(), topArr[0]);
            } else {
                //neither degenerates to a triangle
                lowerLink(oldLeft.getlLeftNeighbor(), botArr[0]);
                upperLink(oldLeft.getuLeftNeighbor(), topArr[0]);
            }
        }
        if (rightmost != null) {
            lowerLink(rightmost, oldRight.getlRightNeighbor());
            upperLink(rightmost, oldRight.getuRightNeighbor());

            lowerLink(botArr[botArr.length - 1], rightmost);
            upperLink(topArr[topArr.length - 1], rightmost);
        } else {
            //link the top & bot arr with the appropriate right links of oldRight
            if (oldRight.getTopSeg().getQ1().equals(oldRight.getBottomSeg().getQ1())) {
                //triangles, hence no right neighbors
            } else if (oldRight.getTopSeg().getQ1().equals(oldRight.getRightP())) {
                //upper half degenerates to a triangle
                lowerLink(botArr[botArr.length - 1], oldRight.getlRightNeighbor());
            } else if (oldRight.getBottomSeg().getQ1().equals(oldRight.getRightP())) {
                //lower half degenerates to a triangle
                upperLink(topArr[topArr.length - 1], oldRight.getuRightNeighbor());
            } else {
                //neither degenerates to a triangle
                lowerLink(botArr[botArr.length - 1], oldRight.getlRightNeighbor());
                upperLink(topArr[topArr.length - 1], oldRight.getuRightNeighbor());
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
        Node[] newStructures = new Node[list.size()];
        for (int j = 0; j < list.size(); j++) {
            Node yy = new YNode(segments.get(i));
            if (j == 0 && leftmost != null) {
                XNode xx = new XNode(segments.get(i).getP1());
                aa = new Leaf(leftmost);
                leftmost.setLeaf(aa);
                xx.setLeft(aa);
                xx.setRight(yy);

                newStructures[j] = xx;
            } else if (j == newStructures.length - 1 && rightmost != null) {
                XNode xx = new XNode(segments.get(i).getQ1());
                aa = new Leaf(rightmost);
                rightmost.setLeaf(aa);
                xx.setRight(aa);
                xx.setLeft(yy);

                newStructures[j] = xx;
            } else {
                newStructures[j] = yy;
            }

            yy.setLeft(topLeaf[j]);

            yy.setRight(botLeaf[j]);

            //insert the new structure in place of the old one
            //Node parent = list.get(j).getParent();

            //now there may be many parents...
            ArrayList<Node> parents = list.get(j).getParents();
            for (int k = 0; k < parents.size(); k++) {
                Node parent = parents.get(k);
                if (parent.getLeft() == list.get(j)) {
                    //replace left child
                    parent.setLeft(newStructures[j]);
                } else {
                    parent.setRight(newStructures[j]);
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
            left.setlRightNeighbor(right);
        }
        if (right != null) {
            right.setlLeftNeighbor(left);
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
            left.setuRightNeighbor(right);
        }
        if (right != null) {
            right.setuLeftNeighbor(left);
        }
    }


	
	
	
	
	
}