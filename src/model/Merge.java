package model;

import java.util.*;

public class Merge {
	public Merge(ArrayList<String> d1, ArrayList<String> d2, ArrayList<Integer> list) {
		do_merge(d1,d2,list);
	}
	
	public void do_merge(ArrayList<String> d1, ArrayList<String> d2, ArrayList<Integer> list) {
		for(int i = 0; i<list.size(); i++) {
			d2.set(d1.get(list.get(i)) , list.get(i));
		}
		
		for(int i = list.get(list.size()-1); i >= list.get(0); i--) {
			if(d1.get(i).compareTo("\t\r") == 0) {
				d1.remove(i);
				d2.remove(i);
			}
		}
		
		for(int i = 0; i<d1.size()-1; i++) {
			d1.set(d1.get(i).substring(0, d1.get(i).length()-1), i);
			d2.set(d2.get(i).substring(0, d2.get(i).length()-1), i);
		}
		
	}
}
