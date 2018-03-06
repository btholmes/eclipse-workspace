import java.util.ArrayList;

// LEAVE THIS FILE IN THE DEFAULT PACKAGE
//  (i.e., DO NOT add 'package cs311.pa1;' or similar)

// DO NOT MODIFY THE EXISTING METHOD SIGNATURES
//  (you may, however, add member fields and additional methods)

// DO NOT INCLUDE LIBRARIES OUTSIDE OF THE JAVA STANDARD LIBRARY
//  (i.e., you may include java.util.ArrayList etc. here, but not junit, apache commons, google guava, etc.)

/**
* @author Ben Holmes, Anthony House
*/

public class HashCodeSimilarity 
{
	private String s1; 
	private String s2; 
	private int length; 
	private int prime = 31; 
	
	private HashTable S; 
	private ArrayList<Integer> s1DistinctHashes; 
	
	private HashTable T; 
	private ArrayList<Integer> s2DistinctHashes; 
	
	private ArrayList<Integer> distinctHashes; 
	private HashTable U; 

	public HashCodeSimilarity(String s1, String s2, int sLength)
	{
		this.s1 = s1; 
		this.s2 = s2; 
		this.length = sLength; 
		//1009 104729  10067 109
		int tableSize = 1009; 
		
		S = new HashTable(tableSize);
		s1DistinctHashes = new ArrayList<Integer>(); 
		
		T = new HashTable(tableSize); 
		s2DistinctHashes = new ArrayList<Integer>(); 
		
		distinctHashes = new ArrayList<Integer>(); 
		U = new HashTable(tableSize); 
		
		hashItOut(s1, true); 
		hashItOut(s2, false); 
	}
	
	public HashTable getS() {
		return S; 
	}
	
	public HashTable getT() {
		return T; 
	}
	
	public void addToS1(String string, int hash) {
		Tuple tuple = new Tuple(hash, string);  
		
		if(S.search(tuple) == 0) s1DistinctHashes.add(hash); 
		
		S.add(tuple);
		
		if(U.search(tuple) == 0) {
			U.add(tuple);
			distinctHashes.add(hash); 
		}
		
	}
	
	public void addToS2(String string, int hash) {
		Tuple tuple = new Tuple(hash, string); 
		
		if(T.search(tuple) == 0) s2DistinctHashes.add(hash); 
		
		T.add(tuple); 
		
		if(U.search(tuple) == 0) {
			U.add(tuple);
			distinctHashes.add(hash); 
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
	
	
	public int computeHash(String string) {
		int result = 0; 
		
		for(int i = 0; i < length; i++) {
			result += string.charAt(i) * power(prime, i); 
		}
		return result; 
	}
	
	/**
	 * Just a note, we chose not to use roll over hashing because due to overflow, it results in way too many incorrect hashes. 
	 * If we would have been allowed to use a long instead of an int, we would have used rollover with much better accurracy, 
	 * but because we could only store an int value, we chose accuracy over the speed improvement. 
	 * 
	 * @param mainString
	 * @param s1
	 */
	public void hashItOut(String mainString, boolean s1) {
		if(mainString.length() < length) {
			return; 
		}
		if(mainString.length() == length) {
			if(s1) addToS1(mainString, computeHash(mainString)); 
			else addToS2(mainString, computeHash(mainString)); 
		}
		
		String firstString = "";  
		int hash = 0;
		for(int i = 0; i <= mainString.length()-length; i++) {
			firstString = mainString.substring(i, i+ length); 
			hash = computeHash(firstString); 
			if(s1) addToS1(firstString, hash); 
			else addToS2(firstString, hash); 
//			hash -= mainString.charAt(i); 
//			hash = hash/prime; 
//			hash += mainString.charAt(i+length) * power(prime, length -1);
		}
//		if(s1) {
//			addToS1(firstString, hash); 
//		}
//		else addToS2(firstString, hash); 
			
	}

	public float lengthOfS1()
	{
		float result = 0.0f; 
		if(s1.length() < length) return 0.0f; 
		
		for(Integer hash : s1DistinctHashes) {
			ArrayList<Tuple> list = S.search(hash); 
			if(list.size() > 0) {
				for(Tuple tuple : list) {
					result += power(tuple.occurrences, 2); 
				}
			}
		}
		
		return (float) Math.sqrt(result); 
	}

	public float lengthOfS2()
	{
		float result = 0.0f; 
		if(s2.length() < length) return 0.0f; 
		
		for(Integer hash : s2DistinctHashes) {
			ArrayList<Tuple> list = T.search(hash); 
			if(list.size() > 0) {
				for(Tuple tuple : list) {
					result += power(tuple.occurrences, 2); 
				} 
			}
		}	
		return (float) Math.sqrt(result); 
	}

	public float similarity()
	{
		float result = 0.0f; 
		float topSummation = 0.0f; 
		
		for(Integer hash : distinctHashes) {
			ArrayList<Tuple> list1 = S.search(hash); 
			ArrayList<Tuple> list2 = T.search(hash); 
			if(list1.size() > 0 && list2.size()>0) {
				int s1Occurrences = 0; 
				int s2Occurrences = 0; 
				for(Tuple tuple : list1) {
					s1Occurrences += tuple.occurrences; 
				}
				for(Tuple tuple : list2) {
					s2Occurrences += tuple.occurrences; 
				}
				topSummation += (s1Occurrences * s2Occurrences); 
			}
		}
		
		float denominator = (this.lengthOfS1() * this.lengthOfS2()); 
		
		if(denominator == 0) return 0.0f; 

		result = topSummation/denominator; 
		return result; 
	}
}