package junit;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.LCSubsequence;
import model.Node;

class testLCSubsequence {
	String[] text1;
	String[] text2;

	ArrayList<LinkedList<Node>> test;
	Node[][] answer;

	@BeforeEach
	void setUp() throws Exception {
		text1 = new String[] { "abcd", "bcde", "cdef", "defg", "efgh" };
		text2 = new String[] { "abcd", "bcde", "cdef", "defg", "efgh" };
		
		answer = new Node[][] { {}, { new Node("a", 2), new Node("e", 1) },
	        { new Node("ab", 2), new Node("ef", 1) }, { new Node("abc", 2), new Node("efg", 1) },
	        { new Node("a", 1), new Node("e", 2) }, {}, { new Node("b", 2), new Node("f", 1) },
	        { new Node("bc", 2), new Node("fg", 1) }, { new Node("ab", 1), new Node("ef", 2) },
	        { new Node("b", 1), new Node("f", 2) }, {}, { new Node("c", 2), new Node("g", 1) },
	        { new Node("abc", 1), new Node("efg", 2) }, { new Node("bc", 1), new Node("fg", 2) },
	        { new Node("c", 1), new Node("g", 2) }, {} };

		test = new ArrayList<LinkedList<Node>>();
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				test.add(new LinkedList<Node>(Arrays.asList(answer[i * 4 + j])));
			}
		}

	};

	@Test
	void test() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				LinkedList<Node> temp = LCSubsequence.getDiff(text1[i], text2[j]);
				for(Node element : test.get(i * 4 + j)) {
					if(temp.peekLast().flag != Node.DUMMY) {
						assertEquals(element.toString(), temp.pollLast().toString());	
					}
				}
			}
		}
	}

}
