package SortingAlgoritms;


public class LogNPowerFunction {
	
	public static void main(String[] args) {
		System.out.println(1/2);
		System.out.println(power(2, 16)); 
	}
	
	public static int power(int k, int m) {
		int result = 0; 
		if(m == 0) {
			System.out.println("Base case m is  : " + m);
			return 1; 
		}
		else if(m %2 == 0) {
			result = power(k, m/2); 
			System.out.println("Even m : " + m);
			System.out.println("Result is " + result);
			return result * result; 

		}
		else {
			result = power(k,m/2); 
			System.out.println("Odd m : " + m);
			System.out.println("result is " + result);
			return result*result*k; 
		}
	}

}
