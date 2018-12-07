package InterviewPrep;

import java.util.Collections;

public class Prep {

	public static void main(String[] args) {
		int[] arr = new int[] {100, 39,34,65,3,5,754,75,2,5,64,4,4}; 
//		int[] result = countSort(arr, 754); 
//		quickSort(arr, 0, arr.length); 
		
		int a = 7;  
		testArr(a); 
		System.out.println(a + " "); 
//		print(a); 
		
	}
	
	public static void testArr(int arr) {
		arr = 999; 
	}
	
	private static void quickSort(int[] arr, int left, int right) {
			int pivot = partition(arr, left, right); 
			if(left < pivot -1)
				quickSort(arr, left, pivot-1); 
			if(pivot < right)
				quickSort(arr, pivot, right); 
	}
	
	private static int partition(int[] arr, int left, int right) {
		int pivot = arr[(left + right)/2]; 
		while(arr[left] < pivot) left++; 
		
		while(arr[right] >= pivot) right--; 
		
		if(left <= right) {
			arr = swap(arr, left, right); 
			left++; 
			right--; 
		}
		
		return left; 
	}
	
	private static int[] swap(int[] arr, int i, int j) {
		int temp = arr[i]; 
		arr[i] = arr[j]; 
		arr[j] = temp; 
		return arr; 
	}
	
	/**
	 * Time is O(n + k) n is number of elements in input, and k is range of the inputs inputs. 
	 * If range is sufficiently small, this method is quicker than Comparison based sorting. 
	 * @param arr
	 * @param range
	 * @return
	 */
	public static int[] countSort(int[] arr, int range) {
		if(arr == null || arr.length == 0 || arr.length == 1) return arr;  
		
		int[] result = new int[arr.length]; 
		
		int[] countArr = new int[range + 1]; 
		for(int i = 0; i < arr.length; i++) {
			countArr[arr[i]] += 1; 
		}
		for(int i = 1; i < countArr.length; i++) {
			countArr[i] = countArr[i-1] + countArr[i]; 
		}
		for(int i = 0; i < arr.length; i++) {
			int index = countArr[arr[i]]; 
			result[index-1] = arr[i];  
			countArr[arr[i]]--; 
		}
	
		return result; 
	}
	
	/**
	 * Time is O(nk) where k is the number of bits, or digits
	 * @param arr
	 * @return
	 */
	public static int[] radixSort(int[] arr) {
		int[] result = new int[arr.length]; 
		
		return result; 
	}
	
	public static void print(int[] arr) {
		for(int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
	}
}
