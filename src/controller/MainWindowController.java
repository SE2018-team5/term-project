package controller;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;

import model.EditPanelModel;
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
					EditPanelModel left = new EditPanelModel();
					EditPanelModel right = new EditPanelModel();
					MainWindowController controller = new MainWindowController(v, m, left, right);
					v.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void isBothLoaded() {
		if(leftController.getEditPanelModel().getIsLoaded() && rightController.getEditPanelModel().getIsLoaded()) {
			this.view.getMergePanel().setBtnsEnable();
		}
	}

	MainWindowController(MainWindowView v, MainWindowModel m, EditPanelModel left, EditPanelModel right) {
		this.view = v;
		this.model = m;

		leftController = new EditPanelController(view.getLeftPanel(),this, left);
		rightController = new EditPanelController(view.getRightPanel(), this, right);
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
			LinkedList<Node> r = LCSubsequence.getDiff(leftController.model.getSB().toString(), rightController.model.getSB().toString());

			
//			leftController.setStringBuffer(leftController.getEditPanel().getContent());
//			rightController.setStringBuffer(rightController.getEditPanel().getContent());
			
			leftBuffer.setStringBuffer(leftController.getEditPanel().getContent());
			rightBuffer.setStringBuffer(rightController.getEditPanel().getContent());
			
			LinkedList<Node> r = LCSubsequence.getDiff(leftBuffer.getStringBuffer().toString(),rightBuffer.getStringBuffer().toString());
			
			
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
	class UpActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	class DownActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
