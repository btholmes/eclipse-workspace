package main;

/**
 * 
 * @author aguestuser
 *
 */
public class XNode extends Node {
    private Point data;
    
    public XNode(Point p) {
        super();
        data = p;
    }
    
    public Point getData() {
        return data;
    }
}