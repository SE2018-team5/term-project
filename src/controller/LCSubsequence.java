package controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import model.MatchNode;
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
				diffMap.put(n, new Node(new StringBuffer(), n.leftIndex, -1, Node.DUMMY));
			}
			
			// make dummy on right
			if(n.flag == Node.DELETE) {
				
			}
		}
		
		
		return null;
	}
	public static LinkedList<Node> getDiff(String a, String b) {
		result = new LinkedList<Node>();
		result.push(new Node(new StringBuffer(), 0, 0, Node.DUMMY));
		
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
					y--;
				}
			} else {
				assert a.charAt(x - 1) == b.charAt(y - 1);
				sb.append(a.charAt(x - 1));
				x--;
				y--;
			}
		}
 				
		result.pollLast();	// remove dummy node

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
		

		// make node word by word
		for(Node n : result) {
			setNodeByWord(a, b, n);
		}
		
//		changedResult = new HashMap<>();
//		LinkedList<MatchNode> matchResult = new LinkedList();
		LinkedList<Node> ret = new LinkedList();
		
		// find changed words
		for(Node n1 : result) {
			if (n1.flag == Node.ADD) {
				ret.add(n1);
				Node left = null;
				
//				for(Node n2 : result) {
//					if(n2.flag == Node.DELETE && n2.leftIndex == n1.leftIndex) {
//						left = n2;
//						continue;
//					}
//				}
				if(ret.contains(left)) 
					continue;
				
				if(left != null) {
					ret.add(left);
//					matchResult.add(new MatchNode(left, n1));
				} else {
					ret.add(new Node(new StringBuffer(), n1.leftIndex, -1, Node.DUMMY));
//					matchResult.add(new MatchNode(left, n1));
				}
				
//				// if n1 is not changed node
//				if(!changedResult.containsKey(n1)) {
//					changedResult.put(n1, new Node(new StringBuffer(), n1.leftIndex, -1, Node.DUMMY));
//					continue;
//				}
			}
			
			if(n1.flag == Node.DELETE) {
				Node right = null;
				ret.add(n1);
//				for(Node n2 : result) {
//					if(n2.flag == Node.ADD && n2.rightIndex == n1.rightIndex) {
//						right = n2;
//						continue;
//					}
//				}
				
				if(ret.contains(right)) 
					continue;
				
				if(right != null) {
					ret.add(right);
//					matchResult.add(new MatchNode(n1, right));
				} else {
					ret.add(new Node(new StringBuffer(), -1, n1.rightIndex, Node.DUMMY));
//					right = new Node(new StringBuffer(), -1, n1.rightIndex, Node.DUMMY);
//					matchResult.add(new MatchNode(n1, right));
				}
				
				
				
//				// if n1 is not changed node
//				if(!changedResult.containsValue(n1)) {
//					changedResult.put(new Node(new StringBuffer(), -1, n1.rightIndex, Node.DUMMY), n1);
//					continue;
//				}
			}
			
//			if(changedResult.containsKey(n1) || changedResult.containsValue(n1))
//				continue;
			
			/*if (n1.flag == Node.ADD) {
				for(Node n2 : result) {
					if(n2.flag == Node.DELETE && n2.leftIndex == n1.leftIndex) {
						changedResult.put(n1, n2);
						continue;
					}
				}
				
				// if n1 is not changed node
				if(!changedResult.containsKey(n1)) {
					changedResult.put(n1, new Node(new StringBuffer(), n1.leftIndex, -1, Node.DUMMY));
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
				if(!changedResult.containsValue(n1)) {
					changedResult.put(new Node(new StringBuffer(), -1, n1.rightIndex, Node.DUMMY), n1);
					continue;
				}
			}*/
		}
		
//		LinkedList<Node> ret = new LinkedList<>();
//		for (Map.Entry<Node, Node> entry : changedResult.entrySet()) {
//			ret.add(entry.getKey());
//			ret.add(entry.getValue());
//		}
		
//		// print out nodes that CHANGED
//		for(Map.Entry<Node, Node> entry: changedResult.entrySet()) {
//			Node key = entry.getKey();
//			Node value = entry.getValue();
//			
//			System.out.println(key.toString());
//			System.out.println(value.toString());
//			System.out.println("============================================== before word");
//		}
		
		

//		for(Map.Entry<Node, Node> entry : changedResult.entrySet()) {
//			Node key = entry.getKey();
//			Node value = entry.getValue();
//			
//			Node dummy = null;
//			if(key.flag == Node.DUMMY) {
//				dummy = key;
//			} else if (value.flag == Node.DUMMY) {
//				dummy = value;
//			}
//			
//			if(dummy == null)
//				continue;
//			
//			setDummyNodeByWord(a, b, entry);
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
				
		return ret;
//		return matchResult;
//		return result;
//		return sb.reverse().toString();
		

	}
	
	private static void setNodeByWord(String text1, String text2, Node node) {
		int wordStart, wordEnd; // [wordStart, wordEnd)
		char c;
		
		// if node is added
		if(node.flag == Node.ADD) {
			
			// separate by words
			// scan front-side
			wordStart = node.rightIndex;
			// check if rightIndex is zero
			if (wordStart < 0 || wordStart == text2.length()) {
				return;
			}
			c = text2.charAt(wordStart);
			if(c != ' ' && c != '\n' && wordStart != 0) {
				c = text2.charAt(wordStart - 1);
				while (c != ' ' && c != '\n' && wordStart != 0) {
					node.addChar(c);
					wordStart--;

					if(wordStart == 0) {
						break;
					} else {
						c = text2.charAt(wordStart - 1);
					}
				}	
			}
			
			// scan back-side
			wordEnd = wordStart + node.context.length() - 1;
			
			// check if wordEnd is out of index
			if (wordEnd == text2.length() - 1) {
				return;
			}
			
			c = text2.charAt(wordEnd);
			if(c != ' ' && c != '\n') {
				c = text2.charAt(wordEnd + 1);
				while (c != ' ' && c != '\n' && wordEnd != text2.length() - 1) {
					node.context.append(c);
					wordEnd++;
					
					if(wordEnd + 1 == text2.length()) {
						break;
					} else {
						c = text2.charAt(wordEnd + 1);
					}
				}
			}
			node.rightIndex = wordStart;
		}
		
		// if node is deleted 
		if(node.flag == Node.DELETE) {
			
			// separate by words
			
			// scan front-side
			wordStart = node.leftIndex;
			
			// check if leftIndex is zero
			if (wordStart < 0 || wordStart == text2.length()) {
				return;
			}
			
			c = text1.charAt(wordStart);
			if(c != ' ' && c != '\n' && wordStart != 0) {
				c = text1.charAt(wordStart - 1);
				while (c != ' ' && c != '\n') {
					node.addChar(c);
					wordStart--;
					
					if(wordStart == 0) {
						break;
					} else {
						c = text1.charAt(wordStart - 1);
					}
						
				}	
			}
			
			// scan back-side
			wordEnd = wordStart + node.context.length() - 1;
			// check if wordEnd is out of index
			if (wordEnd == text1.length() - 1) {
				return;
			}
			
			c = text1.charAt(wordEnd);
			if(c != ' ' && c != '\n') {
				c = text1.charAt(wordEnd + 1);
				while (c != ' ' && c != '\n' && wordEnd != text1.length() - 1) {
					node.context.append(c);
					wordEnd++;
					
					if(wordEnd + 1 == text1.length()) {
						break;
					} else {
						c = text1.charAt(wordEnd + 1);
					}
				}
			}
			
			node.leftIndex = wordStart;
		}
	}
	
	private static void setDummyNodeByWord(String text1, String text2, Map.Entry<Node, Node> entry) {
		int wordStart, wordEnd; // [wordStart, wordEnd)
		char c1, c2;
		Node key = entry.getKey();		// right
		Node value = entry.getValue();	// left
		
		Node node = null;
		if(key.flag == Node.DUMMY) {
			// left index is -1
			node = key;
		} else if (value.flag == Node.DUMMY) {
			// right index is -1
			node = value;
		}
		
		if(node == null)
			return;
		
		assert node.flag == Node.DUMMY;
		
		// if node is added
		if(node.leftIndex == -1) {
			// separate by words
			// scan front-side
			wordStart = node.rightIndex - 1;
			// check if rightIndex is zero
			if (wordStart < 0 || wordStart == text2.length()) {
				return;
			}
			c1 = text2.charAt(wordStart);
			c2 = text1.charAt(value.leftIndex);
			
			if(c2 != ' ' && c2 != '\n' && wordStart != 0) {
				if(c1 != ' ' && c1 != '\n') {
					c1 = text2.charAt(wordStart - 1);
					while (c1 != ' ' && c1 != '\n' && wordStart != 0) {
						node.addChar(c1);
						wordStart--;

						if(wordStart == 0) {
							break;
						} else {
							c1 = text2.charAt(wordStart - 1);
						}
					}	
				}
			}
			if(wordStart == 0) {
				node.addChar(c1);
			}
						
			// scan back-side
			wordEnd = node.rightIndex - 1;
			
			// check if wordEnd is out of index
			if (wordEnd == text2.length() - 1 && wordEnd != 0) {
				return;
			}
			
			c1 = text2.charAt(wordEnd);
			if(c1 != ' ' && c1 != '\n') {
				c1 = text2.charAt(wordEnd + 1);
				while (c1 != ' ' && c1 != '\n' && wordEnd != text2.length() - 1) {
					node.context.append(c1);
					wordEnd++;
					
					if(wordEnd + 1 == text2.length()) {
						break;
					} else {
						c1 = text2.charAt(wordEnd + 1);
					}
				}
			}
			node.rightIndex = wordStart;
		}
		
		// if node is deleted 
		if(node.rightIndex == -1) {
			
			// separate by words
			
			// scan front-side
			wordStart = node.leftIndex - 1;
			
			// check if leftIndex is zero
			if (wordStart < 0 || wordStart == text2.length()) {
				return;
			}
			
			c1 = text1.charAt(wordStart);
			c2 = text2.charAt(key.rightIndex);
			if(c2 != ' ' && c2 != '\n') {
				if(c1 != ' ' && c1 != '\n') {
					c1 = text1.charAt(wordStart - 1);
					while (c1 != ' ' && c1 != '\n') {
						node.addChar(c1);
						wordStart--;
						
						if(wordStart == 0) {
							break;
						} else {
							c1 = text1.charAt(wordStart - 1);
						}
							
					}	
				}
			}
			
			if(wordStart == 0) {
				node.addChar(c1);
			}
			
			// scan back-side
			wordEnd = node.leftIndex - 1;
			// check if wordEnd is out of index
			if (wordEnd == text1.length() - 1) {
				return;
			}
			
			c1 = text1.charAt(wordEnd);
			if(c1 != ' ' && c1 != '\n') {
				c1 = text1.charAt(wordEnd + 1);
				while (c1 != ' ' && c1 != '\n' && wordEnd != text1.length() - 1) {
					node.context.append(c1);
					wordEnd++;
					
					if(wordEnd + 1 == text1.length()) {
						break;
					} else {
						c1 = text1.charAt(wordEnd + 1);
					}
				}
			}
			
			node.leftIndex = wordStart;
		}
	}
	
}