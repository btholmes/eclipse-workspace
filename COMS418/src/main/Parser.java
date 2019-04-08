package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

class Parser{
	
	public static void main(String[] args) throws IOException {
		  StructureConstructor constructor; 
		  String fileName = ""; 
		  Scanner in = new Scanner(System.in); 
		  int i = 1; 
		  int order = 0; 
		  
		  System.out.println("Point location using Trapezoidal Maps\n");
		  
		  while(true) {
			  System.out.println("Trial " + i + ":");
			  System.out.println("Name of input segment file: ");
			  fileName = in.next(); 
			  if (fileName.equals("x")) {
				  System.out.println("Program ended");
				  break; 
			  }
			  			  
			  System.out.println("Segment order 0 (original) 1 (random): "); 
			  order = in.nextInt(); 
			  
			  /**
			   * Construct map from file
			   */
			  constructor = new StructureConstructor(fileName, order); 
			  Set<Trapezoid> trapezoids = constructor.getTrapezoids(); 
			  ArrayList<Trapezoid> list = new ArrayList<>(trapezoids); 
			  
			  drawStruct(list); 
			  
			  System.out.println("Trapezoidal map construction completed\n");
			  
			  while(true) {
				  System.out.println("Query point: ");
				  String val = in.next(); 
				  boolean endQueries = false; 
				  if(val.equals("x")) {
					  System.out.println("End of all queries in this map. \n");
					  endQueries = true; 
				  }
				  if(endQueries) break; 
				  
				  String trim = val.substring(1, val.length()-1); 
				  String[] parse = trim.split(","); 
				  Point point = new Point(Integer.parseInt(parse[0]), Integer.parseInt(parse[1])); 
				  System.out.println("Trapezoid containing the point " + point + ":" );
				  
				  Trapezoid zoid = constructor.query(point); 
				  Segment top = zoid.getTopSeg(); 
		    		Segment bottom = zoid.getBottomSeg(); 
		    		Point left = zoid.getLeftP(); 
		    		Point right = zoid.getRightP(); 
		    		
		    		System.out.println(top);
		    		System.out.println(bottom);
		    		System.out.println(left);
		    		System.out.println(right);
				  /**
				   * Print the DCEL 
				   */
			  }

			  i++; 
			  
		  }
		  
	}
	
	public static void drawStruct(ArrayList<Trapezoid> trapezoids) {
		  Drawing draw = new Drawing(); 
		  draw.create(500,500, trapezoids);
		  
	}
	
}; 