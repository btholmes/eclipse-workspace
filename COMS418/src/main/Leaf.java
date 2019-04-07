package main;

public class Leaf extends Node{
    private Trapezoid data;
    
    public Leaf(Trapezoid t) {
        super();
        data = t;
    }
    
    public Trapezoid getData() {
        return data;
    }
}
