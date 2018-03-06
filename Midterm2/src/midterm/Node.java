package midterm;

public class Node {
		int data; 
		Node left; 
		Node right; 
		Node parent; 
		public Node() {
			
		}
	  public Node(int item)
	    {
	        data = item;
	        left = right = null;
	    }
		
		public void setData(int data) {
			this.data = data; 
		}
		
		public void setLeft(Node left) {
			this.left = left; 
		}
		public void setRight(Node right) {
			this.right = right; 
		}
		public void setParent(Node parent) {
			this.parent = parent; 
		}
		
		public int getData() {
			return this.data; 
		}
		public Node getLeft() {
			return this.left; 
		}
		
		public Node getRight() {
			return this.right; 
		}
		public Node getParent() {
			return this.parent; 
		}
	
}
