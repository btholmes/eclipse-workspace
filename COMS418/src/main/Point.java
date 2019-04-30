package main;

public class Point implements Comparable<Point> {

	private String name; 
    private double x;
    private double y;
    private String edge; 

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEdge() {
		return edge;
	}

	public void setEdge(String edge) {
		this.edge = edge;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
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

    @Override
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
