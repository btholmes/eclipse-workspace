package main;


/**
 * Segment class
 * @author aguestuser
 *
 */
public class Segment{
	private String name; 
	private Point p1; 
	private Point q1; 

	public Segment(Point one, Point two) {
		   if (one.compareTo(two) <= 0) {
	            p1 = one;
	            q1 = two;
	        } else {
	            p1 = two;
	            q1 = one;
	        }
	}
	
	public void setName(String name) {
		this.name = name; 
	}
	
    public Point getP1() {
        return p1;
    }

    public Point getQ1() {
        return q1;
    }

    public int getMinY() {
        return Math.min(p1.getY(), q1.getY());
    }

    public int getMaxY() {
        return Math.max(p1.getY(), q1.getY());
    }

    /**
     * Returns the point on the segment at the given x value or p1 if vertical
     * 
     * sum = (Px-Ax)By + (Bx-Px)Ay
     * y = sum/(Bx-Ax)
     * 
     * @param 
     * @return
     */
    public Point intersectionPoint(int x) {
        if (p1.getX() != q1.getX()) {        	
        	int Px = x; 
        	long Ax = (long)p1.getX(); 
        	long Ay = (long)p1.getY(); 
        	
        	long Bx = (long)q1.getX(); 
        	long By = (long)q1.getY(); 
        	
            long sum = ((long) (Px - Ax)) * By + ((long) (Bx - Px)) * Ay; 
            double yval = (sum * 1.0) / (Bx - Ax);
            return new Point(x, (int) yval);
        } else {
        	/**
        	 * Is a vertical segment, so return p1
        	 */
            return new Point(p1.getX(), p1.getY());
        }
    }
    
    /**
     * Determines if the given point is on or above the segment using cross products for AB and AP
     * 
     * v1 = {x2-x1, y2-y1}   # Vector 1 
     * v2 = {x2-Px, y2-Py}   # Vector 1
     * 
     * xp = v1x*v2y - v1y*v2x  # Cross product
     * 
     * Otherwise we need to find the sign and compare it to the sign of a point we know is above the line
     * 
     * @param 
     * @param 
     * @return
     */
    public boolean isPointAboveSeg(Point point) {
    	int x = point.getX(); 
    	int y = point.getY(); 
        boolean answer = ((x - this.getP1().getX()) * this.getQ1().getY() + (this.getQ1().getX() - x) * this.getP1().getY()) < (y * (this.getQ1().getX() - this.getP1().getX()));

        int Px = point.getX();
        int Py = point.getY();
        
        int x1 = this.getP1().getX(); 
        int y1 = this.getP1().getY(); 
        
        int x2 = this.getQ1().getX(); 
        int y2 = this.getQ1().getY(); 
        
        int v1x = x2-x1; 
        int v1y = y2-y1; 
        
        int v2x = x2-Px; 
        int v2y = y2-Py; 
        
        int xp = v1x*v2y - v1y*v2x; 
        
        /**
         * Means this point lies on the segment
         */
        if(xp == 0) {
//        	if(answer != true) System.out.println("Wrong answer = 0"); 
        	return answer; 
        }
        
        /**
         * We need to determine what side of the line the point lies on. We know that (x1,y1+1) lies above the line, so we calculate the xp again using that as 
         * our new Px and Py, and we will compare that sign to the answer. If they have the same sign, the point lies above the line, otherwise it lies below. 
         */    
        Px = x1; 
        Py = y1+1; 
        
        v2x = x2-Px;
        v2y = y2-Py; 
        
        int above = v1x*v2y - v1y*v2x; 
        
        if(above > 0) {
//        	if(xp > 0 != answer) System.out.println("Wrong answer +");
        	return answer; 
        }else {
//        	if(xp < 0 != answer) System.out.println("Wrong answer -");
        	return answer; 
        }
    }
    
    @Override
    public boolean equals(Object s) {
        if (!(s instanceof Segment) || s == null) {
            return false;
        }
        Segment ss = (Segment) s;
        return ss.p1.equals(this.p1) && ss.q1.equals(this.q1);
    }

    @Override
    public String toString() {
        return "(" + p1 + " , " + q1 + ")";
    }
	
}