package Controller;

import java.util.*;

public class Merge{
	  public Merge(Vector<String> buf1, Vector<String> buf2, Vector<Integer> list){
		  merge(buf1, buf2, list);
	  }
	   public void merge(Vector<String> buf1, Vector<String> buf2, Vector<Integer> list) {
			 for (int i = 0; i < list.size(); i++) {
		         buf2.setElementAt(buf1.get(list.get(i)), list.get(i));
		      }

			for (int i = list.lastElement(); i >= list.firstElement(); i--) {
				if (buf1.get(i).compareTo("\t\r") == 0) {
					buf1.remove(i);
					buf2.remove(i);
				}
			}
			 for(int i=0;i<buf1.size()-1;i++)
			 {
				 buf1.setElementAt(buf1.get(i).substring(0,buf1.get(i).length()-1), i);
				 buf2.setElementAt(buf2.get(i).substring(0,buf2.get(i).length()-1), i);
			 }

	   }
		public void transfer(Vector<String> buf1, Vector<String> buf2, Vector<Integer> list){
			
			for (int i = 0; i < list.size(); i++) {
				 buf2.setElementAt(buf1.get(list.get(i)), list.get(i));
		      }
			
		}
		public void eraseline(Vector<String> buf1, Vector<String> buf2, Vector<Integer> list){
		
			
			for (int i = list.lastElement(); i >= list.firstElement(); i--) {
				if (buf1.get(i).compareTo("\t\r") == 0) {
					buf1.remove(i);
					buf2.remove(i);
				}
			}
		  
		}
		public void erasecarriage(Vector<String> buf1, Vector<String> buf2, Vector<Integer> list){
			for(int i=0;i<buf1.size()-1;i++)
			 {
				 buf1.setElementAt(buf1.get(i).substring(0,buf1.get(i).length()-1), i);
				 buf2.setElementAt(buf2.get(i).substring(0,buf2.get(i).length()-1), i);
			 }
		}	
}