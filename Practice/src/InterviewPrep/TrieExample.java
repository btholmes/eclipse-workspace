package InterviewPrep;

import java.util.List;

public class TrieExample {
	 public static void main(String[] args) {            
//         Trie t = new Trie();      
//         t.insert("a");
//         t.insert("amaz");
//         t.insert("amazon"); 
//         t.insert("amazon prime"); 
//			t.insert("amazing"); 			 
//         t.insert("amazing spider man"); 
//         t.insert("amazed");
//         t.insert("alibaba");
//         t.insert("ali express");
//         t.insert("ebay");
//         t.insert("walmart");       
//         
////         t.remove("a");
//         
//			List a= t.autocomplete("a");
//			for (int i = 0; i < a.size(); i++) {
//				System.out.println(a.get(i));
//			}
		 
//		 String word = reverseWords("   a   b "); 
		 
		 System.out.println(maxCoins(new int[] {1,3,1,5,8,1}));
		 
	  }
	 
	 
	 public static int maxCoins(int[] coins) {
		 return burst(coins, 1, 1); 
	 }
	 
	 
	 public static int burst(int[] coins, int total, int i) {
		 if(coins == null || coins.length == 0)
			 return 1; 
		 if(i > coins.length-1) return total; 
		 
		 int a = burst(coins, total, i+1); 
		 int b = total + coins[i-1] * coins[i] * coins[i+1]; 
		 total = Math.max(a, b); 
		 
		 return total; 
	 }
	 
	    public static String reverseWords(String s) {
	        if(s == null) return null; 
	        
	        String[] arr = s.split(" "); 
	        for(int i = 0, j = arr.length-1; i < j; i++, j--){
	            swap(arr, i, j); 
	        }
	        String result = ""; 
	        boolean first = true; 
	        for(String item : arr){
	            if(item.equals(" ")) continue; 
	            
	            result += " " + item; 
	        }
	        return result.trim(); 
	    }
	    
	    private static void swap(String[] arr, int i, int j){
	        String temp = arr[i]; 
	        arr[i] = arr[j]; 
	        arr[j] = temp; 
	    }
}
