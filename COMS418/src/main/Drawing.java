package main;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;


class Drawing extends Canvas{
	
	private JFrame frame; 
	private Canvas canvas; 
	private int width; 
	private int height; 
	private int scale; 
	
	private ArrayList<Trapezoid> trapezoids; 
	
	public Drawing() {
   
	}
	
	public void create(int width, int height, ArrayList<Trapezoid> trapezoids) {
	    frame = new JFrame("Visual Display");
        canvas = new Drawing();
        canvas.setSize(width, height);
        ((Drawing)canvas).setHeight(height);
        ((Drawing)canvas).setWidth(width);
        ((Drawing)canvas).setTrapezoids(trapezoids);
        ((Drawing)canvas).setScale(50); 
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
	}
	
	private void setScale(int scale) {
		this.scale = scale; 
	}
	
	
	public void redraw() {
		frame.getContentPane().removeAll();
		frame.getContentPane().repaint();
	}
	
	public void setHeight(int height) {
		this.height = height; 
	}
	
	public void setWidth(int width) {
		this.width = width; 
	}

	
    public void paint(Graphics g) {
    	Graphics2D g2d = (Graphics2D) g.create();
    	g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    	
    	Iterator<Trapezoid> it = trapezoids.iterator(); 
    	while(it.hasNext()) {
    		Trapezoid zoid = (Trapezoid)it.next(); 
    		Segment[] bounds = zoid.getBoundary(); 
    		for(int i = 0; i < 4; i++) {
    			Segment seg = bounds[i]; 
    			g2d.draw(new Line2D.Double((seg.getP1().getX())*scale, height - (seg.getP1().getY())*scale, (seg.getQ1().getX())*scale, height - (seg.getQ1().getY())*scale));
    		}
    		
    	}
    }

	public void setTrapezoids(ArrayList<Trapezoid> trapezoids) {
		this.trapezoids = trapezoids; 
	}
    
    
};
