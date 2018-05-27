import test.ArrayList;
import test.LongestCommonSubseq;

package model;

import java.util.*;

// Hyunjae Lee

public class copyTodirection {
	
	final private ArrayList<String> toLeft;
	final private ArrayList<String> toRight;
	private LongestCommonSubseq<String> lcs;
	
	private ArrayList<Integer> left_match;
	private ArrayList<Integer> right_match;
	private ArrayList<Node> left_change;
	private ArrayList<Node> right_change;
	
	private int compare_len;
	
	public copyTodirection (ArrayList<String> Left, ArrayList<String> Right) {
		// initialize the stack of left or the stack of right
		
		if(Left == null) {
			this.toLeft = new ArrayList<String>();
		}else{
			this.toLeft = new ArrayList<String>(Left);
		} 
		if(Right == null)
		{
			this.toRight = new ArrayList<String>();			
		}else {	
			this.toRight = new ArrayList<String>(Right);
		}
		
		// initialze LCS algorithm 
		this.lcs = new LongestCommonSubseq(this.toLeft, this.toRight);
		this.compare_len = 0;
		
		
		// if the length of LCS is not 0,
		// which means there is something different between two strings.
		// then, we need to analyze how they are different 
		if (this.lcs.length() != 0) {
			
			this.left_match = ArrayList<Integer>();
			this.right_match = ArrayList<Integer>();
			this.left_change = ArrayList<Node>();
			this.right_change = ArrayList<Node>();
		
			// need to do more 
		
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
}