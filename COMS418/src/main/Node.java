package main;

import java.util.ArrayList;


public abstract class Node {
    private ArrayList<Node> parents;
    private Node parent = null;
    private Node left = null;
    private Node right = null;
    
    public Node() {
        parents = new ArrayList<Node>(1);
    }
    
    public ArrayList<Node> getParents() {
        return parents;
    }
    
    public Node getParent() {
        return parent;
    }
    
    public void setParent(Node parent) {
        this.parent = parent;
        this.parents.add(parent);
    }
    
    public Node getLeft() {
        return left;
    }
    
    public void setLeft(Node left) {
        this.left = left;
        this.left.setParent(this);
    }
    
    public Node getRight() {
        return right;
    }
    
    public void setRight(Node right) {
        this.right = right;
        this.right.setParent(this);
    }
}