package midterm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class BFS_DFS_TREE
{
    // Root of the Binary Tree
    public static Node root;
 
    public BFS_DFS_TREE()
    {
        root = null;
    }
 
    /* function to print level order traversal of tree*/
    void printLevelOrder()
    {
        int h = height(root);
        int i;
        for (i=1; i<=h; i++)
            System.out.print(printGivenLevel(root, i) + " ");
    }
 
    /* Compute the "height" of a tree -- the number of
    nodes along the longest path from the root node
    down to the farthest leaf node.*/
    int height(Node root)
    {
        if (root == null)
           return 0;
        else
        {
            /* compute  height of each subtree */
            int lheight = height(root.left);
            int rheight = height(root.right);
             
            /* use the larger one */
            if (lheight > rheight)
                return(lheight+1);
            else return(rheight+1); 
        }
    }
 
    /* Print nodes at the given level */
    int printGivenLevel (Node root ,int level)
    {
    		int result = 0; 
        if (root == null)
            return 0;
        if (level == 1)
//            System.out.print(root.data + " ");
        		 return root.data; 
        else if (level > 1)
        {
            result += printGivenLevel(root.left, level-1);
            result += printGivenLevel(root.right, level-1);
        }
        return result; 
    }
    
    
    void printColumns(Node root, int column, HashMap<Integer, Integer> map) {
    		if(root == null) return; 
    		if(map.get(column) == null) map.put(column, root.data);
    		else map.put(column, map.get(column) + root.data);
    		
    		printColumns(root.right, column+1, map); 
    		printColumns(root.left, column-1, map); 
    		
    }
        
    void printPostorder(Node node)
    {
        if (node == null)
            return;
 
        // first recur on left subtree
        printPostorder(node.left);
 
        // then recur on right subtree
        printPostorder(node.right);
 
        // now deal with the node
        System.out.print(node.data + " ");
    }
 
    /* Given a binary tree, print its nodes in inorder*/
    void printInorder(Node node)
    {
        if (node == null)
            return;
 
        /* first recur on left child */
        printInorder(node.left);
 
        /* then print the data of node */
        System.out.print(node.data + " ");
 
        /* now recur on right child */
        printInorder(node.right);
    }
 
    /* Given a binary tree, print its nodes in preorder*/
    void printPreorder(Node node)
    {
        if (node == null)
            return;
 
        /* first print data of node */
        System.out.print(node.data + " ");
 
        /* then recur on left sutree */
        printPreorder(node.left);
 
        /* now recur on right subtree */
        printPreorder(node.right);
    }
    
    public static Node insert(Node root, int val) {
    		if(root == null) {
    			Node node = new Node(); 
    			node.data = val; 
    			root = node; 
    		}else if(val < root.data) {
    			if(root.left == null) {
    				Node node = new Node(); 
        			node.data = val; 
        			root.left = node; 
    			}else {
    				insert(root.left, val); 
    			}  			
    		}else {
    			if(root.right == null) {
    				Node node = new Node(); 
    				node.data = val; 
    				root.right = node; 
    			}else {
    				insert(root.right, val); 
    			}	
    		}    		
    		return root; 
    }
    
//    // Wrappers over above recursive functions
//    void printPostorder()  {     printPostorder(root);  }
//    void printInorder()    {     printInorder(root);   }
//    void printPreorder()   {     printPreorder(root);  }
     
    /* Driver program to test above functions */
    public static void main(String args[])
    {
    		BFS_DFS_TREE tree = new BFS_DFS_TREE();
    		tree.root= new Node(5);
    		tree.insert(root, 4); 
    		tree.insert(root, 3);
    		tree.insert(root, 2);
    		tree.insert(root, 0);
    		tree.insert(root, 1);
    		tree.insert(root, 10);
    		tree.insert(root, 8);
    		tree.insert(root, 9);
    		
    		

        
       
       System.out.println("Level order (BFS) traversal of binary tree is ");
       tree.printLevelOrder();
       
       

       System.out.println("Preorder traversal of binary tree is ");
       tree.printPreorder(tree.root);

       System.out.println("\nInorder traversal of binary tree is ");
       tree.printInorder(tree.root);

       System.out.println("\nPostorder traversal of binary tree is ");
       tree.printPostorder(tree.root);
       
       HashMap<Integer, Integer> map = new HashMap<Integer, Integer>(); 
       tree.printColumns(root, 0, map);
       System.out.println();
       Iterator it = map.entrySet().iterator(); 
       while(it.hasNext()) {
    	   	Map.Entry pair = (Map.Entry)it.next();  
    	   	System.out.println(pair.getKey() + " , " + pair.getValue());
       }
       
    }
}