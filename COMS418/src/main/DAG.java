package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DAG{
	
	private Node root; 
	
	public DAG() {
		root = null; 
	}
	
	public void setRoot(Node root) {
		this.root = root; 
	}
	
	public Node getRoot() {
		return root; 
	}
	
	public Set<Trapezoid> getTrapezoids() {
		Set<Trapezoid> leaves = new HashSet<Trapezoid>(); 
		helper(root, leaves); 
		return leaves; 
	}
	
	/**
	 * Helper function to get all leaves of the DAG
	 * @param current
	 * @param leaves
	 */
	private void helper(Node current, Set<Trapezoid> leaves) {
		if(current.getLeft() == null && current.getRight() == null) {
			leaves.add(((Leaf)current).getData()); 
		}
		if(current.getLeft() != null)
			helper(current.getLeft(), leaves); 
		
		if(current.getRight() != null) 
			helper(current.getRight(), leaves); 
	}
	
	
	/**
	 * Follows algorithm as described in book
	 * 
	 * @param 
	 * @return
	 */
    public ArrayList<Leaf> followSegment(Segment s) {
        ArrayList<Leaf> list = new ArrayList<Leaf>();
        Leaf current = searchPointInDAG(s.getP1(), s);
        list.add(current);
        
        /**
         * Keep following the segment until we reach a trapezoid which contains the segments right endpoint
         */
        while (current != null && s.getQ1().compareTo(current.getData().getRightP()) > 0) {

        	if (s.isPointAboveSeg(current.getData().getRightP())) {
        		current = current.getData().getlRightNeighbor().getLeaf();
            } else {
            	if(current.getData().getuRightNeighbor() != null)
            		current = current.getData().getuRightNeighbor().getLeaf();
            	else
            		current = null; 
            }
            if(current != null)
            	list.add(current);
        }

        return list; 
    }
    
    /**
     * 
     * Search this DAG for the trapezoid containing the given point, the segment 
     * is passed through incase the point we are looking for is on the segment
     * 
     * @param p
     * @param s if s = null, then it's just a normal point query, otherwise
     * @return
     */
    public Leaf searchPointInDAG(Point p, Segment s) {
        Node current = root;
        while (!(current instanceof Leaf)) {
            if (current instanceof XNode) {
                int val = p.compareTo(((XNode) current).getData());

                if (val < 0) {
                    current = current.getLeft();
                } else {
                    current = current.getRight();
                }
            } else {
                /**
                 * Dealing with a Y node
                 */
                if (s == null) {
                	/**
                	 * Just looking for this point as normal, so we need to determine if it lies above or below 
                	 * this given Y node, above = left, below = right
                	 */
                    if (((YNode) current).getData().isPointAboveSeg(p)) {
                        current = current.getLeft();
                    } else {
                        current = current.getRight();
                    }
                } else {
                    /**
                     * This point is on a segment
                     */
                    if (isPointAboveLineDegenerateCase(p, ((YNode) current).getData(), s)) {
                        current = current.getLeft();
                    } else {
                        current = current.getRight();
                    }
                }
            }
        }

        return ((Leaf) current);
    }
    


    /**
     * 
     * @param p
     * @param old
     * @param pseg
     * @return
     */
    public static boolean isPointAboveLineDegenerateCase(Point p, Segment a, Segment b) {

    	if (p.equals(a.getP1())) {
        	/**
        	 * If slope is greater, it's above
        	 */
            double x1 = p.getX();
            double x2 = a.getQ1().getX();
            double x3 = b.getQ1().getX();
            double y1 = p.getY();
            double y2 = a.getQ1().getY();
            double y3 = b.getQ1().getY();
            double result = (x2 - x1) * (y3 - y1) - (x3 - x1) * (y2 - y1);
            return result > 0;
        }

        return a.isPointAboveSeg(p);
    }
}