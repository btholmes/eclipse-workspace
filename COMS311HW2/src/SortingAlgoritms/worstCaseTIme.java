package SortingAlgoritms;

public class worstCaseTIme {
	
	public static void main(String[] args) {
		function(10); 
	}
	
	
	public static void function(int n) {
		int r = 0;
		for (int i = 1; i < n; i++){
			int sum = 0; 
			System.out.println("Iteration i " + i);
			for (int j = 1; j < (n-i+1); j++){
				System.out.println("\t Iteration j  " + (j+(i-1)));
				for(int k = 1; k < (j-1); k++){
					sum++; 
					System.out.println("	\tIteration k " + k);
					r++; 
				}
			}
			System.out.println("Total k iterations was " + sum);
		}
		System.out.println("Final r is " + r);
	}

}
