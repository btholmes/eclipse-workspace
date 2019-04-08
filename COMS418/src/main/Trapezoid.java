package main;


public final class Trapezoid {

    private Trapezoid uLeftNeighbor;
    private Trapezoid lLeftNeighbor;
    private Trapezoid uRightNeighbor;
    private Trapezoid lRightNeighbor;
    private Leaf leaf;

    private Point leftP;
    private Point rightP;
    private Segment topSeg;
    private Segment bottomSeg;

    
    public Trapezoid(Point left, Point right, Segment top, Segment bottom) {
        leftP = left;
        rightP = right;
        topSeg = top;
        bottomSeg = bottom;

        uLeftNeighbor = null;
        lLeftNeighbor = null;
        uRightNeighbor = null;
        lRightNeighbor = null;
        leaf = null;
    }
    
    public Trapezoid getuLeftNeighbor() {
        return uLeftNeighbor;
    }
    
    public void setuLeftNeighbor(Trapezoid t) {
        uLeftNeighbor = t;
    }
    
    public Trapezoid getlLeftNeighbor() {
        return lLeftNeighbor;
    }
    
    public void setlLeftNeighbor(Trapezoid t) {
        lLeftNeighbor = t;
    }
    
    public Trapezoid getuRightNeighbor() {
        return uRightNeighbor;
    }
    
    public void setuRightNeighbor(Trapezoid t) {
        uRightNeighbor = t;
    }
    
    public Trapezoid getlRightNeighbor() {
        return lRightNeighbor;
    }
    
    public void setlRightNeighbor(Trapezoid t) {
        lRightNeighbor = t;
    }
 
    public Leaf getLeaf() {
        return leaf;
    }
    
    public void setLeaf(Leaf l) {
        leaf = l;
    }
    
    public Point getLeftP() {
        return leftP;
    }

    public Point getRightP() {
        return rightP;
    }

    public Segment getTopSeg() {
        return topSeg;
    }
    
    public Segment getBottomSeg() {
        return bottomSeg;
    }

    public boolean equals(Object t) {
        if (t == null || !(t instanceof Trapezoid)) {
            return false;
        }
        Trapezoid tt = (Trapezoid) t;
        return (this.topSeg == tt.topSeg) && (this.bottomSeg == tt.bottomSeg);
    }
    
    /**
     * Gets the four segments which bound this trapezoid
     * 
     * @return this trapezoid's boundary in order left, top, right bottom
     */
    public Segment[] getBoundary() {
        Point tl = topSeg.intersectionPoint(leftP.getX());
        Point tr = topSeg.intersectionPoint(rightP.getX());
        Point bl = bottomSeg.intersectionPoint(leftP.getX());
        Point br = bottomSeg.intersectionPoint(rightP.getX());
        Segment[] segs = {new Segment(bl, tl), new Segment(tl, tr), new Segment(tr, br), new Segment(br, bl)};

        return segs;
    }
    
    public boolean hasWidth() {
        return !leftP.equals(rightP);
    }

}
