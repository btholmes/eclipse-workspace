package main;

import java.util.ArrayList;

/**
 * 
 * @author aguestuser
 *
 */
public class Face {

	private String name; 
	private String outerEdge; 
	private ArrayList<String> innerEdges; 
	
	public Face(String name) {
		this.name = name; 
		innerEdges = new ArrayList<String>(); 
	}
	
	public String getName() {
		return this.name; 
	}
	
	public void setName(String name) {
		this.name = name; 
	}

	public String getOuterEdge() {
		return outerEdge;
	}

	public void setOuterEdge(String outerEdge) {
		this.outerEdge = outerEdge;
	}

	public void addInnerEdge(String edge) {
		innerEdges.add(edge); 
	}
	
	public ArrayList<String> getInnerEdges() {
		return innerEdges;
	}

	public void setInnerEdges(ArrayList<String> innerEdges) {
		this.innerEdges = innerEdges;
	}
	
	
	
}
