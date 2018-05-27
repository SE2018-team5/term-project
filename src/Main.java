import java.io.File;
import java.util.Scanner;

import controller.LCSubsequence;
import model.Node;


public class Main {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
    	
        File	file1 = new File("input1.txt");
        File	file2 = new File("input2.txt");
        
        Scanner input1;
        Scanner input2;
        
        StringBuffer text1 = new StringBuffer();
      	StringBuffer text2 = new StringBuffer();
        try {
        	input1 = new Scanner(file1);
        	input2 = new Scanner(file2);
        	
        	while(input1.hasNextLine()) {
        		text1.append(input1.nextLine() + "\r\n");
        	}
        	
        	while(input2.hasNextLine()) {
        		text2.append(input2.nextLine() + "\r\n"); 	
        	}
        } catch(Exception e) {
        	e.printStackTrace(System.out);
        }
        
        
        LCSubsequence l = new LCSubsequence();
//        LinkedList<LongestCommonSubsequence.Diff> list = l.diff_main(text1.toString(), text2.toString());
        
        for(Node e : LCSubsequence.getDiff(text1.toString(), text2.toString())) {
            System.out.println(e.toString());
        }
        
      
    }

}
