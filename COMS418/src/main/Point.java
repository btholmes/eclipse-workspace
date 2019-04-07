package main;

public class Point implements Comparable<Point> {

    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int compareToX(Point p) {
        if (p == null) {
            return 1;
        }
        if (this.x < p.x) {
            return -1;
        }
        if (this.x == p.x) {
            return 0;
        }
        return 1;
    }

    public int compareTo(Point p) {
        if (p == null) {
            return 1;
        }
        if (this.x < p.x || (this.x == p.x && this.y < p.y)) {
            return -1;
        } else if ((this.x == p.x) && (this.y == p.y)) {
            return 0;
        } else {
            return 1;
        }
    }

    public boolean equals(Object p) {
        if (p == null || !(p instanceof Point)) {
            return false;
        }
        Point pp = (Point) p;
        return (this.x == pp.x) && (this.y == pp.y);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
