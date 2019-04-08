package main;


public class YNode extends Node{
    private Segment data;
    
    public YNode(Segment s) {
        super();
        data = s;
    }
    
    public Segment getData() {
        return data;
    }
}