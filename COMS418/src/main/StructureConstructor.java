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
	
	private Point c1; 
	private Point c2; 
	private Point c3; 
	private Point c4; 
	
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
		  
		  c1 = new Point(minX, maxY); 
		  c2 = new Point(minX, minY); 
		  c3 = new Point(maxX, minY); 
		  c4 = new Point(maxX, maxY); 
		  
		  if(order == 1)
			  Collections.shuffle(segments);
		  
		  makeStruct();  
	}	
	
	
	
	public Point getC1() {
		return c1;
	}

	public void setC1(Point c1) {
		this.c1 = c1;
	}

	public Point getC2() {
		return c2;
	}

	public void setC2(Point c2) {
		this.c2 = c2;
	}

	public Point getC3() {
		return c3;
	}

	public void setC3(Point c3) {
		this.c3 = c3;
	}

	public Point getC4() {
		return c4;
	}

	public void setC4(Point c4) {
		this.c4 = c4;
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
	 * 
	 * @param list
	 * @param leftX
	 * @param rightX
	 * @param yNode
	 * @param leftLeaf
	 * @param rightLeaf
	 * @param topLeaves
	 * @param bottomLeaf
	 * @param tLeft
	 * @param tBelow
	 * @param tRight
	 * @param tAbove
	 * @param old
	 */
	private void handleFourCase(ArrayList<Leaf> list, XNode leftX, XNode rightX, YNode yNode, Leaf leftLeaf, Leaf rightLeaf, Leaf topLeaves, Leaf bottomLeaf, Trapezoid tLeft, Trapezoid tBelow, Trapezoid tRight, Trapezoid tAbove, Trapezoid old) {
        leftX.setLeft(leftLeaf);
        leftX.setRight(rightX);
        rightX.setRight(rightLeaf);
        rightX.setLeft(yNode);
        yNode.setLeft(topLeaves);
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
	 * 
	 * @param list
	 * @param leftX
	 * @param rightX
	 * @param yNode
	 * @param leftLeaf
	 * @param rightLeaf
	 * @param topLeaves
	 * @param bottomLeaf
	 * @param tLeft
	 * @param tBelow
	 * @param tRight
	 * @param tAbove
	 * @param old
	 */
	private void handleRightThreeCase(ArrayList<Leaf> list, XNode leftX, XNode rightX, YNode yNode, Leaf leftLeaf, Leaf rightLeaf, Leaf topLeaves, Leaf bottomLeaf, Trapezoid tLeft, Trapezoid tBelow, Trapezoid tRight, Trapezoid tAbove, Trapezoid old) {
      	rightX.setLeft(yNode);
    	rightX.setRight(rightLeaf);
        yNode.setLeft(topLeaves);
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
	 * 
	 * @param list
	 * @param leftX
	 * @param rightX
	 * @param yNode
	 * @param leftLeaf
	 * @param rightLeaf
	 * @param topLeaves
	 * @param bottomLeaf
	 * @param tLeft
	 * @param tBelow
	 * @param tRight
	 * @param tAbove
	 * @param old
	 */
	private void handleLeftThreeCase(ArrayList<Leaf> list, XNode leftX, XNode rightX, YNode yNode, Leaf leftLeaf, Leaf rightLeaf, Leaf topLeaves, Leaf bottomLeaf, Trapezoid tLeft, Trapezoid tBelow, Trapezoid tRight, Trapezoid tAbove, Trapezoid old) {
    	leftX.setLeft(leftLeaf);
    	leftX.setRight(yNode);
    	yNode.setLeft(topLeaves);
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
	 * 
	 * @param list
	 * @param leftX
	 * @param rightX
	 * @param yNode
	 * @param leftLeaf
	 * @param rightLeaf
	 * @param topLeaves
	 * @param bottomLeaf
	 * @param tLeft
	 * @param tBelow
	 * @param tRight
	 * @param tAbove
	 * @param old
	 */
	private void handleTwoCase(ArrayList<Leaf> list, XNode leftX, XNode rightX, YNode yNode, Leaf leftLeaf, Leaf rightLeaf, Leaf topLeaves, Leaf bottomLeaf, Trapezoid tLeft, Trapezoid tBelow, Trapezoid tRight, Trapezoid tAbove, Trapezoid old) {
		yNode.setLeft(topLeaves);
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
        
        Leaf topLeaves = new Leaf(tAbove);
        tAbove.setLeaf(topLeaves);
        
        Leaf bottomLeaf = new Leaf(tBelow);
        tBelow.setLeaf(bottomLeaf);
        
        /**
         * Breaks the parent trapezoid into 4 new trapezoids
         */
        if (tLeft.hasWidth() && tRight.hasWidth()) { 
        	handleFourCase(list,  leftX,  rightX,  yNode,  leftLeaf,  rightLeaf,  topLeaves,  bottomLeaf,  tLeft,  tBelow,  tRight,  tAbove,  old); 

        } 
        /**
         * Breaks parent into 3 trapezoids top, bottom, right
         */
        else if (!tLeft.hasWidth() && tRight.hasWidth()) {
        	handleRightThreeCase(list,  leftX,  rightX,  yNode,  leftLeaf,  rightLeaf,  topLeaves,  bottomLeaf,  tLeft,  tBelow,  tRight,  tAbove,  old); 
        } 
        /**
         * Breaks parent into 3 trapezoids, left, top, bottom
         */
        else if (!tRight.hasWidth() && tLeft.hasWidth()) {
        	handleLeftThreeCase(list,  leftX,  rightX,  yNode,  leftLeaf,  rightLeaf,  topLeaves,  bottomLeaf,  tLeft,  tBelow,  tRight,  tAbove,  old); 
        } 
        /**
         * Breaks parent into 2 trapezoids, one top, and one bottom
         */
        else {
        	handleTwoCase(list,  leftX,  rightX,  yNode,  leftLeaf,  rightLeaf,  topLeaves,  bottomLeaf,  tLeft,  tBelow,  tRight,  tAbove,  old); 
        	
        }
	}
	
	
	/**
	 * 
	 * @param list
	 * @param i
	 */
	private void cutsThroughTrapezoid(ArrayList<Leaf> list, int i) {
        Trapezoid[] trapezoidsAbove = new Trapezoid[list.size()];
        Trapezoid[] trapezoidsBelow = new Trapezoid[list.size()];
        
        fillTrapezoidArrays(list, i, trapezoidsAbove, trapezoidsBelow); 
        mergeTrapezoids(list, i, trapezoidsAbove, trapezoidsBelow); 
        linkTrapezoids(list, i, trapezoidsAbove, trapezoidsBelow); 
	}
	
	/**
	 * 
	 * @param trapezoidsAbove
	 * @param list
	 * @param i
	 * @param j
	 */
	private void handleTop(Trapezoid[] trapezoidsAbove, ArrayList<Leaf> list, int i, int j) {
        if (j == 0) {
            Point rightPoint = null;
            
            if (segments.get(i).isPointAboveSeg(list.get(j).getData().getRightP()) ) {
            	rightPoint = list.get(j).getData().getRightP();
            }
            trapezoidsAbove[j] = new Trapezoid(segments.get(i).getP1(), rightPoint, list.get(j).getData().getTopSeg(), segments.get(i));
            
        } else if (j == list.size() - 1) {
            Point leftPoint = null;
            if (segments.get(i).isPointAboveSeg(list.get(j).getData().getLeftP()) ) {
            	leftPoint = list.get(j).getData().getLeftP();
            }
            trapezoidsAbove[j] = new Trapezoid(leftPoint, segments.get(i).getQ1(), list.get(j).getData().getTopSeg(), segments.get(i));
        } else {
            Point rightPoint = null;
            if (segments.get(i).isPointAboveSeg(list.get(j).getData().getRightP()) ) {
            	rightPoint = list.get(j).getData().getRightP();
            }
            Point leftPoint = null;
            if (segments.get(i).isPointAboveSeg(list.get(j).getData().getLeftP()) ) {
            	leftPoint = list.get(j).getData().getLeftP();
            }
            trapezoidsAbove[j] = new Trapezoid(leftPoint, rightPoint, list.get(j).getData().getTopSeg(), segments.get(i));
        }
	}
	
	
	/**
	 * 
	 * @param list
	 * @param i
	 * @param trapezoidsAbove
	 * @param trapezoidsBelow
	 */
	private void fillTrapezoidArrays(ArrayList<Leaf> list, int i, Trapezoid[] trapezoidsAbove, Trapezoid[] trapezoidsBelow){
	       for (int j = 0; j < list.size(); j++) {

	    	   	handleTop(trapezoidsAbove, list, i, j); 
	    	   	handleBottom(trapezoidsBelow, list, i, j); 
	        }
	}
	
	/**
	 * 
	 * @param trapezoidsBelow
	 * @param list
	 * @param i
	 * @param j
	 */
	private void handleBottom(Trapezoid[] trapezoidsBelow, ArrayList<Leaf> list, int i, int j) {
        /**
         * Now fill bottom array
         */
        if (j == 0) {
            Point rightPoint = null;
            if (!segments.get(i).isPointAboveSeg(list.get(j).getData().getRightP()) ) {
            	rightPoint = list.get(j).getData().getRightP();
            }
            trapezoidsBelow[j] = new Trapezoid(segments.get(i).getP1(), rightPoint, segments.get(i), list.get(j).getData().getBottomSeg());
        } else if (j == list.size() - 1) {
            Point leftPoint = null;
            if (!segments.get(i).isPointAboveSeg(list.get(j).getData().getLeftP()) ) {
            	leftPoint = list.get(j).getData().getLeftP();
            }
            trapezoidsBelow[j] = new Trapezoid(leftPoint, segments.get(i).getQ1(), segments.get(i), list.get(j).getData().getBottomSeg());
        } else {
            Point rightPoint = null;
            if (!segments.get(i).isPointAboveSeg(list.get(j).getData().getRightP()) ) {
            	rightPoint = list.get(j).getData().getRightP();
            }
            Point leftPoint = null;
            if (!segments.get(i).isPointAboveSeg(list.get(j).getData().getLeftP()) ) {
            	leftPoint = list.get(j).getData().getLeftP();
            }
            trapezoidsBelow[j] = new Trapezoid(leftPoint, rightPoint, segments.get(i), list.get(j).getData().getBottomSeg());
        }
	}
	
	
	/**
	 * 
	 * @param list
	 * @param i
	 * @param trapezoidsAbove
	 * @param trapezoidsBelow
	 */
	private void mergeTrapezoids(ArrayList<Leaf> list, int i, Trapezoid[] trapezoidsAbove, Trapezoid[] trapezoidsBelow) {
        int leftT = 0;
        int rightT = 0;
        int leftB = 0;
        int rightB = 0;
        
        for (int j = 0; j < list.size(); j++) {
            if (trapezoidsAbove[j].getRightP() != null) {
                rightT = j;
                Trapezoid tempMerge = new Trapezoid(trapezoidsAbove[leftT].getLeftP(), trapezoidsAbove[rightT].getRightP(), trapezoidsAbove[leftT].getTopSeg(), segments.get(i));
                for (int k = leftT; k <= rightT; k++) {
                    trapezoidsAbove[k] = tempMerge;
                }
                leftT = j + 1;
            }
            
            if (trapezoidsBelow[j].getRightP() != null) {
                rightB = j;
                Trapezoid tempMerge = new Trapezoid(trapezoidsBelow[leftB].getLeftP(), trapezoidsBelow[rightB].getRightP(), segments.get(i), trapezoidsBelow[leftB].getBottomSeg());
                for (int k = leftB; k <= rightB; k++) {
                    trapezoidsBelow[k] = tempMerge;
                }
                leftB = j + 1;
            }
       
      
        }
	}
	
	/**
	 * 
	 * @param list
	 * @param j
	 * @param trapezoidsAbove
	 * @param trapezoidsBelow
	 */
	private void handleUppersLeft(ArrayList<Leaf> list, int j, Trapezoid[] trapezoidsAbove, Trapezoid[] trapezoidsBelow) {
        /**
         * Link upper trapezoid array together setting their lower left and right neighbor
         */
        if (trapezoidsAbove[j] != trapezoidsAbove[j - 1]) {
            lowerLink(trapezoidsAbove[j - 1], trapezoidsAbove[j]);
        }

        /**
         * If upper left neighbor is not null, link it
         */
        Trapezoid upperLeftT = list.get(j).getData().getuLeftNeighbor();
        if (!list.get(j-1).getData().equals(upperLeftT)) {
            upperLink(upperLeftT, trapezoidsAbove[j]);
        }
	}
	
	/**
	 * 
	 * @param list
	 * @param j
	 * @param trapezoidsAbove
	 * @param trapezoidsBelow
	 */
	private void handleLowersLeft(ArrayList<Leaf> list, int j, Trapezoid[] trapezoidsAbove, Trapezoid[] trapezoidsBelow) {
        /**
         * Now do the same for lower trapezoid array, as long as they aren't the same trapezoid
         */
        if (trapezoidsBelow[j] != trapezoidsBelow[j - 1]) {
            upperLink(trapezoidsBelow[j - 1], trapezoidsBelow[j]);
        }

        Trapezoid lowerLeftT = list.get(j).getData().getlLeftNeighbor();
        if (!list.get(j-1).getData().equals(lowerLeftT)) {
            lowerLink(lowerLeftT, trapezoidsBelow[j]);
        }
	}
	
	/**
	 * 
	 * @param list
	 * @param j
	 * @param trapezoidsAbove
	 * @param trapezoidsBelow
	 */
	private void handleUppersRight(ArrayList<Leaf> list, int j, Trapezoid[] trapezoidsAbove, Trapezoid[] trapezoidsBelow) {
		 if (trapezoidsAbove[j] != trapezoidsAbove[j + 1]) {
             lowerLink(trapezoidsAbove[j], trapezoidsAbove[j + 1]);
         }

         Trapezoid upperRight = list.get(j).getData().getuRightNeighbor();
         if (!list.get(j + 1).getData().equals(upperRight)) {
             upperLink(trapezoidsAbove[j], upperRight);
         }
		
	}
	
	/**
	 * 
	 * @param list
	 * @param j
	 * @param trapezoidsAbove
	 * @param trapezoidsBelow
	 */
	private void handleLowersRight(ArrayList<Leaf> list, int j, Trapezoid[] trapezoidsAbove, Trapezoid[] trapezoidsBelow) {
		/**
		 * Check for repeats before linking
		 */
        if (trapezoidsBelow[j] != trapezoidsBelow[j + 1]) {
            upperLink(trapezoidsBelow[j], trapezoidsBelow[j + 1]);
        }

        Trapezoid lowerRight = list.get(j).getData().getlRightNeighbor();
        if (!list.get(j + 1).getData().equals(lowerRight)) {
            lowerLink(trapezoidsBelow[j], lowerRight);
        }
	}

	/**
	 * 
	 * @param list
	 * @param trapezoidsAbove
	 * @param trapezoidsBelow
	 * @param farLeft
	 * @param oldLeft
	 */
	private void handleFarLeft(ArrayList<Leaf> list, Trapezoid[] trapezoidsAbove, Trapezoid[] trapezoidsBelow, Trapezoid farLeft, Trapezoid oldLeft) {
        if (farLeft != null) {
            lowerLink(oldLeft.getlLeftNeighbor(), farLeft);
            upperLink(oldLeft.getuLeftNeighbor(), farLeft);

            lowerLink(farLeft, trapezoidsBelow[0]);
            upperLink(farLeft, trapezoidsAbove[0]);
        } else {
            /**
             * Set the links
             */
        	if (oldLeft.getTopSeg().getP1().equals(oldLeft.getLeftP())) {
        		/**
        		 * Shared left top vertice, is a triangle
        		 */
                lowerLink(oldLeft.getlLeftNeighbor(), trapezoidsBelow[0]);
            } else if (oldLeft.getBottomSeg().getP1().equals(oldLeft.getLeftP())) {
                /**
                 * Shared bottom left vertice is a triangle
                 */
                upperLink(oldLeft.getuLeftNeighbor(), trapezoidsAbove[0]);
            } else {
                /**
                 * Neither share a vertice
                 */
                lowerLink(oldLeft.getlLeftNeighbor(), trapezoidsBelow[0]);
                upperLink(oldLeft.getuLeftNeighbor(), trapezoidsAbove[0]);
            }
        }
	}

	/**
	 * 
	 * @param list
	 * @param trapezoidsAbove
	 * @param trapezoidsBelow
	 * @param farRight
	 * @param oldRight
	 */
	private void handleFarRight(ArrayList<Leaf> list, Trapezoid[] trapezoidsAbove, Trapezoid[] trapezoidsBelow, Trapezoid farRight, Trapezoid oldRight) {
	      if (farRight != null) {
	            lowerLink(farRight, oldRight.getlRightNeighbor());
	            upperLink(farRight, oldRight.getuRightNeighbor());

	            lowerLink(trapezoidsBelow[trapezoidsBelow.length - 1], farRight);
	            upperLink(trapezoidsAbove[trapezoidsAbove.length - 1], farRight);
	        } else {
	            if (oldRight.getTopSeg().getQ1().equals(oldRight.getRightP())) {
	        		/**
	        		 * Shared right top vertice, is a triangle
	        		 */
	                lowerLink(trapezoidsBelow[trapezoidsBelow.length - 1], oldRight.getlRightNeighbor());
	            } else if (oldRight.getBottomSeg().getQ1().equals(oldRight.getRightP())) {
	        		/**
	        		 * Shared right bottom vertice, is a triangle
	        		 */
	                upperLink(trapezoidsAbove[trapezoidsAbove.length - 1], oldRight.getuRightNeighbor());
	            } else {
	        		/**
	        		 * Neither share a vertice
	        		 */
	                lowerLink(trapezoidsBelow[trapezoidsBelow.length - 1], oldRight.getlRightNeighbor());
	                upperLink(trapezoidsAbove[trapezoidsAbove.length - 1], oldRight.getuRightNeighbor());
	            }
	        }
	}

	/**
	 * 
	 * @param topLeaves
	 * @param bottomLeaves
	 * @param trapezoidsAbove
	 * @param trapezoidsBelow
	 */
	private void fillTopAndBottomLeaves(Leaf[] topLeaves, Leaf[] bottomLeaves, Trapezoid[] trapezoidsAbove, Trapezoid[] trapezoidsBelow) {
        Leaf temp;
        for (int j = 0; j < topLeaves.length; j++) {
            if (j == 0 || trapezoidsAbove[j] != trapezoidsAbove[j - 1]) {
                /**
                 * New leaf
                 */
            	temp = new Leaf(trapezoidsAbove[j]);
                trapezoidsAbove[j].setLeaf(temp);
                topLeaves[j] = temp;
            } else {
                /**
                 * Use old leaf
                 */
                topLeaves[j] = topLeaves[j - 1];
            }

            if (j == 0 || trapezoidsBelow[j] != trapezoidsBelow[j - 1]) {
                /**
                 * New leaf
                 */
            	temp = new Leaf(trapezoidsBelow[j]);
                trapezoidsBelow[j].setLeaf(temp);
                bottomLeaves[j] = temp;
            } else {
                /**
                 * Use old leaf
                 */
                bottomLeaves[j] = bottomLeaves[j - 1];
            }
        }
	}

	/**
	 * 
	 * @param list
	 * @param i
	 * @param trapezoidsAbove
	 * @param trapezoidsBelow
	 */
	private void linkTrapezoids(ArrayList<Leaf> list, int i, Trapezoid[] trapezoidsAbove, Trapezoid[] trapezoidsBelow) {
		
        /**
         * Link trapezoids, left then right
         */
        for (int j = 1; j < list.size(); j++) {
            	handleUppersLeft(list, j, trapezoidsAbove, trapezoidsBelow); 
            	handleLowersLeft(list, j, trapezoidsAbove, trapezoidsBelow); 
        }
        
        for (int j = 0; j < list.size()-1; j++) {       		
        		handleUppersRight(list, j, trapezoidsAbove, trapezoidsBelow); 
        		handleLowersRight(list, j, trapezoidsAbove, trapezoidsBelow); 
        }

        /**
         * Cases where either farLeft or farRight trapezoid breaks into 3 parts
         */
        Trapezoid farLeft = null;
        Trapezoid farRight = null;
        Trapezoid oldLeft = list.get(0).getData();
        Trapezoid oldRight = list.get(list.size() - 1).getData();
        if (!segments.get(i).getP1().equals(oldLeft.getLeftP())) {
            farLeft = new Trapezoid(oldLeft.getLeftP(), segments.get(i).getP1(),
                    oldLeft.getTopSeg(), oldLeft.getBottomSeg());
        }
        if (!segments.get(i).getQ1().equals(list.get(list.size() - 1).getData().getRightP())) {
            farRight = new Trapezoid(segments.get(i).getQ1(), oldRight.getRightP(),
                    oldRight.getTopSeg(), oldRight.getBottomSeg());
        }
        
        handleFarLeft(list, trapezoidsAbove, trapezoidsBelow, farLeft, oldLeft); 
        handleFarRight(list, trapezoidsAbove, trapezoidsBelow, farRight, oldRight); 

        
        

        Leaf[] topLeaves = new Leaf[trapezoidsAbove.length];
        Leaf[] bottomLeaves = new Leaf[trapezoidsBelow.length];
        
        fillTopAndBottomLeaves(topLeaves, bottomLeaves, trapezoidsAbove, trapezoidsBelow); 
        
        
        updateStructure(list, i, farLeft, farRight, topLeaves, bottomLeaves); 
		
	}

	/**
	 * 
	 * @param farLeft
	 * @param i
	 * @param j
	 * @param yNode
	 * @param newNodes
	 */
	private void setFarLeft(Trapezoid farLeft, int i, int j, Node yNode, Node[] newNodes) {
		Leaf temp; 
        XNode xNode = new XNode(segments.get(i).getP1());
        temp = new Leaf(farLeft);
        farLeft.setLeaf(temp);
        xNode.setLeft(temp);
        xNode.setRight(yNode);

        newNodes[j] = xNode;
	}
	
	/**
	 * 
	 * @param farRight
	 * @param i
	 * @param j
	 * @param yNode
	 * @param newNodes
	 */
	private void setFarRight(Trapezoid farRight, int i, int j, Node yNode, Node[] newNodes) {
		 Leaf temp; 
		 XNode xNode = new XNode(segments.get(i).getQ1());
         temp = new Leaf(farRight);
         farRight.setLeaf(temp);
         xNode.setRight(temp);
         xNode.setLeft(yNode);

         newNodes[j] = xNode;
	}

	/**
	 * YNode is segment, xNode is point
	 * 
	 * @param list
	 * @param i
	 * @param farLeft
	 * @param farRight
	 * @param topLeaves
	 * @param bottomLeaves
	 */
	private void updateStructure(ArrayList<Leaf> list, int i,  Trapezoid farLeft, Trapezoid farRight, Leaf[] topLeaves, Leaf[] bottomLeaves) {

		Node[] newNodes = new Node[list.size()];
        for (int j = 0; j < list.size(); j++) {
            Node yNode = new YNode(segments.get(i));
            if (j == 0 && farLeft != null) {
            	setFarLeft(farLeft, i, j, yNode, newNodes); 
            } else if (j == newNodes.length - 1 && farRight != null) {
               setFarRight(farRight, i, j, yNode, newNodes); 
            } else {
            	newNodes[j] = yNode;
            }

            yNode.setLeft(topLeaves[j]);
            yNode.setRight(bottomLeaves[j]);

            updateParents(list, j, newNodes); 
        }
	}
	
	/**
	 * 
	 * @param list
	 * @param j
	 * @param newNodes
	 */
	private void updateParents(ArrayList<Leaf> list, int j, Node[] newNodes) {
	      ArrayList<Node> parents = list.get(j).getParents();
          for (int k = 0; k < parents.size(); k++) {
              Node parent = parents.get(k);
              if (parent.getLeft() == list.get(j)) {
                  parent.setLeft(newNodes[j]);
              } else {
                  parent.setRight(newNodes[j]);
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