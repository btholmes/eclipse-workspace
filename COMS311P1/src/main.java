import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class main {

	public static HashTable hashTable; 
	
    public static String vectorString1 = "ABFHBFDFAB";
    public static String vectorString2 = "BEAAHHDCH";
    public static String similarityString1 = "aroseisaroseisarose";
    public static String similarityString2 = "aroseisaflowerwhichisarose";

    public static int vectorShingleLength = 1;
    public static int similarityShingleLength = 4;
	
	public static void main(String[] args) throws IOException {
		 FileReader test1 =new FileReader("/Users/benholmes/eclipse-workspace/COMS311P1/src/shak1.txt");    
		 FileReader test2 = new FileReader("/Users/benholmes/eclipse-workspace/COMS311P1/src/shak2.txt"); 
         BufferedReader br=new BufferedReader(test1);    
  
          int i;    
          String line; 
          String s1 = ""; 
          String s2 = ""; 
          line = br.readLine(); 
          while(line != null){  
//        	  	line = line.toLowerCase(); 
//        	  	line = line.replaceAll("[^A-Za-z0-9]", ""); 
        	  	line = line.replaceAll(" ", "")
                .replaceAll("\t", "")
                .replaceAll("\\.", "")
                .replaceAll(",", "")
                .replaceAll(":", "")
                .replaceAll(";", "");
        	  	
        	  	line.toLowerCase(); 
        	  	s1 += line; 
        	  	line = br.readLine(); 
          }  
          
          br = new BufferedReader(test2);
          line = br.readLine(); 
          while(line != null) {
//        		line = line.toLowerCase(); 
//        	  	line = line.replaceAll("[^A-Za-z0-9]", ""); 
        		line = line.replaceAll(" ", "")
                .replaceAll("\t", "")
                .replaceAll("\\.", "")
                .replaceAll(",", "")
                .replaceAll(":", "")
                .replaceAll(";", "");
        		line.toLowerCase(); 
        		
        	  	s2 += line; 
        	  	line = br.readLine(); 
          }
          
          br.close();    
          test1.close();   
          test2.close(); 
		
//          s1 = "aroseisaroseisarose"; 
//          s2 = "aroseisarosewhichisaflowerwhichisarose"; 
          
    	    Long time = System.currentTimeMillis(); 
//    		BruteForceSimilarity brute = new BruteForceSimilarity(s1, s2, 8); 
//    		System.out.println(brute.similarity());
//      	System.out.println("Total time was " + new Double((System.currentTimeMillis() - time)/1000.0));
		
//        time = System.currentTimeMillis(); 
  		HashStringSimilarity stringSimilarity = new HashStringSimilarity(s1, s2, 8);
  		System.out.println(stringSimilarity.similarity());
  		System.out.println("Total time was " + new Double((System.currentTimeMillis() - time)/1000.0));
	

  	    time = System.currentTimeMillis(); 
		HashCodeSimilarity hashCodeSimilarity = new HashCodeSimilarity(s1, s2, 8); 
		System.out.println(hashCodeSimilarity.similarity());
 		System.out.println("Total time was " + new Double((System.currentTimeMillis() - time)/1000.0));
 		
 		String stuff = "123456789"; 

 		
// 		int shouldBe = computeHash("2345"); 
// 		System.out.println("Should be " + shouldBe);
//
// 		int hash = computeHash("1234"); 
// 		System.out.println("Starting hash is " + hash);
// 		hash = hash - stuff.charAt(0); 
// 		hash = hash/1009; 
// 		hash += '5' * power(1009, 3); 
// 		System.out.println(hash);
// 		
		
	}
	public static int computeHash(String stuff) {
		int result = 0; 
		for(int i =0; i < 4; i++) {
			result += stuff.charAt(i) * power(1009, i); 
		}
		return result; 
	}
	
	public static int power(int k, int m) {
		int result = 0; 
		if(m == 0) {
//			System.out.println("Base case m is  : " + m);
			return 1; 
		}
		else if(m %2 == 0) {
			result = power(k, m/2); 
//			System.out.println("Even m : " + m);
//			System.out.println("Result is " + result);
			return result * result; 

		}
		else {
			result = power(k,m/2); 
//			System.out.println("Odd m : " + m);
//			System.out.println("result is " + result);
			return result*result*k; 
		}
	}
	
}
