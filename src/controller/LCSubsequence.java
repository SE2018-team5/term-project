package controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import model.Node;

public class LCSubsequence {
	public static LinkedList<Node> result;
	public static HashMap<Node,Node> changedResult;

	// mapping node and dummy node
	public static HashMap<Node, Integer> getMergeMap(LinkedList<Node> diff){
		HashMap<Node, Node> diffMap = new HashMap<Node, Node>();
		
		for(Node n : diff) {
			// make dummy on left
			if(n.flag == Node.ADD) {
				diffMap.put(n, new Node(null, n.leftIndex, -1, Node.DUMMY));
			}
			
			// make dummy on right
			if(n.flag == Node.DELETE) {
				
			}
		}
		
		
		return null;
	}
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
		
		int x = 0, y = 0;
 		for (x = a.length(), y = b.length(); x > 0 && y > 0;) {
			if (lengths[x][y] == lengths[x - 1][y]) {
				// add right text character in node
				if (y == result.peek().rightIndex && result.peek().flag == Node.DELETE) {
					// already node exist in same right text index
						result.peek().leftIndex = x - 1;
						result.peek().addChar(a.charAt(x - 1));
						x--;
				} else {
					result.push(new Node(new StringBuffer(a.substring(x-1, x)), x - 1, y, Node.DELETE));
//					int rightidx = x - 1;
//					while(a.charAt(x - 1) != '\n') {
//						result.peek().leftIndex = x - 1;
//						result.peek().addChar(a.charAt(x - 1));
//						x--;
//					}
//					while(a.charAt(rightidx) != '\n') {
//						result.peek().context.append(a.charAt(rightidx));
//						rightidx++;
//					}
					x--;
				}
			} else if (lengths[x][y] == lengths[x][y - 1]) {
				
				// add left text character in node
				if (x == result.peek().leftIndex && result.peek().flag == Node.ADD) {
					// already node exist in same left text index
						result.peek().rightIndex = y - 1;
						result.peek().addChar(b.charAt(y - 1));
						y--;
				} else {
					result.push(new Node(new StringBuffer(b.substring(y-1, y)), x, y - 1, Node.ADD));
//					int leftidx = y - 1;
//					while(b.charAt(y - 1) != '\n'){
//						result.peek().rightIndex = y - 1;
//						result.peek().addChar(b.charAt(y - 1));
//						y--;
//					}
//					while(b.charAt(leftidx) != '\n') {
//						result.peek().context.append(b.charAt(leftidx + 1));
//						leftidx++;
//					}
					y--;
				}
			} else {
				assert a.charAt(x - 1) == b.charAt(y - 1);
				sb.append(a.charAt(x - 1));
				x--;
				y--;
			}
		}
 		
 		// make the node for rest of *left string*
		while(x > 0) {
			// add left text character in node
			if (y == result.peek().rightIndex && result.peek().flag == Node.DELETE) {
				// already node exist in same right text index
					result.peek().leftIndex = x - 1;
					result.peek().addChar(a.charAt(x - 1));
					x--;
			} else {
				result.push(new Node(new StringBuffer(a.substring(x-1, x)), x-1, y, Node.DELETE));
				x--;
			}
		}
		
		// make the node for rest of *right string*
		while(y > 0) {
			// add left text character in node
			if (x == result.peek().leftIndex && result.peek().flag == Node.ADD) {
				// already node exist in same left text index
					result.peek().rightIndex = y - 1;
					result.peek().addChar(b.charAt(y - 1));
					y--;
			} else {
				result.push(new Node(new StringBuffer(b.substring(y-1, y)), x, y - 1, Node.ADD));
				y--;
			}
		}
		
		// print out node char by char 
		for(Node n : result) {
			System.out.println(n.toString());
		}
		
		changedResult = new HashMap<>();
		
		// find changed words
		for(Node n1 : result) {
			if(changedResult.containsKey(n1) || changedResult.containsValue(n1))
				continue;
			
			if (n1.flag == Node.ADD) {
				for(Node n2 : result) {
					if(n2.flag == Node.DELETE && n2.leftIndex == n1.leftIndex) {
						changedResult.put(n1, n2);
						continue;
					}
				}
				
				// if n1 is not changed node
				if(!changedResult.containsKey(n1)) {
					changedResult.put(n1, new Node(null, n1.leftIndex, -1, Node.DUMMY));
					continue;
				}
			}
			
			if(n1.flag == Node.DELETE) {
				for(Node n2 : result) {
					if(n2.flag == Node.ADD && n2.rightIndex == n1.rightIndex) {
						changedResult.put(n2, n1);
						continue;
					}
				}
				
				// if n1 is not changed node
				if(changedResult.containsValue(n1)) {
					changedResult.put(new Node(null, -1, n1.rightIndex, Node.DUMMY), n1);
					continue;
				}
			}
		}
		
//		// print out nodes that CHANGED
//		for(Map.Entry<Node, Node> entry: changedResult.entrySet()) {
//			Node key = entry.getKey();
//			Node value = entry.getValue();
//			
//			System.out.println(key.toString());
//			System.out.println(value.toString());
//			System.out.println("============================================== before word");
//		}
		
		
		
		// make node word by word
		for(Node n : result) {
			int wordStart, wordEnd; // [wordStart, wordEnd)
			char c;
			
			// if node is added
			if(n.flag == Node.ADD) {
				
				// separate by words
				// scan front-side
				wordStart = n.rightIndex;
				// check if rightIndex is zero
				if (wordStart == 0) {
					continue;
				}
				c = b.charAt(wordStart);
				if(c != ' ' && c != '\n') {
					c = b.charAt(wordStart - 1);
					while (c != ' ' && c != '\n' && wordStart != 0) {
						n.addChar(c);
						wordStart--;

						if(wordStart == 0) {
							break;
						} else {
							c = b.charAt(wordStart - 1);
						}
					}	
				}
				
				// scan back-side
				wordEnd = wordStart + n.context.length() - 1;
				
				// check if wordEnd is out of index
				if (wordEnd == b.length() - 1) {
					continue;
				}
				
				c = b.charAt(wordEnd);
				if(c != ' ' && c != '\n') {
					c = b.charAt(wordEnd + 1);
					while (c != ' ' && c != '\n' && wordEnd != b.length() - 1) {
						n.context.append(c);
						wordEnd++;
						
						if(wordEnd + 1 == b.length()) {
							break;
						} else {
							c = b.charAt(wordEnd + 1);
						}
					}
				}
				n.rightIndex = wordStart;
			}
			
			
			// if node is deleted 
			if(n.flag == Node.DELETE) {
				
				// separate by words
				
				// scan front-side
				wordStart = n.leftIndex;
				
				// check if leftIndex is zero
				if (wordStart == 0) {
					continue;
				}
				
				c = a.charAt(wordStart);
				if(c != ' ' && c != '\n') {
					c = a.charAt(wordStart - 1);
					while (c != ' ' && c != '\n') {
						n.addChar(c);
						wordStart--;
						
						if(wordStart == 0) {
							break;
						} else {
							c = a.charAt(wordStart - 1);
						}
							
					}	
				}
				
				
				// scan back-side
				wordEnd = wordStart + n.context.length() - 1;
				// check if wordEnd is out of index
				if (wordEnd == a.length() - 1) {
					continue;
				}
				
				c = a.charAt(wordEnd);
				if(c != ' ' && c != '\n') {
					c = a.charAt(wordEnd + 1);
					while (c != ' ' && c != '\n' && wordEnd != a.length() - 1) {
						n.context.append(c);
						wordEnd++;
						
						if(wordEnd + 1 == a.length()) {
							break;
						} else {
							c = a.charAt(wordEnd + 1);
						}
					}
				}
				
				n.leftIndex = wordStart;
			}
		}
		

//		for(Map.Entry<Node, Node> entry : changedResult.entrySet()) {
//			Node key = entry.getKey();
//			Node value = entry.getValue();
//			
//			if(key.flag == Node.DUMMY) {
//				// separate by words
//				// scan front-side
//				boolean isLeft = n.leftIndex != -1? true : false;
//				wordStart = n.leftIndex != -1? key.leftIndex : key.rightIndex;
//				// check if rightIndex is zero
//				if (wordStart == 0) {
//					continue;
//				}
//				
//				c = b.charAt(wordStart);
//				if(c != ' ' && c != '\n') {
//					c = b.charAt(wordStart - 1);
//					while (c != ' ' && c != '\n') {
//						key.addChar(c);
//						wordStart--;
//						
//						c = b.charAt(wordStart - 1);
//					}	
//				}
//				
//				// scan back-side
//				wordEnd = wordStart + n.context.length() - 1;
//				
//				// check if wordEnd is out of index
//				if (wordEnd == b.length() - 1) {
//					continue;
//				}
//				
//				c = b.charAt(wordEnd);
//				if(c != ' ' && c != '\n') {
//					c = b.charAt(wordEnd + 1);
//					while (c != ' ' && c != '\n') {
//						n.context.append(c);
//						wordEnd++;
//						
//						c = b.charAt(wordEnd + 1);
//					}
//				}
//				n.rightIndex = wordStart;
//			}
//		}
		
		
//		// print out nodes that CHANGED
//    	for(Map.Entry<Node, Node> entry: changedResult.entrySet()) {
//    		Node key = entry.getKey();
//    		Node value = entry.getValue();
//    		
//    		System.out.println(key.toString());
//    		System.out.println(value.toString());
//    		System.out.println("============================================== after word");
//    	}
				
		return result;
//		return sb.reverse().toString();
		

	}
	
}
