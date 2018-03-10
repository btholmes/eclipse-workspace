package stuff;

import java.math.BigInteger;

public class HW1 {
	
	public static void main(String[] args) {
//		problem3(BigInteger.valueOf(1000), BigInteger.valueOf(1000)); //Evens
//		problem3(81, 81);  //Odds
//		problem3(8, 9); //Even, Odd
//		problem3(9,8); //Odd, even
//		int[] a = {1,2,3,4,5,6,7,8,9,10,11,12,13,14}; 
		int[] a = {1,2,3,4,5,6,7,8,9,10,11}; 
		int T = 14; 
		problem4(a, T); 
		
	}
	
	public static BigInteger pow(BigInteger base, BigInteger exponent) {
		  BigInteger result = BigInteger.ONE;
		  while (exponent.signum() > 0) {
		    if (exponent.testBit(0)) result = result.multiply(base);
		    base = base.multiply(base);
		    exponent = exponent.shiftRight(1);
		  }
		  return result;
		}
	
//	At the start of the Ith iteration, Vi a^n = x(i)^mi x y(i)
	public static void problem3(BigInteger a, BigInteger n) {
		int i = 0; 
		BigInteger x = a; 
		BigInteger m = n; 
		BigInteger y = BigInteger.ONE; 
		while (m.compareTo(BigInteger.ONE) == 1) {
			System.out.println("Start of Iteration "+ i + " : " +"\n a = " + a +  "\n n = " + n + "\n x = " + x + "\n m = " + m + "\n y = " + y );
			System.out.println("a^n : " + pow(a, n) + "\n AND x^m * y : " + pow(x, m).multiply(y) );
			if (m.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) {
				x = x.multiply(x);
				m = m.divide(BigInteger.valueOf(2));
			}
			else {
				y = x.multiply(y);
				x = x.multiply(x);
				m = (m.subtract(BigInteger.ONE)).divide(BigInteger.valueOf(2));
			}
			i++; 
		}
		System.out.println("OUTPUT : " + x.multiply(y));
		System.out.println();
	}
	
	public static boolean problem4(int[] a, int T) {
//		int[] a = {1,2,3,4,5,6,7,8,9,10,11,12,13,14}; 
//		int[] a = {1,2,3,4,5,6,7,8,9,10,11}; 
//		int T = 14; 
		int i = 0; 
		int left = 0;
		int right = a.length -1;
		while (left <=right){
		int x = a[left] + a[right];
		System.out.println("Start of Iteration " + i + " \nLeft is : " + left + " \nright is : " + right);

			if (x==T) {
				System.out.println("Found at Iteration " + i + " \nLeft is : " + left + " \nright is : " + right);
				return true;
			}
			if (x < T) {
				left++;
			}
			if (x > T) {
				right--;
			}
			i++; 
		}
		return false;
	}
	
	

}

