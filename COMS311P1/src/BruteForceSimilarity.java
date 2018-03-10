import java.util.ArrayList;
import java.util.Collections;

// LEAVE THIS FILE IN THE DEFAULT PACKAGE
//  (i.e., DO NOT add 'package cs311.pa1;' or similar)

// DO NOT MODIFY THE EXISTING METHOD SIGNATURES
//  (you may, however, add member fields and additional methods)

// DO NOT INCLUDE LIBRARIES OUTSIDE OF THE JAVA STANDARD LIBRARY
//  (i.e., you may include java.util.ArrayList etc. here, but not junit, apache commons, google guava, etc.)

/**
* @author Ben Holmes, Anthony House
*/

public class BruteForceSimilarity 
{
	String s1; 
	String s2; 
	int length; 
	

	public static ArrayList<String> s1DistinctStrings = new ArrayList<String>(); 
	public static ArrayList<String> s2DistinctStrings = new ArrayList<String>(); 
	
	public static ArrayList<Integer> S = new ArrayList<Integer>(); 
	public static ArrayList<Integer> T = new ArrayList<Integer>(); 
	
	public static ArrayList<String> distinctStrings = new ArrayList<String>(); 

	public BruteForceSimilarity(String s1, String s2, int sLength)
	{
		this.s1 = s1; 
		this.s2 = s2; 
		this.length = sLength; 
		
		hashItOut(s1, true); 
		hashItOut(s2, false);  
			
	}
	
	public static int getS1Count(String val) {
		if(!s1DistinctStrings.contains(val)) return 0; 
		
		int index = s1DistinctStrings.indexOf(val); 		
		return S.get(index); 
	}
	
	public static void addToS1(String val) {
		if(!s1DistinctStrings.contains(val)) {
			s1DistinctStrings.add(val); 
			S.add(1); 
		}else {
			int index = s1DistinctStrings.indexOf(val); 
			S.set(index, S.get(index)+1); 
		}
		addToDistinctStrings(val); 
	}
	
	public static int getS2Count(String val) {
		if(!s2DistinctStrings.contains(val)) return 0; 
		
		int index = s2DistinctStrings.indexOf(val); 		
		return T.get(index); 
	}
	
	public static void addToS2(String val) {
		if(!s2DistinctStrings.contains(val)) {
			s2DistinctStrings.add(val); 
			T.add(1); 
		}else {
			int index = s2DistinctStrings.indexOf(val); 
			T.set(index, T.get(index)+1); 
		}	
		addToDistinctStrings(val); 
	}
	
	public static void addToDistinctStrings(String value) {
		if(!distinctStrings.contains(value)) distinctStrings.add(value); 
	}
	

	public void hashItOut(String mainString, boolean s1) {
		if(mainString.length() < length) {
			return; 
		}
		if(mainString.length() == length) {
			if(s1) addToS1(mainString); 
			else addToS2(mainString); 
		}
		
		String firstString;  
		String secondString; 
		
		for(int i = 0; i <= mainString.length()-length; i++) {
			firstString = mainString.substring(i, i+ length); 
			if(s1) {
				addToS1(firstString); 
			}
			else {
				addToS2(firstString); 
			}
		}
	}
	
	public static int power(int k, int m) {
		int result = 0; 
		if(m == 0) {
			return 1; 
		}
		else if(m %2 == 0) {
			result = power(k, m/2); 
			return result * result; 

		}
		else {
			result = power(k,m/2); 
			return result*result*k; 
		}
	}

	public float lengthOfS1()
	{
		float result = 0.0f; 
		if(s1.length() < length) return 0.0f; 
		
		for(String string : s1DistinctStrings) {
			result += power(getS1Count(string), 2);  
		}
		result = (float) Math.sqrt(result);
		return  result; 
	}
	public float lengthOfS2()
	{
		float result = 0.0f; 
		if(s2.length() < length) return 0.0f; 
		
		for(String string : s2DistinctStrings) {
			result += power(getS2Count(string), 2);  
		}
		result = (float) Math.sqrt(result); 
		return result; 
	}

	public float similarity()
	{
		float result = 0.0f; 
		long topSummation = 0; 
		
		for(String string : distinctStrings) {
			topSummation += (getS1Count(string) * getS2Count(string)); 
		}
		float denominator = (this.lengthOfS1() * this.lengthOfS2()); 
		
		if(denominator == 0) return 0.0f; 

		result = topSummation/denominator; 
		return result; 
	}
}