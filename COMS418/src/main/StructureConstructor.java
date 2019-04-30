package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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
	private HashMap<String, Segment> segmentMap; 
	private HashMap<String, Point> pointMap; 
	private HashMap<String, String> pointLookUp; 
	private HashMap<String, Face> faceMap; 
	private Segment L; 
	private Segment R; 
	private Segment T; 
	private Segment B; 
	private Face unboundedFace; 
	
	private Point leftP; 
	private Point rightP; 
	
	/**
	 * 0 = original, 1 = shuffle
	 */
	private int order; 
	
	/**
	 * The Directed Acyclic Graph for this map
	 */
	private DAG dag; 
	
	double minX = Integer.MAX_VALUE; 
	double maxX = Integer.MIN_VALUE; 
	double minY = Integer.MAX_VALUE; 
	double maxY = Integer.MIN_VALUE; 
	
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
		segmentMap = new HashMap<>(); 
		pointMap = new HashMap<>(); 
		pointLookUp = new HashMap<>(); 
		faceMap = new HashMap<>(); 
		
		  while(in.hasNext()) {
			  String line = in.nextLine(); 
			  if(line == null || line.length() == 0) continue; 
			  
			  line = line.trim(); 
			  if(line.charAt(0) == 'v') {
				  makeVertex(line); 
			  }else if(line.charAt(0) == 'f') {
				  makeFace(line); 
			  }else if(line.charAt(0) == 'e') {
				  makeEdge(line); 
			  }
		  }
		  
		  minX = 0.0; 
		  maxX += 2.0; 
		  minY = 0.0; 
		  maxY += 2.0; 
		  
		  if(order == 1)
			  Collections.shuffle(segments);
		  
		  makeStruct();  
	}	
	
	
	
	public double getMinX() {
		return minX;
	}

	public void setMinX(double minX) {
		this.minX = minX;
	}

	public double getMaxX() {
		return maxX;
	}

	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}

	public double getMinY() {
		return minY;
	}

	public void setMinY(double minY) {
		this.minY = minY;
	}

	public double getMaxY() {
		return maxY;
	}

	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}
	
	

	public HashMap<String, String> getPointLookUp() {
		return pointLookUp;
	}

	public void setPointLookUp(HashMap<String, String> pointLookUp) {
		this.pointLookUp = pointLookUp;
	}
	
	public HashMap<String, Segment> getSegmentMap() {
		return segmentMap;
	}

	public void setSegmentMap(HashMap<String, Segment> segmentMap) {
		this.segmentMap = segmentMap;
	}

	private void makeVertex(String line) {
		line = line.replaceAll(" +", " "); 
		int start = line.indexOf('('); 
		String name = line.substring(0, start -1).trim(); 
		int end = line.indexOf(')'); 
		int comma = line.indexOf(','); 
		double a = Double.parseDouble(line.substring(start +1, comma).trim()); 
		double b = Double.parseDouble(line.substring(comma + 1, end).trim()); 
		String edge = line.substring(end + 1).trim(); 
		
		Point point = new Point(a,b); 
		point.setName(name);
		point.setEdge(edge);
		pointMap.put(name, point); 
		pointLookUp.put(point.toString(), name); 
		
		minX = Math.min(minX, point.getX()); 
		maxX = Math.max(maxX,  point.getX()); 
	  
		minY = Math.min(minY, point.getY()); 
		maxY = Math.max(maxY,  point.getY()); 
	}
	
	private void makeFace(String line) {
		line = line.replaceAll(" +", " "); 
		String[] array = line.split(" "); 
		String name = array[0].trim(); 
		String outter = array[1].trim();
		
		Face face = new Face(name);
		face.setOuterEdge(outter); 
		
		if(array[2].contains(";")) {
			String[] innerEdges = array[2].trim().split(";");
			ArrayList<String> edges = new ArrayList<>(Arrays.asList(innerEdges)); 
			face.setInnerEdges(edges);
		}else {
			face.addInnerEdge(array[2]);
		}
		
		if(outter.equalsIgnoreCase("nil")) {
			unboundedFace = face; 
		}
		
		faceMap.put(name,  face); 
	}
	
	private void makeEdge(String line) {
		line = line.replaceAll(" +", " "); 
		String[] array = line.split(" "); 
		String name = array[0]; 
		String origin = array[1]; 
		String twin = array[2]; 
		String face = array[3]; 
		String next = array[4]; 
		String prev = array[5]; 
		
		String[] nameSplit = name.trim().substring(1).split(","); 
		Segment seg = new Segment(pointMap.get("v" + nameSplit[0]), pointMap.get("v" + nameSplit[1])); 
		seg.setName(name);
		seg.setOrigin(origin);
		seg.setTwin(twin);
		seg.setFace(face);
		seg.setNext(next);
		seg.setPrev(prev);
		
		segmentMap.put(name,  seg); 
	    segments.add(seg); 		
	}
	
	
	
	public Face getUnboundedFace() {
		return unboundedFace;
	}

	public void setUnboundedFace(Face unboundedFace) {
		this.unboundedFace = unboundedFace;
	}

	public Trapezoid query(Point point) {
		return dag.searchPointInDAG(point, null).getData(); 
	}
	
	public Set<Trapezoid> getTrapezoids() {
		return dag.getTrapezoids(); 
	}
	
	private void makeStruct() {
        leftP = new Point(minX, minY);
        Point tLeft = new Point(minX, maxY); 
        rightP = new Point(maxX, maxY);
        Point bRight = new Point(maxX, minY); 
        /**
         * This is the bounding box
         */
        L = new Segment(leftP, tLeft); 
        L.setFace(unboundedFace.getName());
        
        R = new Segment(bRight, rightP); 
        R.setFace(unboundedFace.getName());
        
        T = new Segment(tLeft, rightP); 
        T.setFace(unboundedFace.getName());
        
        B = new Segment(leftP, bRight); 
        B.setFace(unboundedFace.getName());
        
        Trapezoid t = new Trapezoid(leftP, rightP, T, B);
        t.setFace(unboundedFace.getName());
        
        Leaf f = new Leaf(t);
        t.setLeaf(f);
        dag.setRoot(f);
        
        for (int i = 0; i < segments.size() && segments.get(i) != null; i++) {
            ArrayList<Leaf> list = dag.followSegment(segments.get(i));
            
            if(list.size() == 1) {
            	handleContainedInSingleTrapezoid(list, i); 
            }else {
            	cutsThroughTrapezoid(list, i); 
            }
        }
	}
	
	
	public Segment getL() {
		return L;
	}

	public void setL(Segment l) {
		L = l;
	}

	public Segment getR() {
		return R;
	}

	public void setR(Segment r) {
		R = r;
	}

	public Segment getT() {
		return T;
	}

	public void setT(Segment t) {
		T = t;
	}

	public Segment getB() {
		return B;
	}

	public void setB(Segment b) {
		B = b;
	}
	

	public Point getLeftP() {
		return leftP;
	}

	public void setLeftP(Point leftP) {
		this.leftP = leftP;
	}

	public Point getRightP() {
		return rightP;
	}

	public void setRightP(Point rightP) {
		this.rightP = rightP;
	}

	/**
	 * Will cut the containing trapezoid into either 4 new trapezoids, 3 new trapezoids, or 2 new trapezoids
	 * @param list
	 * @param i
	 */
	private void handleContainedInSingleTrapezoid(ArrayList<Leaf> list, int i) {
        Trapezoid old = list.get(0).getData();
        
        Trapezoid tLeft = new Trapezoid(old.getLeftP(), segments.get(i).getP1(), old.getTopSeg(), old.getBottomSeg());
//        tLeft.setFace(old.getFace());
        Trapezoid tRight = new Trapezoid(segments.get(i).getQ1(), old.getRightP(), old.getTopSeg(), old.getBottomSeg());
//        tRight.setFace(old.getFace());
        Trapezoid tAbove = new Trapezoid(segments.get(i).getP1(), segments.get(i).getQ1(), old.getTopSeg(), segments.get(i));
//        tAbove.setFace(segments.get(i).getFace());
        Trapezoid tBelow = new Trapezoid(segments.get(i).getP1(), segments.get(i).getQ1(), segments.get(i), old.getBottomSeg());
//        Segment seg = segments.get(i); 
//        Segment twin = segmentMap.get(seg.getTwin()); 
//        tBelow.setFace(twin.getFace());
        
        XNode leftX = new XNode(segments.get(i).getP1());
        XNode rightX = new XNode(segments.get(i).getQ1());
        YNode yNode = new YNode(segments.get(i));

        Leaf leftLeaf = new Leaf(tLeft);
        tLeft.setLeaf(leftLeaf);
        
        Leaf rightLeaf = new Leaf(tRight);
        tRight.setLeaf(rightLeaf);
        
        Leaf topLeaf = new Leaf(tAbove);
        tAbove.setLeaf(topLeaf);
        
        Leaf bottomLeaf = new Leaf(tBelow);
        tBelow.setLeaf(bottomLeaf);
        
        /**
         * Breaks the parent trapezoid into 4 new trapezoids
         */
        if (tLeft.hasWidth() && tRight.hasWidth()) {
//        if (true) {
            leftX.setLeft(leftLeaf);
            leftX.setRight(rightX);
            rightX.setRight(rightLeaf);
            rightX.setLeft(yNode);
            yNode.setLeft(topLeaf);
            yNode.setRight(bottomLeaf);

            
            if (list.get(0).getParent() == null) {
                dag.setRoot(leftX);
            } else {
            	/**
            	 * Update the parents of the trapezoid
            	 */
                ArrayList<Node> parents = list.get(0).getParents();
                for (int j = 0; j < parents.size(); j++) {
                    Node tempParent = parents.get(j);
                    if (tempParent.getLeft() == list.get(0)) {
                        tempParent.setLeft(leftX);
                    } else {
                        tempParent.setRight(leftX);
                    }
                }
            }

            /**
             * Set the adjacent trapezoids
             */
            lowerLink(tLeft, tBelow);
            lowerLink(old.getlLeftNeighbor(), tLeft);
            upperLink(tLeft, tAbove);
            upperLink(old.getuLeftNeighbor(), tLeft);

            lowerLink(tRight, old.getlRightNeighbor());
            lowerLink(tBelow, tRight);
            upperLink(tRight, old.getuRightNeighbor());
            upperLink(tAbove, tRight);
        } 
        /**
         * Breaks parent into 3 trapezoids top, bottom, right
         */
        else if (!tLeft.hasWidth() && tRight.hasWidth()) {
        	
        	rightX.setLeft(yNode);
        	rightX.setRight(rightLeaf);
            yNode.setLeft(topLeaf);
            yNode.setRight(bottomLeaf);

            if (list.get(0).getParent() == null) {
                dag.setRoot(rightX);
            } else {
                ArrayList<Node> parents = list.get(0).getParents();
                for (int j = 0; j < parents.size(); j++) {
                    Node tempParent = parents.get(j);
                    if (tempParent.getLeft() == list.get(0)) {
                        tempParent.setLeft(rightX);
                    } else {
                        tempParent.setRight(rightX);
                    }
                }
            }

            lowerLink(old.getlLeftNeighbor(), tBelow);
            upperLink(old.getuLeftNeighbor(), tAbove);

            lowerLink(tRight, old.getlRightNeighbor());
            lowerLink(tBelow, tRight);
            upperLink(tRight, old.getuRightNeighbor());
            upperLink(tAbove, tRight);
        } 
        /**
         * Breaks parent into 3 trapezoids, left, top, bottom
         */
        else if (!tRight.hasWidth() && tLeft.hasWidth()) {
        	leftX.setLeft(leftLeaf);
        	leftX.setRight(yNode);
        	yNode.setLeft(topLeaf);
            yNode.setRight(bottomLeaf);

            if (list.get(0).getParent() == null) {
                dag.setRoot(leftX);
            } else {
                ArrayList<Node> parents = list.get(0).getParents();
                for (int j = 0; j < parents.size(); j++) {
                    Node tempParent = parents.get(j);
                    if (tempParent.getLeft() == list.get(0)) {
                        tempParent.setLeft(leftX);
                    } else {
                        tempParent.setRight(leftX);
                    }
                }
            }

            lowerLink(tLeft, tBelow);
            lowerLink(old.getlLeftNeighbor(), tLeft);
            upperLink(tLeft, tAbove);
            upperLink(old.getuLeftNeighbor(), tLeft);

            lowerLink(tBelow, old.getlRightNeighbor());
            upperLink(tAbove, old.getuRightNeighbor());
        } 
        /**
         * Breaks parent into 2 trapezoids, one top, and one bottom
         */
        else {
        	yNode.setLeft(topLeaf);
            yNode.setRight(bottomLeaf);

            if (list.get(0).getParent() == null) {
                dag.setRoot(yNode);
            } else {
                ArrayList<Node> parents = list.get(0).getParents();
                for (int j = 0; j < parents.size(); j++) {
                    Node tempParent = parents.get(j);
                    if (tempParent.getLeft() == list.get(0)) {
                        tempParent.setLeft(yNode);
                    } else {
                        tempParent.setRight(yNode);
                    }
                }
            }

            lowerLink(old.getlLeftNeighbor(), tBelow);
            lowerLink(tBelow, old.getlRightNeighbor());
            upperLink(old.getuLeftNeighbor(), tAbove);
            upperLink(tAbove, old.getuRightNeighbor());
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