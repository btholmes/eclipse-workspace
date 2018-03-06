package hw5;

import java.util.Random;

public class problem3 {
	static int[][] m; 
	static int rows; 
	static int columns; 
	
	public static void main(String[] args) {
		rows = 5; 
		columns = 4; 
	
//		boolean result = false; 
//		while(!result) {
//			generateGraph(); 
//			result = Solve(1, 1); 	
//		}

		generateGraph(); 
		printGraph(); 
		System.out.println(getCost(0,columns-1, 0, 0,0));
//		
//		printGraph(); 
	}
	
	public static void generateGraph() {
		m = new int[rows][columns]; 
		Random rand = new Random(); 
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < columns; j++) {
				int max = 7; 
				int min = 1; 
				m[i][j] = rand.nextInt((max - min)+1) + min; 
//				m[i][j] = 7; 
			}
		}
	}
	
	public static void printGraph() {
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < columns; j++) {
				System.out.print(m[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public static int getCost(int i, int j, int a, int b, int c) {
		int result = 0; 
		if(j == 0)
			return m[i][j]; 
//		if(i == rows-1)
//			return m[i][j]; 
//		if(i == rows-1 && j == 0)
//			return m[i][j]; 
	
		
		if(valid((i-1) , j)) {
			getCost(i-1,j, a+m[i][j] , 0, 0); 
		}
//		if(valid(i, j-1))
//			getCost(i, j-1, 0, a+m[i][j], 0); 
//		if(valid(i-1, j-1))
//			getCost(i-1, j-1, 0, 0, a + m[i][j]); 
//		
//		int t = Math.max(a, b); 
//		result = Math.max(t, c); 
		
		return a; 
	}
	
	public static boolean isTarget(int x, int y) {
		return x == 0 && y == 0; 
	}
	
	public static boolean valid(int x, int y) {
		return x < rows && x > 0 && y > 0 && y < columns; 
	}
	

	static boolean Solve(int x,int y) {

		   if(isTarget(x,y)) 
		       return(true);

		   if(!valid(x,y)) return false; 
		   
		   if(valid(x+1,y)&&m[x+1][y]== 7) {
		      m[x][y] = 0; 
		      if(Solve(x+1,y))
		         return(true); 
		      else 
		    	  	m[x][y] = 7;
		   }
		   if(valid(x-1,y)&&m[x-1][y]== 7) {
		      m[x][y] = 0;
		      if(Solve(x-1,y))
		         return(true); 
		      else 
		    	  	m[x][y] = 7;
		   }
		   if(valid(x,y+1)&&m[x][y+1]==7) {
		      m[x][y] = 0;
		      if(Solve(x,y+1))
		         return(true); 
		      else 
		    	  	m[x][y] = 7; 
		   }
		   if(valid(x,y-1)&&m[x][y-1]==7) {
		      m[x][y] = 0; 
		      if(Solve(x,y-1))
		         return(true); 
		      else
		    	  	m[x][y] = 7; 
		   }
		   if(valid(x+1, y+1) && m[x+1][y+1] == 7) {
			   m[x][y] = 0; 
			   if(Solve(x+1, y+1)) return true; 
			   else
				   m[x][y] = 7; 
		   }
		   if(valid(x-1, y-1) && m[x-1][y-1] == 7) {
			   m[x][y] = 0; 
			   if(Solve(x-1, y-1)) return true; 
			   else
				   m[x][y] = 7; 
		   }
		   if(valid(x+1, y-1) && m[x+1][y-1] == 7) {
			   m[x][y] = 0; 
			   if(Solve(x+1, y-1)) return true; 
			   else 
				   m[x][y] = 7; 
		   }
		   if(valid(x-1, y+1) && m[x-1][y+1] == 7) {
			   m[x][y] = 0; 
			   if(Solve(x-1, y+1)) return true; 
			   else 
				   m[x][y] = 7; 
		   }
		   
		   return(false);
		}
	
}
