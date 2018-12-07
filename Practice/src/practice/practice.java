package practice;

import java.util.ArrayList;
import java.util.Arrays;

public class practice {

	public static void main(String[] args) {
		int[] a = new int[] {0, 2,4,-1,5,0, -1}; 
		int pivot = findPivot(a, 0, a.length -1); 
//		System.out.println(pivot);
//		int key = 5; 
//		if(key < a[0])
//			System.out.println(binarySearch(a, 0, pivot+1, key));
//		else
//			System.out.print(binarySearch(a, pivot+1, a.length-1, key));
		
		System.out.println(maxProduct(a));
	}
	
	
	public static int findPivot(int[] A, int low, int high) {
		if(low > high) return -1; 
		if(low == high) return low; 
	
		int mid = low + (high-low)/2; 
		if(mid < high && A[mid] > A[mid+1]) {
			return mid; 
		}
		if(mid > low && A[mid] < A[mid-1]) {
			return mid-1; 
		}
		
		if(A[low] >= A[mid])
			return findPivot(A, low, mid-1); 
		
		return findPivot(A, mid+1, high);	
	}
	
	public static int binarySearch(int[] A, int low, int high, int key) {
		if(low > high) return -1; 
		if(low == high) return low; 
		
		int mid = low + (high-low)/2; 
		if(A[mid] == key) return mid; 
		
		if(key < A[mid])
			return binarySearch(A, low, mid-1, key); 
		return binarySearch(A, mid+1, high, key); 
			
	}
	
    static int r; 
    public static int maxProduct(int[] A) {

        r = A[0];

        maxProductHelper(A, r, 1); 
        return r; 
    }
    
    public static void maxProductHelper(int[] A, int product, int i){
        if(i >= A.length) return; 
        
        int imax = Math.max(A[i], A[i] * product); 
        r = Math.max(imax, r); 
        
        maxProductHelper(A, A[i], i+1);  
        
        product *= A[i]; 
        maxProductHelper(A, product, i+1); 
    }
	
}
