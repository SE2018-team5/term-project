package controller;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;

import model.MainWindowModel;
import model.Node;
import model.StringBufferModel;
import view.MainWindowView;

public class MainWindowController {

	MainWindowView view;
	MainWindowModel model;

	EditPanelController leftController;
	EditPanelController rightController;
	StringBufferModel leftBuffer;
	StringBufferModel rightBuffer;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindowView v = new MainWindowView();
					MainWindowModel m = new MainWindowModel();
					MainWindowController controller = new MainWindowController(v, m);
					v.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	MainWindowController(MainWindowView v, MainWindowModel m) {
		this.view = v;
		this.model = m;

		leftController = new EditPanelController(view.getLeftPanel(), m);
		rightController = new EditPanelController(view.getRightPanel(), m);
		this.view.getMergePanel().addCmpActionListener(new CmpActionListener());
		this.view.getMergePanel().addCopyToLeftActionListener(new CopyToLeftActionListener());
		this.view.getMergePanel().addCopyToRightActionListener(new CopyToRightActionListener());
		
		leftBuffer = new StringBufferModel(leftController.getEditPanel().getContent());
		rightBuffer = new StringBufferModel(rightController.getEditPanel().getContent());
		//Set Left/Right StringBuffer
	}

	class CmpActionListener implements ActionListener {

		javax.swing.text.DefaultHighlighter.DefaultHighlightPainter highlightPainter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(
				Color.RED);

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			LCSubsequence l = new LCSubsequence();
			
//			leftController.setStringBuffer(leftController.getEditPanel().getContent());
//			rightController.setStringBuffer(rightController.getEditPanel().getContent());
			
			leftBuffer.setStringBuffer(leftController.getEditPanel().getContent());
			rightBuffer.setStringBuffer(rightController.getEditPanel().getContent());
			
			String text1 = leftBuffer.getStringBuffer().toString();
			String text2 = rightBuffer.getStringBuffer().toString();
			
			LinkedList<Node> r = LCSubsequence.getDiff(leftBuffer.getStringBuffer().toString(),rightBuffer.getStringBuffer().toString());
			LinkedList<Node> processedResult = new LinkedList<>();
			
			for(Node n : r) {
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
					c = text2.charAt(wordStart);
					if(c != ' ' && c != '\n') {
						c = text2.charAt(wordStart - 1);
						while (c != ' ' && c != '\n') {
							n.addChar(c);
							wordStart--;
							
							c = text2.charAt(wordStart - 1);
						}	
					}
					
					// scan back-side
					wordEnd = wordStart + n.context.length() - 1;
					
					// check if wordEnd is out of index
					if (wordEnd == text2.length() - 1) {
						continue;
					}
					
					c = text2.charAt(wordEnd);
					if(c != ' ' && c != '\n') {
						c = text2.charAt(wordEnd + 1);
						while (c != ' ' && c != '\n') {
							n.context.append(c);
							wordEnd++;
							
							c = text2.charAt(wordEnd + 1);
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
					
					c = text1.charAt(wordStart);
					if(c != ' ' && c != '\n') {
						c = text1.charAt(wordStart - 1);
						while (c != ' ' && c != '\n') {
							n.addChar(c);
							wordStart--;
							
							c = text1.charAt(wordStart - 1);
						}	
					}
					
					
					// scan back-side
					wordEnd = wordStart + n.context.length() - 1;
					// check if wordEnd is out of index
					if (wordEnd == text1.length() - 1) {
						continue;
					}
					
					c = text1.charAt(wordEnd);
					if(c != ' ' && c != '\n') {
						c = text1.charAt(wordEnd + 1);
						while (c != ' ' && c != '\n') {
							n.context.append(c);
							wordEnd++;
							
							c = text1.charAt(wordEnd + 1);
						}
					}
					
					n.leftIndex = wordStart;
				}
			}
			
			System.out.println(leftBuffer.getStringBuffer().toString());
			System.out.println(rightBuffer.getStringBuffer().toString());
			
			for (Node e1 : r) {
				if (e1.flag == Node.DELETE) {
					System.out.println("DELETE\n");
					try {
						leftController.getEditPanel().getEditorPane().getHighlighter().
								addHighlight(e1.leftIndex, e1.leftIndex + e1.context.length(), highlightPainter);
					} catch (BadLocationException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				}
				if (e1.flag == Node.ADD) {
					System.out.println("ADD\n");
					try {
						rightController.getEditPanel().getEditorPane().getHighlighter().
								addHighlight(e1.rightIndex, e1.rightIndex + e1.context.length(), highlightPainter);
					} catch (BadLocationException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				}
				System.out.println(e1.toString());
			}
			MainWindowModel.compared();
			MainWindowModel.highlighted(); 
		}
	}

	class CopyToLeftActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			MainWindowModel.setIsHighlighted(false);
			
			if (!MainWindowModel.getIsCompared()) {
				int answer = JOptionPane.showConfirmDialog(null, "Files are not compared yet. Still want to merge?",
						"Warning", JOptionPane.WARNING_MESSAGE);
				if (answer == JOptionPane.OK_OPTION) {
					String str = rightController.getEditPanel().getContent();
		            leftController.getEditPanel().setContent(str);
		            MainWindowModel.setIsCompared(false);
				}
			}else if (MainWindowModel.getIsCompared()) {
				MainWindowModel.setIsCompared(false);
				int answer = JOptionPane.showConfirmDialog(null, "Hightlighted words will be initialized, Still want to merge?",
						"Warning", JOptionPane.WARNING_MESSAGE);
				if (answer == JOptionPane.OK_OPTION) {
					String str = rightController.getEditPanel().getContent();
		            leftController.getEditPanel().setContent(str);
		            MainWindowModel.setIsCompared(false);
				}
			}
		}

	}

	class CopyToRightActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			MainWindowModel.setIsHighlighted(false);

			if (!MainWindowModel.getIsCompared()) {
				int answer = JOptionPane.showConfirmDialog(null, "Files are not compared yet. Still want to merge?",
						"Warning", JOptionPane.WARNING_MESSAGE);
				if (answer == JOptionPane.OK_OPTION) {
					String str = leftController.getEditPanel().getContent();
		            rightController.getEditPanel().setContent(str);
		            MainWindowModel.setIsCompared(false);
				}
			}else if (MainWindowModel.getIsCompared()) {
				MainWindowModel.setIsCompared(false);
				int answer = JOptionPane.showConfirmDialog(null, "Hightlighted words will be initialized, Still want to merge?",
						"Warning", JOptionPane.WARNING_MESSAGE);
				if (answer == JOptionPane.OK_OPTION) {
					String str = leftController.getEditPanel().getContent();
		            rightController.getEditPanel().setContent(str);
		            MainWindowModel.setIsCompared(false);
				}
			}
		}

	}
}
