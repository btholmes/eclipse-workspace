package practice;

import java.util.Random;

public class quicksort {

	public static void main(String[] args) {
		int[] array = new int[] {17, 87, 6,0,100,99,-1, 22, 41,3,13,54}; 
		quickSort(array, 0, array.length-1); 
		
		for(int i = 0; i < array.length; i++) {
			System.out.print(array[i] + " ");
		}
	}	
	
	public static void quickSort(int[] arr, int left, int right) {
		if(left >= right) return; 
		int index = pivot(arr, left, right); 
		quickSort(arr, left, index-1); 
		quickSort(arr, index+1, right); 
	}
	
	
	
	public static int pivot(int[] arr, int left, int right) {
		int pivot = arr[right]; 
		int pIndex = left; 
		
		for(int i = left; i < right; i++) {
			if(arr[i] <= pivot) {
				swap(arr, i, pIndex); 
				pIndex++; 
			}
		}
		swap(arr, pIndex, right); 
		return pIndex; 
	}
	
	
	
	
	
	
	
//	public static int pivot(int[] arr, int left, int right) {
//		int pivot = arr[right]; 
//		int pIndex = left; 
//		
//		for(int i = pIndex; i < right; i++) {
//			if(arr[i] <= pivot) {
//				swap(arr, i, pIndex); 
//				pIndex++;
//			}
//		}
//		swap(arr, pIndex, right); 
//		return pIndex; 
//	}
	
	public static void swap(int[] arr, int a, int b) {
		int temp = arr[a]; 
		arr[a] = arr[b]; 
		arr[b] = temp; 
	}
}
