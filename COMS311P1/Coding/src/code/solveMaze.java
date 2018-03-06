package code;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class solveMaze {

	static char[] pattern; 
	static char[][] maze = new char[1000][1000]; ; 
	static int[][] path = new int[1000][1000]; 
	static int i; 
	static int j; 
	
	public static void main(String[] args) throws IOException {
//		File input = new File("input.txt"); 
//		FileOutputStream out = new FileOutputStream(input); 
//		out.write(12);
//		out.close(); 
		getInput(); 
		System.out.println(findPath(0,0)); 
		
		
	}
	
	public static boolean findPath(int y, int x) {
		if(y >= i || x >= j || y < 0 || x < 0) return false; 
		if(y == i-1 && x == j-1) {
			path[y][x] = 1; 
			return true; 
		}
		
		
		return true; 
	}
	
	
	public static void getInput() throws FileNotFoundException {
		File input = new File("input.txt"); 
		Scanner in = new Scanner(input); 
		pattern = in.next().toCharArray(); 
		int y = 0; 
		int x = 0;
		while(in.hasNext()) {
			char[] array = in.next().toCharArray(); 
			maze[y] = array; 
			y++; 
			x = array.length; 
		}
		i = y; 
		j = x; 
		path = new int[y][x]; 
		
	}
	
	
}
