import java.util.ArrayList;
import java.util.Collections;

// LEAVE THIS FILE IN THE DEFAULT PACKAGE
//  (i.e., DO NOT add 'package cs311.pa1;' or similar)

// DO NOT MODIFY THE EXISTING METHOD SIGNATURES
//  (you may, however, add member fields and additional methods)

// DO NOT INCLUDE LIBRARIES OUTSIDE OF THE JAVA STANDARD LIBRARY
//  (i.e., you may include java.util.ArrayList etc. here, but not junit, apache commons, google guava, etc.)

/**
* @author Anthony House, Ben Holmes
*/

public class Tuple
{
	int key; 
	String value; 
	int occurrences;


	public Tuple(int keyP, String valueP)
	{
		this.key = keyP; 
		this.value = valueP; 
		this.occurrences = 1; 
	}
	
	public void increment() {
		this.occurrences += 1; 
	}
	
	public void decrement() {
		this.occurrences -= 1; 
	}

	public int getKey()
	{
		return key; 
	}

	public String getValue()
	{
		return value; 
	}

	public boolean equals(Tuple t)
	{
		boolean result = false; 
		if(t.key == this.key && t.value.equals(this.value)) {
			result = true; 
		}
		return result; 
	}
}