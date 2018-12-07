package InterviewPrep; 
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
 
class TrieNode {
    char data;     
    LinkedList<TrieNode> children; 
    TrieNode parent;
    boolean isEnd;
 
    public TrieNode(char c) {
    	data = c;
        children = new LinkedList();
        isEnd = false;        
    }  
    
    public TrieNode getChild(char c) {
        if (children != null)
            for (TrieNode eachChild : children)
                if (eachChild.data == c)
                    return eachChild;
        return null;
    }
    
    public LinkedList<TrieNode> getChildren(){
    	return this.children; 
    }
    
    protected List getWords() {
       List list = new ArrayList();      
       if (isEnd) {
    	   list.add(toString());
       }
       
       if (children != null) {
	       for (int i=0; i< children.size(); i++) {
	          if (children.get(i) != null) {
	             list.addAll(children.get(i).getWords());
	          }
	       }
       }       
       return list; 
    }
    
	public String toString() {
		if (parent == null) {
		     return "";
		} else {
		     return parent.toString() + new String(new char[] {data});
		}
	}
}
 
class Trie {
	
	private TrieNode root; 
	
	public Trie() {
		root = new TrieNode(' '); 
	}
	
	public void insert(String word) {
		if(search(word))
			return; 
		
		TrieNode current = root; 
		TrieNode parent; 
		for(int i = 0; i < word.length(); i++) {
			char c = word.charAt(i); 
			parent = current; 
			TrieNode child = current.getChild(c); 
			if( child != null) {
				current = child; 
				current.parent = parent; 
			}else {
				child = new TrieNode(c); 
				child.parent = current; 
				current.children.add(child); 
				current = child; 
			}
		}
		current.isEnd = true; 
	}

	public boolean search(String word) {
		TrieNode current = root; 
		
		for(char ch : word.toCharArray()) {
			TrieNode child = current.getChild(ch); 
			if(child == null)
				return false;
			else
				current = child; 
		}
		if(current.isEnd)
			return true; 

		return false; 
	}
	
	public void removeNode(TrieNode parent, TrieNode child) {
		if(parent.getChild(child.data) != null) {
			LinkedList<TrieNode> children = parent.getChildren(); 
			children.remove(child); 
		}
	}
	
	/**
	 * Loop through characters in this word, and check current node for children matching char. 
	 * If child is found, set current node = child, and continue. 
	 * If you reach the end of the word, set isEnd= false, is an edge case where this word is a prefix for 
	 * other words. 
	 * Then as long as this child does not branch off to other words, delete it from parent's linkedList. 
	 * Then set current = parent; 
	 * @param word
	 */
	public void remove(String word) {
		TrieNode current = root; 
		for(char ch : word.toCharArray()) {
			TrieNode child = current.getChild(ch); 
			if(child != null) {
				current = child; 
			}
		}
		if(current.isEnd) {
			current.isEnd = false; 
			while(current.children == null || current.children.size() == 0) {
				removeNode(current.parent, current); 
				current = current.parent; 
			}
		}
	}
	
    
    public List autocomplete(String prefix) {     
       TrieNode lastNode = root;
       for (int i = 0; i< prefix.length(); i++) {
	       lastNode = lastNode.getChild(prefix.charAt(i));	     
	       if (lastNode == null) 
	    	   return new ArrayList();      
       }
       
       return lastNode.getWords();
    }
}    
 

