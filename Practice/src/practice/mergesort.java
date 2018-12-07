package practice;

public class mergesort {

	
	public static void main(String[] args) {
		int[] array = new int[] {100,99,0}; 
		int[] helper = new int[array.length]; 
		mergeSortHelper(array, helper, 0, array.length-1); 
//		for(int i = 0; i < array.length; i++) {
//			System.out.print(array[i] + " ");
//		}
		print(array); 
	}
	
	public static void mergeSortHelper(int[] array, int[] helper, int low, int high) {
		if(low >= high) return; 
		else {
			int middle = (low+high)/2; 
			
			mergeSortHelper(array, helper, low, middle); 
			mergeSortHelper(array, helper, middle+1, high);
			merge(array, helper, low, middle, high); 
		}
		
	}
	
	public static void merge(int[] array, int[] helper, int low, int middle, int high) {
		for(int i = low; i <= high; i++) {
			helper[i] = array[i]; 
		}
		
		int l = low; 
		int r = middle+1; 
		int current = low; 
		
		while(l <= middle && r <= high) {
			if(helper[l] <= helper[r]) {
				array[current] = helper[l]; 
				l++; 
			}else {
				array[current] = helper[r]; 
				r++; 
			}
			current++; 
		}
		
		int restL = middle-l; 
		int restR = high-r; 

		if(restL >= 0)
			for(int i =0; i <= restL; i++) {
				array[current] = helper[l+i]; 
				current++; 
			}
//		if(restR >= 0)
//			for(int i = 0; i <= restL; i++) {
//				array[current] = helper[r+i]; 
//				current++; 
//			}
		
	}
	
	private static void print(int[] array) {
		for(int i = 0; i < array.length; i++) {
			System.out.print(array[i] + " ");
		}
		System.out.println();
	}
}
