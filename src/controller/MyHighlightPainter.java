package controller;

import java.awt.Color;

import javax.swing.*;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

public class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {

		public MyHighlightPainter(Color color) {
			super(color);
			
		}
		
		Highlighter.HighlightPainter myHighlightPainter = new MyHighlightPainter (Color.YELLOW);
		
		public void highlight (JTextField textComp, String pattern, int index, int length) {
			try{
				Highlighter hilite = textComp.getHighlighter();
				Document doc = textComp.getDocument();
				String text = doc.getText(index, length);
				int pos = index;
				
				while((pos = text.toUpperCase().indexOf(pattern.toUpperCase(),pos)) >= 0) {
					hilite.addHighlight(pos, pos+pattern.length(), myHighlightPainter);
					pos += pattern.length();
				}
				
			}catch(Exception e){
				
				
			}
			
			
		}
	}

