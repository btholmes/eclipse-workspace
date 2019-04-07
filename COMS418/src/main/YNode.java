package main;


public class YNode extends Node{
    private Segment data;
    
    public YNode(Segment s) {
        super();
        data = s;
    }
    
    /**
     * Return the segment data held by this Node
     * @return the segment data
     */
    public Segment getData() {
        return data;
    }
}