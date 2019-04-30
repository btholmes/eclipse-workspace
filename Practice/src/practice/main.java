package practice; 

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class main {

	public static void main(String[] args) {
		Me a = new Me(0); 
		System.out.println(a.getN());
		Me b = new Me(5); 
		System.out.println(b.getN());
		Me c = new Me(5); 
		System.out.println(c.getN());
		Me d = new Me(10); 
		System.out.println(d.getN());
	}

	public void combine(List<Integer> list, List<List<Integer>> result) {

	}

	public static void print(ArrayList<Integer> list, int i) {
		if(i < 0) return;

		System.out.print(list.get(i) + " ");;
		print(list, i-1);
		System.out.println("");
	}

}
