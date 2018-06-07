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
	String text1;
	String text2;

	LinkedList<Node> test;
	
	Node answer1;
	Node answer2;
	Node answer3;
	Node answer4;
	Node answer5;
	Node answer6;
	@BeforeEach
	void setUp() throws Exception {
		text1 = new String("abcd\n" + 
				"a\n" + 
				"a\n" + 
				"c\n" + 
				"\n" + 
				"d\n" + 
				"b\n" + 
				"d" + " abcdefg");
		text2 = new String("abdd\n" + 
				"a\n" + 
				"a\n" + 
				"d\n" + 
				"b\n" + 
				"d" + "abcdefg");
		
		answer1 = new Node("abcd", 2); // DELETE = 2
		answer2 = new Node("abdd", 1); // ADD = 1
		answer3 = new Node("c\n\n", 2);
		answer4 = new Node("", 2);
		answer5 = new Node(" ", 1);
		answer6 = new Node("dabcdefg", 1);
		
		test = LCSubsequence.getDiff(text1, text2);
		

	};

	@Test
	void test() throws Exception {
		setUp();
		assertEquals(answer1.context.toString(), test.poll().context.toString());
		assertEquals(answer2.context.toString(), test.poll().context.toString());
		assertEquals(answer3.context.toString(), test.poll().context.toString());
		assertEquals(answer4.context.toString(), test.poll().context.toString());
		assertEquals(answer5.context.toString(), test.poll().context.toString());
		assertEquals(answer6.context.toString(), test.poll().context.toString());
	}

}