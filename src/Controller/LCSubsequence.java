package Controller;

import java.util.*;
import Model.*;

public class LCSubsequence {
	public static LinkedList<Node> result;
	
	public static LinkedList<Node> getDiff(String a, String b) {
		result = new LinkedList<Node>();
		result.push(new Node(null, 0, 0, Node.DUMMY));
		
		int[][] lengths = new int[a.length() + 1][b.length() + 1];
		// row 0 and column 0 are initialized to 0 already

		for (int i = 0; i < a.length(); i++)
			for (int j = 0; j < b.length(); j++)
				if (a.charAt(i) == b.charAt(j))
					lengths[i + 1][j + 1] = lengths[i][j] + 1;
				else
					lengths[i + 1][j + 1] = Math.max(lengths[i + 1][j], lengths[i][j + 1]);

		// read the substring out from the matrix
		StringBuffer sb = new StringBuffer();
		
		for (int x = a.length(), y = b.length(); x != 0 && y != 0;) {
			if (lengths[x][y] == lengths[x - 1][y]) {
				// add right text character in node
				if (y == result.peek().rightIndex && result.peek().flag == Node.DELETE) {
					// already node exist in same right text index
					result.peek().leftIndex = x - 1;
					result.peek().addChar(a.charAt(x - 1));
				} else {
					result.push(new Node(new StringBuffer(String.valueOf(a.charAt(x - 1))), x - 1, y, Node.DELETE));
				}
				x--;
			} else if (lengths[x][y] == lengths[x][y - 1]) {
				// add left text character in node
				if (x == result.peek().leftIndex && result.peek().flag == Node.ADD) {
					// already node exist in same left text index
					result.peek().rightIndex = y - 1;
					result.peek().addChar(b.charAt(y - 1));
				} else {
					result.push(new Node(new StringBuffer(String.valueOf(b.charAt(y - 1))), x, y - 1, Node.ADD));
				}
				y--;
			} else {
				assert a.charAt(x - 1) == b.charAt(y - 1);
				sb.append(a.charAt(x - 1));
				x--;
				y--;
			}
		}

//		while (!result.isEmpty()) {
//			if (result.peek().flag == Node.DUMMY) {
//				result.pop();
//				break;
//			}
//			if (result.peek().flag == Node.DELETE) {
//
//				Node e = result.pop();
//
//				System.out.println(e.leftIndex + "," + (e.leftIndex + e.context.length()) + "d" + e.rightIndex);
//				System.out.println("> \"" + a.substring(e.leftIndex, (e.leftIndex + e.context.length())) + "\"");
//
//			} else if (result.peek().flag == Node.ADD) {
//
//				Node e = result.pop();
//
//				System.out.println(e.leftIndex + "a" + e.rightIndex + "," + (e.rightIndex + e.context.length()));
//				System.out.println("< \"" + b.substring(e.rightIndex, (e.rightIndex + e.context.length())) + "\"");
//
//			}
//		}

		return result;
//		return sb.reverse().toString();
		

	}

	public static LinkedList<Node> getResult() {
		return result;
	}

	public static void setResult(LinkedList<Node> result) {
		LCSubsequence.result = result;
	}
	
	
}
