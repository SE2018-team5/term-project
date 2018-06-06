package controller;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;

import com.sun.org.apache.xpath.internal.operations.Equals;

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
	
	public LinkedList<Node> getCopytoleft() {
		return copytoleft;
	}

	public void setCopytoleft(LinkedList<Node> copytoleft) {
		this.copytoleft = copytoleft;
	}



	LinkedList<Node> copytoleft;
	LinkedList<Node> copytoright;
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

			leftBuffer.setStringBuffer(leftController.getEditPanel().getContent());
			rightBuffer.setStringBuffer(rightController.getEditPanel().getContent());
			
			LinkedList<Node> r = LCSubsequence.getDiff(leftBuffer.getStringBuffer().toString(),rightBuffer.getStringBuffer().toString());

			for(Node e1 : r) {
				if(e1.flag == Node.ADD ) {
					copytoleft.add(e1);
				}else {
					copytoright.add(e1);
				}
			}
			
			
			System.out.println(leftBuffer.getStringBuffer().toString());
			System.out.println(rightBuffer.getStringBuffer().toString());
			
			for (Node e1 : r) {
				if (e1.flag == Node.DELETE) {
					System.out.println("Delete\n");
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
					//1. UP/DOWN 이 가리키는 NODE의 번호 
					int idx = get()/2;//몇 번째 노드인지 UP/DOWN이 가리키는 nodeNum 대입	
					int leftidx = 0;
					int rightidx = 0;
					String head = null, mid = null, tail = null;
					
							//2. 0부터 leftinx 까지 string 
							if(copytoleft.get(idx).leftIndex == 0) {
								head = leftController.text.substring(0, leftidx);//0보다 작을경우 exception
							}else {
								head = leftController.text.substring(0, leftidx-1);
							}
							// 오른쪽 패널 (flag가 ADD인 노드들)에서 해당 idx의 context 
							mid = copytoleft.get(idx).context.toString();  
							
							// 왼쪽 패널 (flag가 DELETE인 노드들)에서 해당 idx의 rightindex부터 file의 끝까지.
							tail = leftController.text.substring(copytoright.get(idx).rightIndex + copytoright.get(idx).context.length(), leftController.text.length());
							
							// leftpanel의 전체 string update.
							head.concat(mid);
							head.concat(tail);
							
							head = leftController.getEditPanel().getContent();
				            leftController.getEditPanel().setContent(head);
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
