import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

// LEAVE THIS FILE IN THE DEFAULT PACKAGE
//  (i.e., DO NOT add 'package cs311.pa1;' or similar)

// DO NOT MODIFY THE EXISTING METHOD SIGNATURES
//  (you may, however, add member fields and additional methods)

// DO NOT INCLUDE LIBRARIES OUTSIDE OF THE JAVA STANDARD LIBRARY
//  (i.e., you may include java.util.ArrayList etc. here, but not junit, apache commons, google guava, etc.)

/**
 * Just a note, I emailed the TA's, and specifically asked if we were allowed to use Java's LinkedList. I was told we could, 
 * that is why the LinkedList library is included, I can present the email if needed. 
 */

/**
* @author Anthony House, Ben Holmes
*/

public class HashTable
{
	int size = 0; 
	HashFunction hashFunc; 
	HashFunction insideHashFunc; 
	int numOfElements; 
	ArrayList<Integer> loads; 

	
	ArrayList<ArrayList<LinkedList<Tuple>>> hashTable; 
	
	//10067 8641 7001 5701 4001 3001 1901 1009 409 109
	int insideTableSize = 1009; 

	

	/**
	 * There has to be a better way to instantiate the ArrayList of new LinkedLists than this for loop
	 * @param size
	 */
	public HashTable(int size)
	{
		hashFunc = new HashFunction(size); 
		insideHashFunc = new HashFunction(insideTableSize); 
		
		this.size = hashFunc.getPrime(); 
		
		loads = new ArrayList<Integer>(Collections.nCopies(this.size, 0)); 

		numOfElements = 0; 
		
		hashTable = new ArrayList<ArrayList<LinkedList<Tuple>>>(Collections.nCopies(this.size, null)); 
	}
	
	/**
	 * Method to retrieve the actual table, just for testing, to print out the hash table
	 * @return
	 */
	public ArrayList<ArrayList<LinkedList<Tuple>>> getTable(){
		return hashTable; 
	}

	/**
	 * 
	 * @return the maximum list size stored in the hashTable ArrayList. 
	 */
	public int maxLoad()
	{
		return Collections.max(loads); 
	}

	/**
	 * 
	 * @return the average of total elements/non null cells
	 */
	public float averageLoad()
	{
		int nonNullCells = 0; 
		int summationOfListSizes = 0; 
		for(int i =0; i < loads.size(); i++) {
			if(loads.get(i) > 0) {
				nonNullCells++; 
				summationOfListSizes += loads.get(i); 
			}
		}
		if(nonNullCells == 0 || summationOfListSizes == 0) return 0; 
		
		return summationOfListSizes/nonNullCells; 
	}

	public int size()
	{
		return this.size; 
	}

	/**
	 * Return the number of distinct elements in table
	 * @return
	 */
	public int numElements()
	{
		return this.numOfElements; 
	}

	/**
	 * 
	 * @return ratio of the number of elements to the total size of the table
	 */
	public float loadFactor()
	{
		if(size == 0) return 0f; 
		
		return numOfElements/size; 
	}

	/**
	 * Add new tuple to hashTable, update maxLoad, and numOfElements
	 * @param t
	 */
	public void add(Tuple t)
	{
		int hash = hashFunc.hash(t.getKey()); 
		int insideHash = insideHashFunc.hash(t.getKey() - hash); 
		
		if(hashTable.get(hash) == null) { 
			hashTable.set(hash, new ArrayList<LinkedList<Tuple>>(Collections.nCopies(insideTableSize,  null)));  
			hashTable.get(hash).set(insideHash, new LinkedList<Tuple>()); 
			hashTable.get(hash).get(insideHash).addFirst(t);
			numOfElements++; 
		}else if(hashTable.get(hash).get(insideHash) == null) {
			hashTable.get(hash).set(insideHash, new LinkedList<Tuple>()); 
			hashTable.get(hash).get(insideHash).addFirst(t);
			numOfElements++; 
		}
		else{
			LinkedList<Tuple> linkedTuples = hashTable.get(hash).get(insideHash); 
			boolean found = false; 
			for(int i = 0; i < linkedTuples.size(); i++) {
				Tuple tuple = linkedTuples.get(i); 
				if(t.equals(tuple)) {
					hashTable.get(hash).get(insideHash).get(i).increment();
					found = true; 
					break; 
				}
			}
			if(!found) {
				hashTable.get(hash).get(insideHash).addFirst(t); 
				numOfElements++; 
			}
		}
		loads.set(hash, loads.get(hash) + 1); 
	}

	/**
	 * 
	 * @param k int key 
	 * @return array list of all tuples with given key
	 */
	public ArrayList<Tuple> search(int k)
	{
		List<Tuple> result = new ArrayList<Tuple>(); 
		int hash = hashFunc.hash(k); 
		int insideHash = insideHashFunc.hash(k-hash); 
		
		if(hashTable.get(hash) == null) return (ArrayList<Tuple>) result; 
		

		else if(hashTable.get(hash).get(insideHash) == null) {
			return (ArrayList<Tuple>) result; 
		}
		else {
			Iterator<Tuple> it = hashTable.get(hash).get(insideHash).iterator();  
			while(it.hasNext()) {
				Tuple tuple = it.next(); 
				if(tuple.key == k) {
					result.add(tuple); 
				}
			}	
		}		
		return (ArrayList<Tuple>) result; 
	}

	/**
	 * 
	 * @param t Tuple
	 * @return number of occurences of a given Tuple in the hashTable
	 */
	public int search(Tuple t)
	{
		int hash = hashFunc.hash(t.getKey()); 
		int insideHash = insideHashFunc.hash(t.getKey() - hash); 
		int result = 0; 
		
		if(hashTable.get(hash) == null) return 0; 
		else if(hashTable.get(hash).get(insideHash) == null) {
			return 0; 
		}else {
			Iterator<Tuple> it = hashTable.get(hash).get(insideHash).iterator(); 
			while(it.hasNext()) {
				Tuple tuple = (Tuple) it.next(); 
				if(tuple.equals(t)) {
					result += tuple.occurrences; 
				}
			}
		}	

		return result; 
	}

	/**
	 * Removes first occurence of the given tuple inside of hashTable
	 * @param t
	 */
	public void remove(Tuple t)
	{	
		int hash = hashFunc.hash(t.getKey()); 
		int insideHash = insideHashFunc.hash(t.getKey() - hash); 
		
		if(hashTable.get(hash) == null) return; 
		else if(hashTable.get(hash).get(insideHash) == null) {
			return; 
		}
		else {
			for(int i = 0; i < hashTable.get(hash).get(insideHash).size(); i++) {
				if(hashTable.get(hash).get(insideHash).get(i).equals(t)) {
					hashTable.get(hash).get(insideHash).get(i).decrement(); 
					loads.set(hash, loads.get(hash) - 1); 
					
					if(hashTable.get(hash).get(insideHash).get(i).occurrences == 0) {
						hashTable.get(hash).get(insideHash).set(i, null); 
						numOfElements--; 
					}
				}
			}
		}
	}
	
	
}