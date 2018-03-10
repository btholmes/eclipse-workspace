package SortingAlgoritms;

import java.util.Random;

public class Sorting {
	
	public static void main(String[] args) {
//		• Create a sorted array of size n, run all three algorithms and report run times for
//		n = 3000, 30000 and 300000.
//		• Create a reverse sorted array of size n, run all three algorithms and report run times
//		for n = 3000, 30000 and 300000.
//		• Create a random array of size n, run all three algorithms and report run times for
//		n = 3000, 30000 and 300000.
		
		runForN(20);
//		runForN(30000); 
//		runForN(300000); 
		
		
		
	}
	
	public static void runForN(int n) {
		Random rand = new Random(); 
		int[] sorted = new int[n]; 
		int[] reverseSorted = new int[n]; 
		int[] random = new int[n]; 
		
		for(int i =0; i < n; i++) {
			sorted[i] = i; 
			reverseSorted[n-1-i] = i; 
			random[i] = rand.nextInt(3000); 
		}
		
//		selectionSort(sorted); 
//		selectionSort(reverseSorted); 
//		selectionSort(random); 
//		
//		bubbleSort(sorted); 
//		bubbleSort(reverseSorted); 
//		bubbleSort(random); 
		
//		insertionSort(sorted); 
		insertionSort(reverseSorted); 
//		insertionSort(random); 
		
	}
	
	
	 public static void selectionSort(int[] arr){  
		 Long startTime = System.currentTimeMillis(); 
		 System.out.println("Start time for selection of " + arr.length + " is : " + startTime);
	        for (int i = 0; i < arr.length - 1; i++)  
	        {  
	            int index = i;  
	            for (int j = i + 1; j < arr.length; j++){  
	                if (arr[j] < arr[index]){  
	                    index = j;//searching for lowest index  
	                }  
	            }  
	            int smallerNumber = arr[index];   
	            arr[index] = arr[i];  
	            arr[i] = smallerNumber;  
	        }
			 System.out.println("Total time for selection of " + arr.length + " is : " + (System.currentTimeMillis() - startTime));
			 
			 for(int i = 0; i < arr.length; i++) {
				 System.out.print(arr[i] + ", ");
			 }
			 System.out.println();

	    }
	 
	 
	 public static void bubbleSort(int[] numArray) {
		 Long startTime = System.currentTimeMillis(); 

		 System.out.println("Start time for bubble of " + numArray.length + " is : " + startTime);
		    int n = numArray.length;
		    int temp = 0;

		    for (int i = 0; i < n; i++) {
		        for (int j = 1; j < (n - i); j++) {

		            if (numArray[j - 1] > numArray[j]) {
		                temp = numArray[j - 1];
		                numArray[j - 1] = numArray[j];
		                numArray[j] = temp;
		            }

		        }
		    }
			 System.out.println("Total time for bubble of " + numArray.length + " is : " + (System.currentTimeMillis() - startTime));
			 
			 for(int i = 0; i < numArray.length; i++) {
				 System.out.print(numArray[i] + ", ");
			 }
			 System.out.println();
		    
		}
	 
	 
	  public static int[] insertionSort(int[] a){
			 Long startTime = System.currentTimeMillis(); 
//			 System.out.println("Start time for insertion of " + input.length + " is : " + startTime);
//
//	        int temp;
//	        for (int i = 1; i < input.length; i++) {
//	            for(int j = i ; j > 0 ; j--){
//	                if(input[j] < input[j-1]){
//	                    temp = input[j];
//	                    input[j] = input[j-1];
//	                    input[j-1] = temp;
//	                }
//	            }
//	        }
//			 System.out.println("Total time for insertion of " + input.length + " is : " + (System.currentTimeMillis() - startTime));
//			 for(int i = 0; i < input.length; i++) {
//				 System.out.print(input[i] + ", ");
//			 }
//			 System.out.println();
//	        return input;
			 
				  for (int i = 1; i < a.length; i++){
					  System.out.println("Start of iteration " + i);
					  int j = i;
					  int iteration = 1; 
					  while (j > 0 && a[j-1] > a[j]) {
						  int temp = a[j-1]; 
						  a[j-1] = a[j]; 
						  a[j] = temp; 
						  j = j-1;
						  System.out.println("Iteration while is " + iteration);
						  iteration++; 
					  }
						System.out.println("End of iteration: " + i);
						 for(int k = 0; k < a.length; k++) {
							 System.out.print(a[k] + ", ");
						 }
						 System.out.println();

				  }
				 System.out.println("Total time for insertion of " + a.length + " is : " + (System.currentTimeMillis() - startTime));
				 System.out.println();
				  return a; 
	    }
	  public static void swap() {
		  
	  }

}
