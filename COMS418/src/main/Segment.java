package main;


/**
 * Segment class
 * @author aguestuser
 *
 */
public class Segment{
	private String name; 
	private String origin; 
	private String twin; 
	private String face; 
	private String next; 
	private String prev; 
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
	
	
	
	public String getOrigin() {
		return origin;
	}



	public void setOrigin(String origin) {
		this.origin = origin;
	}



	public String getTwin() {
		return twin;
	}



	public void setTwin(String twin) {
		this.twin = twin;
	}



	public String getFace() {
		return face;
	}



	public void setFace(String face) {
		this.face = face;
	}



	public String getNext() {
		return next;
	}



	public void setNext(String next) {
		this.next = next;
	}



	public String getPrev() {
		return prev;
	}



	public void setPrev(String prev) {
		this.prev = prev;
	}



	public String getName() {
		return name;
	}



	public void setP1(Point p1) {
		this.p1 = p1;
	}



	public void setQ1(Point q1) {
		this.q1 = q1;
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

    public double getMinY() {
        return Math.min(p1.getY(), q1.getY());
    }

    public double getMaxY() {
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
    public Point intersectionPoint(double x) {
        if (p1.getX() != q1.getX()) {        	
        	double Px = x; 
        	double Ax = p1.getX(); 
        	double Ay = p1.getY(); 
        	
        	double Bx = q1.getX(); 
        	double By = q1.getY(); 
        	
        	double sum = ((Px - Ax)) * By + ((Bx - Px)) * Ay; 
            double yval = (sum * 1.0) / (Bx - Ax);
            return new Point(x, yval);
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
    	double x = point.getX(); 
    	double y = point.getY(); 
        boolean answer = ((x - this.getP1().getX()) * this.getQ1().getY() + (this.getQ1().getX() - x) * this.getP1().getY()) < (y * (this.getQ1().getX() - this.getP1().getX()));

        double Px = point.getX();
        double Py = point.getY();
        
        double x1 = this.getP1().getX(); 
        double y1 = this.getP1().getY(); 
        
        double x2 = this.getQ1().getX(); 
        double y2 = this.getQ1().getY(); 
        
        double v1x = x2-x1; 
        double v1y = y2-y1; 
        
        double v2x = x2-Px; 
        double v2y = y2-Py; 
        
        double xp = v1x*v2y - v1y*v2x; 
        
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
        
        double above = v1x*v2y - v1y*v2x; 
        
        if(above > 0) {
//        	if(xp > 0 != answer) System.out.println("Wrong answer +");
        	return answer; 
        }else {
//        	if(xp < 0 != answer) System.out.println("Wrong answer -");
        	return answer; 
        }
    }
    
    public boolean liesOn(Segment seg) {
    	boolean result = false; 
    	if((this.p1.getX() == seg.getP1().getX() && this.q1.getX() == seg.getQ1().getX()) ||
    			(this.p1.getY() == seg.getP1().getY() && this.q1.getY() == seg.getQ1().getY())) {
    		result = true; 
    	}
    	return result; 
    }
    
    private double distance(Point a, Point b) {
    	return Math.sqrt((a.getX() - b.getX())*(a.getX() - b.getX()) + (a.getY() - b.getY())*(a.getY() - b.getY())); 
    }
    
    
    public boolean liesOn(Point point) {
    	return distance(this.getP1(), point) + distance(point,this.getQ1()) == distance(this.getP1(), this.getQ1());     	
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