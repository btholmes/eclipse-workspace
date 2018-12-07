package practice;

import java.util.Arrays;
import java.util.Comparator;

public class Anagram {

	public static void main(String[] args) {
		String[] arr = new String[] {"ANA", "CBC", "AGG", "NAA", "GGA", "BCC"}; 
		Arrays.sort(arr, new AnagramComparator());
		for(int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
	}
	
	
	public static class AnagramComparator implements Comparator<String>{

		public String sort(String s) {
			char[] arr = s.toCharArray(); 
			Arrays.sort(arr);
			return new String(arr); 
		}
		
		@Override
		public int compare(String o1, String o2) {
			// TODO Auto-generated method stub
			return sort(o1).compareTo(sort(o2));
		}
		
	}
}
