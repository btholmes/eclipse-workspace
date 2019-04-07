package main;



public class Segment{
	Point p1; 
	Point q1; 

	public Segment(Point one, Point two) {
		   if (one.compareTo(two) <= 0) {
	            p1 = one;
	            q1 = two;
	        } else {
	            p1 = two;
	            q1 = one;
	        }
	}
	
	
    public Point getLeftEndPoint() {
        return p1;
    }

    public Point getRightEndPoint() {
        return q1;
    }

    public int getMinX() {
        return p1.getX();
    }

    public int getMaxX() {
        return q1.getX();
    }

    public int getMinY() {
        return Math.min(p1.getY(), q1.getY());
    }

    public int getMaxY() {
        return Math.max(p1.getY(), q1.getY());
    }

//    public Line2D.Double getline() {
//        return l;
//    }

    @Override
    public boolean equals(Object s) {
        if (!(s instanceof Segment) || s == null) {
            return false;
        }
        Segment ss = (Segment) s;
        return ss.p1.equals(this.p1) && ss.q1.equals(this.q1);
    }

    /**
     * Returns the point on the segment at the given x value or the lower
     * endpoint if the segment is vertical. The behavior for vertical segments
     * may change later
     *
     * @param x The x-value to intersect the line at
     * @return The point on the line (segment) at the given x-value
     */
    public Point intersect(int x) {
        if (p1.getX() != q1.getX()) {

            long ysum = ((long) (x - p1.getX())) * ((long) q1.getY()) + ((long) (q1.getX() - x)) * ((long) p1.getY());
            double yval = (ysum * 1.0) / (q1.getX() - p1.getX());
            return new Point(x, (int) yval);
        } else {
            return new Point(p1.getX(), p1.getY());
        }
    }

    private double getSlope() {
        if (isVertical()) {
            return 0;
        }
        return (q1.getY() - p1.getY()) / ((double) (q1.getX() - p1.getX()));
    }


    private boolean isVertical() {
        return (q1.getX() == p1.getX());
    }

    /**
     * Checks to see if this segment object crosses another properly (not a
     * shared endpoint)
     *
     * @param other The other segment to check against
     * @return True if the segments intersect at a point which is not a common
     * vertex
     */
    public boolean crosses(Segment other) {
        //check if x-ranges overlap
        if (other.p1.getX() > this.q1.getX()) {
            return false;
        }
        if (other.q1.getX() < this.p1.getX()) {
            return false;
        }

        //at this point, the x-ranges overlap
        if (this.isVertical() && other.isVertical()) {//they must lie vertically aligned
            if (this.getMaxY() <= other.getMinY() || this.getMinY() >= other.getMaxY()) {
                return false;
            }
            return true;
        } else if (this.isVertical()) {
            Point p = other.intersect(this.p1.getX());
            return (p.getY() > this.getMinY()) && (p.getY() < this.getMaxY());
        } else {//neither is a vertical line
            //we use a bounding box technique instead of directly computing the intersection
            //it is quite possible we aren't saving any time with this strategy

            //must find the intersection points
            double slope1 = this.getSlope();
            double slope2 = other.getSlope();
            //use slope1 to calculate 3 b's, same for slope2
            double b00 = this.p1.getY() - this.p1.getX() * slope1;
            double b01 = other.p1.getY() - other.p1.getX() * slope1;
            double b02 = other.q1.getY() - other.q1.getX() * slope1;

            double b10 = other.p1.getY() - other.p1.getX() * slope2;
            double b11 = this.p1.getY() - this.p1.getX() * slope2;
            double b12 = this.q1.getY() - this.q1.getX() * slope2;
            if (((b01 <= b00 && b00 <= b02) || (b01 >= b00 && b00 >= b02)) && ((b11 <= b10 && b10 <= b12) || b11 >= b10 && b10 >= b12)) {
                return this.equals(other) || !(this.p1.equals(other.p1) || this.p1.equals(other.q1) || this.q1.equals(other.p1) || this.q1.equals(other.q1));

            }
        }

        return false;
    }

    @Override
    public String toString() {
        return "(" + p1 + " , " + q1 + ")";
    }
	
}