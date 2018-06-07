package controller;

import java.util.HashMap;
import java.util.LinkedList;
import model.Node;

public class LCSubsequence {
	public static LinkedList<Node> result;
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
					// already node exist in same right text index`
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
		
		for (Node n1 : result) {
			System.out.println(n1);
		}
		
		System.out.println("before word");
		
		LinkedList<Node> ret = new LinkedList();
		
		// find changed words
		for(Node n1 : result) {
			if (n1.flag == Node.ADD) {
				Node left = null;
				if(ret.contains(n1))
					continue;
				else 
					ret.add(n1);
				
				for(Node n2 : result) {
					if(n2.flag == Node.DELETE && n2.leftIndex == n1.leftIndex) {
						left = n2;
						continue;
					}
				}
				
				if(ret.contains(left)) 
					continue;
				
				if(left != null) {
					ret.add(left);
				} else {
					// need dummy node
					ret.add(new Node(new StringBuffer(), n1.leftIndex, -1, Node.DUMMY));
				}
				
			}
			
			if(n1.flag == Node.DELETE) {
				Node right = null;
				if(ret.contains(n1))
					continue;
				else 
					ret.add(n1);
				
				for(Node n2 : result) {
					if(n2.flag == Node.ADD && n2.rightIndex == n1.rightIndex) {
						right = n2;
						break;
					}
				}
				
				// already exist
				if(ret.contains(right)) 
					continue;
				
				if(right != null) {
					ret.add(right);
				} else {
					ret.add(new Node(new StringBuffer(), -1, n1.rightIndex, Node.DUMMY));
				}
			}			
		}

		// make node word by word
		for(Node n : ret) {
			setNodeByWord(a, b, n);
		}
		
		// make node word by word
		for(Node n : ret) {
			setDummyNodeByWord(a, b, n);
		}
		
		// print out node char by char 
		for(Node n : result) {
			System.out.println(n.toString());
		}
		
		
		System.out.print("A\n");
		
		for (Node n : ret) {
			System.out.println(n);
			System.out.println(".....");
		}
		return ret;


	}
	
	private static void setNodeByWord(String text1, String text2, Node node) {
	    int wordStart = 0, wordEnd = 0; // [wordStart, wordEnd)
	    String text = null;
        char c;
        
        // separate by words
        
        
        if(node.flag == Node.ADD) {
            wordStart = node.rightIndex;
            text = text2;
        }
            
        if(node.flag == Node.DELETE) {
            wordStart = node.leftIndex;
            text = text1;
        }
        
        // DUMMY node do nothing
        if(node.flag == Node.DUMMY) {
            return;
        }
        
        assert wordStart == node.rightIndex || wordStart == node.leftIndex;
        assert text != null;
        
        // scan front-side 
        // check if start index is zero
        if (wordStart < 0 || wordStart == text.length()) {
            return;
        }
        c = text.charAt(wordStart);
        if(c != ' ' && c != '\n' && c != '\t' && wordStart != 0) {
        	wordStart--;
            c = text.charAt(wordStart);
            while (c != ' ' && c != '\n' && wordStart >= 0) {
                node.addChar(c);
                wordStart--;

                if(wordStart == -1) {
                	wordStart = 0;
                    break;
                } else {
                    c = text.charAt(wordStart);
                }
            }   
        }
        
        // scan back-side
        wordEnd = wordStart + node.context.length() - 1;
        
        // check if wordEnd is out of index
        if (wordEnd == text.length()) {
        	// set new index
    		if(node.leftIndex == -1) {
                node.rightIndex = wordStart;
            }
            if(node.rightIndex == -1) {
                node.leftIndex = wordStart;
            }
            return;
        }
        
        c = text.charAt(wordEnd);
        if(c != ' ' && c != '\n' && c != '\t' ) {
            c = text.charAt(wordEnd + 1);
            while (c != ' ' && c != '\n'  && c != '\t' && wordEnd != text.length() - 1) {
                node.context.append(c);
                wordEnd++;
                
                if(wordEnd + 1 == text.length()) {
                    break;
                } else {
                    c = text.charAt(wordEnd + 1);
                }
            }
        }

        if(node.flag == Node.ADD)
            node.rightIndex = wordStart;
        if(node.flag == Node.DELETE)
            node.leftIndex  = wordStart;
	    
	}
	
	private static void setDummyNodeByWord(String text1, String text2, Node node) {
		int wordStart = 0, wordEnd = 0; // [wordStart, wordEnd)
		String text = null;
		char c1, c2;
		
		
		if(node.flag != Node.DUMMY)
			return;
		
		// init variables
		if(node.leftIndex == -1) {
		    wordStart = node.rightIndex;
		    wordEnd   = node.rightIndex;
		    
		    text = text2;
		}
		if(node.rightIndex == -1) {
		    wordStart = node.leftIndex;
            wordEnd   = node.leftIndex;
            
		    text = text1;
		}
		
		assert (wordStart == node.leftIndex || wordStart == node.rightIndex);

		// scan front-side
		// check if start index is zero
		if (wordStart <= 0 || wordStart == text.length()) {
			return;
		}
		
		// dummy on start
		c1 = text.charAt(wordStart);
		if(c1 == ' ' || c1 == '\n' || c1 == '\t')
			return;
		
		c1 = text.charAt(wordStart - 1);
		if(c1 != ' ' && c1 != '\n' && c1 != '\t') {
			wordStart--;
            c1 = text.charAt(wordStart);
			while (c1 != ' ' && c1 != '\n' && c1 != '\t' && wordStart >= 0 ) {
				node.addChar(c1);
				wordStart--;

				if(wordStart == -1) {
					wordStart = 0;
					break;
				} else {
					c1 = text.charAt(wordStart);
				}
			}	
		} else {
			return;
		}
					
		// scan back-side
		// check if wordEnd is out of index
		if (wordEnd == text.length()) {
			// set new index
			if(node.leftIndex == -1) {
	            node.rightIndex = wordStart;
	        }
	        if(node.rightIndex == -1) {
	            node.leftIndex = wordStart;
	        }
			return;
		}
		
		c1 = text.charAt(wordEnd);
		while (c1 != ' ' && c1 != '\n' && c1 != '\t'&& wordEnd < text.length()) {
			node.context.append(c1);
			wordEnd++;
			
			if(wordEnd == text.length()) {
				break;
			} else {
				c1 = text.charAt(wordEnd);
			}
		}
		
		// set new index
		if(node.leftIndex == -1) {
            node.rightIndex = wordStart;
        }
        if(node.rightIndex == -1) {
            node.leftIndex = wordStart;
        }
        
	}
}