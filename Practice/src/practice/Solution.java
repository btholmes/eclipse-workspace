package practice;

import java.util.Arrays;

public class Solution {

	public static void main(String[] args) {
		String s = "abcabcbb"; 
		System.out.println(solve(s)); 
	}
	
	public static int solve(String s) {
		int result = 0; 
		int[] letters = new int[256]; 
		int max = 0; 
		for(int i = 0; i < s.length(); i++) {
			char item = s.charAt(i); 
			if(letters[item] == 0) {
				letters[item] = 1; 
				max++; 
				result = Math.max(result,  max); 
			}else {
				letters = new int[256]; 
				max = 0; 
			}
		}
		
		return result; 
	}
}
