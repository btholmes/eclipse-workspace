package practice;

import java.util.Arrays;

public class hackerrank {

	public static void main(String[] args) {
		int[] scores = {100, 100 , 50 , 40 , 40 , 20 , 10}; 
		System.out.println(binarySearch(scores, 0, scores.length, 10)); 
		
	}
	
	public static int binarySearch(int[] array, int first, int last, int key) {
		int result = 0; 
		
		int mid = (first+last)/2; 
		while(first < last) {
			if(array[mid] > key) {
				first = mid+1; 
			}else if(array[mid] < key) {
				last = mid-1; 
			}else if(array[mid] == key) {
				return mid; 
			}else if(array[mid] > key && array[mid+1] < key) {
				return mid; 
			}
			mid = (first+last)/2; 
		}
		
		return result; 
	}
	
}