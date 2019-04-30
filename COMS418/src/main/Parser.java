package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * 
 * @author aguestuser
 *
 */
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
			  
			  HashMap<String, String> pointLookUp = constructor.getPointLookUp(); 
			  HashMap<String, Segment> segmentLookUp = constructor.getSegmentMap(); 
			  
			  drawStruct(list, constructor.getMaxX()-constructor.getMinX(), constructor.getMaxY() - constructor.getMinY()); 
			  printFile(constructor, list); 
			  
			  
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
				  trim = trim.replaceAll(" +", ""); 
				  String[] parse = trim.split(","); 
				  Point point = new Point(Double.parseDouble(parse[0]), Double.parseDouble(parse[1])); 
				  System.out.println("Trapezoid containing the point " + point + ":" );
				  
				  if(pointLookUp.containsKey(point.toString())) {
					  System.out.println(point.toString());
				  }else {
					  Trapezoid zoid = constructor.query(point); 
					  Segment[] bounds = zoid.getBoundary(); 	
					  /**
					   * Determine if point lies on edge of zoid
					   */
					  boolean found = false; 
					  for(Segment bound : bounds) {
						  if(bound.liesOn(point)) {
							  found = true; 
							  System.out.println(bound);
						  }
						  if(found) break; 
					  }
					  if(!found) {
						  /**
						   * if not on edge, just print out bounds
						   */
						  System.out.println(bounds[1]);
						  System.out.println(bounds[3]);
						  System.out.println(bounds[0].getP1());
						  System.out.println(bounds[2].getQ1());
					  }
				  }
				 
			  }
			  i++;   
		  }  
	}
	
	/**
	 * File won't include trapezoids that have 0 width, but they will include trapezoids with 0 height
	 * @param constructor
	 * @param list
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static void printFile(StructureConstructor constructor, ArrayList<Trapezoid> list) throws FileNotFoundException, UnsupportedEncodingException {
		LinkedHashMap<String, Set<Trapezoid>> faces = new LinkedHashMap<>(); 
		PrintWriter writer = new PrintWriter("trapezoidalMap.txt", "UTF-8");
		HashMap<String, String> pointLookUp = constructor.getPointLookUp(); 
		HashMap<String, Segment> segmentLookUp = constructor.getSegmentMap(); 
		
		writer.println("****** Trapezoidal Map******");
		writer.println();
		
		HashSet<Trapezoid> set = new HashSet<>(); 
		for(Trapezoid trap : list) {
			Segment[] bounds = trap.getBoundary(); 
			Point leftP = bounds[0].getP1(); 
			Point rightP = bounds[2].getQ1(); 
			Segment top = trap.getTopSeg();
			Segment bottom = trap.getBottomSeg(); 
			
			Trapezoid zoid = new Trapezoid(leftP, rightP, top, bottom); 
			bounds = zoid.getBoundary(); 
			
			Segment left = bounds[0]; 
			Segment right = bounds[2]; 		
			
			if(top.equals(bottom) || left.equals(right)) continue; 

			
			String face = ""; 
			if(top.equals(constructor.getT()) || bottom.equals(constructor.getB()) || left.equals(constructor.getL()) || right.equals(constructor.getR())) {
				face = constructor.getUnboundedFace().getName(); 
			}else {
				if(!top.equals(constructor.getT())) {
					Point q1 = top.getQ1(); 
					Point p1 = top.getP1(); 
					String segName = "e" + pointLookUp.get(q1.toString()).substring(1).trim() + "," + pointLookUp.get(p1.toString()).substring(1).trim(); 
					Segment seg = segmentLookUp.get(segName); 
					face = seg.getFace(); 
				}else if(!bottom.equals(constructor.getB())) {
					Point p1 = bottom.getP1(); 
					Point q1 = bottom.getQ1(); 
					String segName = "e" + pointLookUp.get(p1.toString()).substring(1).trim() + "," + pointLookUp.get(q1.toString()).substring(1).trim(); 
					Segment seg = segmentLookUp.get(segName); 
					face = seg.getFace(); 
				}
			}
		
			faces.putIfAbsent(face, new HashSet<Trapezoid>()); 
			faces.get(face).add(zoid); 
		}
		
		
		Iterator it = faces.entrySet().iterator(); 
		while(it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			String face = (String)pair.getKey(); 
			HashSet<Trapezoid> faceSet = (HashSet)pair.getValue(); 

			writer.println("Face " + face + " contains " +  faceSet.size() + " trapezoids: " +  "\n");
			
			Iterator setIt = faceSet.iterator(); 
			while(setIt.hasNext()) {
				Trapezoid zoid = (Trapezoid)setIt.next(); 
				Segment[] bounds = zoid.getBoundary(); 
				
				Segment left = bounds[0]; 
				Segment top = zoid.getTopSeg(); 
				Segment right = bounds[2]; 
				Segment bottom = zoid.getBottomSeg(); 
						
				
				if(top.equals(constructor.getT())){
					writer.println("T");
				}else {
					writer.println(top.toString());
				}
				if(bottom.equals(constructor.getB())) {
					writer.println("B");
				}else {
					writer.println(bottom.toString());
				}
				if(left.equals(constructor.getL())) {
					writer.println("L");
				}else {
					writer.println(left.getP1().toString());
				}
				if(right.equals(constructor.getR())) {
					writer.println("R");
				}else {
					writer.println(right.getQ1().toString()); 
				}
				writer.println();
			}	
		}
		
		writer.flush();
		writer.close();
	}
	
	public static void drawStruct(ArrayList<Trapezoid> trapezoids, double width, double height) {
		  Drawing draw = new Drawing(); 
		  width = (width+2)*100; 
		  height = (height+2)*100; 
		  draw.create((int)width, (int)height, trapezoids);
		  
	}
	
}; 