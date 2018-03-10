package hw3;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

//Java program to print BST in given range
//Works in O(n) time 
//T(n) = 2T(n/2) + C
//T(n) -->  Time taken for an array of size n
// C   -->  Constant (Finding middle of array and linking root to left 
//                    and right subtrees take constant time) 
//A binary tree node

public class SortedArrayToBST {
	
	 public static void main(String[] args) {
	     BinaryTree tree = new BinaryTree();
	     tree.add(4);
	     tree.add(2);
	     tree.add(1);
	     tree.add(3); 
	     tree.add(6);
	     tree.add(5);
	     tree.add(7);
//	     
	     tree.preOrder(tree.root);
//	     System.out.println();
//	     
	     System.out.println(tree.order(7));
//	     System.out.println(tree.getMinimumDifference(tree.root, 0));
	     
//	     System.out.println(tree.frequency(7));
	     
	     
//	     int arr[] = new int[]{1,2,3,4,5,6,7, 7};
//	     int n = arr.length;
//	     Node root = tree.sortedArrayToBST(arr, 0, n - 1);
//	     
//	     System.out.println(tree.order(root, 5));
//	     tree.preOrder(root);
//	     
//	     System.out.println("Min diff is " + tree.getMinimumDifference(root, 1)); 
//	     tree.printLeafNodes(root, root, 3); 
//	 
//	     System.out.println("Order of root is : " + tree.findOrder(root,  3));
	 }
	

	static class Node {
	  
		 int data;
		 Node left, right;
		 Node parent; 
		 int frequency; 
		 int numRight; 
		 
		  
		 Node(int d) {
		     data = d;
		     left = right = null;
		     frequency = 1; 
		      numRight = 0; 
		 }
	}
	
	static class BinaryTree {
		 static Node root;
		
		public BinaryTree() {
			root = null; 
		}
		
		 /**
		  * Finds elements less than or equal to a given key in O(n) time
		  * @param root
		  * @param key
		  * @return
		  */
		 public int findOrder(Node root, int key) {
			 if(root == null) {
				 return 0; 
			 }
			 if(root.data == key) {
				 return 1 + findOrder(root.left, key); 
			 }
			 if(root.data < key) {
				 return 1 + findOrder(root.left, key) + findOrder(root.right, key); 
			 }
			 return findOrder(root.left, key); 
		 }
		 
			// nodes from left to right
		 //Time Complexity: O( n ) , where n is number of nodes in the bi
			public void printLeafNodes(Node trueRoot, Node root, int key)
			{
		    			    
			    if (root.left==null && root.right==null)
			    		System.out.println(root.data + " " + findOrder(trueRoot, root.data));
			    
			    if (root.left != null) 
				       printLeafNodes(trueRoot, root.left, key);
				       
			    if (root.right !=null)
			       printLeafNodes(trueRoot, root.right, key);
			} 
	
	 /* A function that constructs Balanced Binary Search Tree 
	  from a sorted array */
	 Node sortedArrayToBST(int arr[], int start, int end) {
	
	     /* Base Case */
	     if (start > end) {
	         return null;
	     }
	
	     /* Get the middle element and make it root */
	     int mid = (start + end) / 2;
	     Node node = new Node(arr[mid]);
	     node.numRight = arr.length - (mid+1); 
	
	     node.left = sortedArrayToBST(arr, start, mid - 1);

	     node.right = sortedArrayToBST(arr, mid + 1, end);
	      
	     return node;
	 }
	 
	 
//	 the worst case stack size are the total number of nodes, the average tree height. 
//	 Therefore, the space complexity is O (\ log n) O (logn) 
//	 in average, and each node is accessed once, the time complexity is O (n) O (n).
	 public int getMinimumDifference(Node T, int k) {
	     int min = Integer.MAX_VALUE;
	     Node prev = null;
	     Node a, b; 
	     Deque<Node> stack = new ArrayDeque<Node>();
	     Deque<Node> copy = new ArrayDeque<Node>(); 
	     Node[] result = new Node[2]; 
	
	     while (T != null || (!stack.isEmpty())) {
	         if (T != null) {
	             stack.push(T);
	             copy.push(T); 
	             T = T.left;
	         } else {
	        	      
	             if (prev != null) {
	                 if(Math.abs((T.data - prev.data) - k) < min) {
	                	 	min = Math.abs((T.data-prev.data)); 
	                	   	a = T; 
	  	                b = prev; 
	  	                result[0] = T; 
	  	                result[1] = prev; 
	                 }
	              
	             }
	             prev = T;
	             T = T.right;
	         }
	     }
	     
	     return min;
	 }
	 
//	 public int getMinDiffRecursive(Node root) {
//		    int min = Integer.MAX_VALUE;
//		    Node prev = null;
////		    
//		    public int getMinimumDifference(Node root) {
//		        if (root == null) return min;
//
//		        getMinimumDifference(root.left);
//		        if (prev != null) {
//		            min = Math.min(min, Math.abs(root.data - prev.data));
//		        }
//		        prev = root;
//		        getMinimumDifference(root.right);
//		        return min;
//		    }
//	 }
	 
//	 public int findOrder(Node root) {
//		 
//		 
//	 }
	 
	
	 
	 public Boolean search(Node root, int key) {
		 Node result = exists(root, key); 
		 if(result != null) return true; 
		 return false; 
	 }

	 public Node exists(Node root, int key)
	 {
	     if (root==null || root.data==key)
	         return root;
  	     if (root.data > key)
	         return exists(root.left, key);
	     return exists(root.right, key);
	 }
	 
	 public int frequency(int x) {
		 Node node = exists(root, x); 
		 if(node != null) return node.frequency; 
		 return 0; 
	 }
	 
	 public int order( int x) {
		 Node node = exists(root, x); 
		 if(node != null) return node.numRight; 
		 return 0; 
	 }
	
	 public void updateWeights(Node root) {
		 Node parent = root.parent; 
		 int justAdded = root.data; 
		 while(parent != null) {
			 if(justAdded > parent.data) {
				 parent.numRight +=1; 
				 parent = parent.parent; 
			 }
		 }
	 }
	
	  // This method mainly calls insertRec()
	    void add(int key) {
	    		count = 0; 
	    	   Node parent = null; 
	       root = insertRec(root, key);
	    }
	     
	    int count = 0; 
	    /* A recursive function to insert a new key in BST */
	    Node insertRec(Node root, int key) {
	        if (root == null) {
	            root = new Node(key); 
	            root.numRight = count;     
	            return root;
	        }
	        if (key < root.data) {	    
	            Node lchild = insertRec(root.left, key);
	            count+= 1 + root.numRight; 
	            root.left = lchild; 
	            lchild.parent = root; 
	            
	        }
	        else if (key > root.data) {
	        		root.numRight += 1; 
	            Node rchild = insertRec(root.right, key);
	            root.right = rchild; 
	            rchild.parent = root; 
	        }
	        else if(key == root.data) {
	        		root.frequency += 1; 
	        }
	        return root;
	    }
	 
	 
	 /* A utility function to print preorder traversal of BST */
	 void preOrder(Node node) {
	     if (node == null) {
	         return;
	     }
	     System.out.println(node.data + " ");
	     if(node.parent != null) System.out.println("Parent is " + node.parent.data);
	     preOrder(node.left);
	     preOrder(node.right);
	 }
	 
	// function to print leaf 

	  
	
	}
}