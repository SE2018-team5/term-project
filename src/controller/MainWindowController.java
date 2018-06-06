package controller;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;

import model.EditPanelModel;
import model.MainWindowModel;
import model.Node;
import view.MainWindowView;

public class MainWindowController {

	MainWindowView view;
	MainWindowModel model;

	EditPanelController leftController;
	EditPanelController rightController;
	EditPanelModel leftModel;
	EditPanelModel rightModel;
	javax.swing.text.DefaultHighlighter.DefaultHighlightPainter highlightPainter;

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

	//mvc pattern
	MainWindowController(MainWindowView v, MainWindowModel m, EditPanelModel left, EditPanelModel right) {
		this.view = v;
		this.model = m;
		this.leftModel = left;
		this.rightModel = right;

		leftController = new EditPanelController(view.getLeftPanel(),this, left);
		rightController = new EditPanelController(view.getRightPanel(), this, right);

		this.view.getMergePanel().addCmpActionListener(new CmpActionListener());
		this.view.getMergePanel().addCopyToLeftActionListener(new CopyToLeftActionListener());
		this.view.getMergePanel().addCopyToRightActionListener(new CopyToRightActionListener());
		this.view.getMergePanel().addUpActionListener(new UpActionListener());
		this.view.getMergePanel().addDownActionListener(new DownActionListener());

	}

	/**
	 * Compare 踰꾪듉 �닃�졇�쓣 �븣 泥섎━�븯�뒗 ActionListener
	 * 1. 鍮④컙�깋 �븯�씠�씪�씠�꽣 以�鍮�
	 * 2. �뼇履� editPanelModel�쓽 StringBuffer�뿉 �궡�슜 ���엯
	 * 3. 臾몄옣 遺꾩꽍 �썑 resultList�뿉 �끂�뱶由ъ뒪�듃 ���엯
	 * 4. �쇊履쎌쓽 �끂�뱶�� �삤瑜몄そ�쓽 �끂�뱶媛� 1��1濡� ���쓳�릺�룄濡� �끂�뱶由ъ뒪�듃 遺꾨━
	 * 5. 
	 */
	class CmpActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			highlightPainter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(Color.RED);

			leftModel.setSB(leftController.getEditPanel().getContent());
			rightModel.setSB(rightController.getEditPanel().getContent());

			LinkedList<Node> result = LCSubsequence.getDiff(leftModel.getSB().toString(),rightModel.getSB().toString());
			if(result == null) {
				System.out.println("List Error");
				return;
			}
			model.initResultNode();
			for(Node e1:result) {
				if(e1.flag != Node.DUMMY)
					model.add(e1);
			}

			for (Node e1 : result) {
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
			model.setNodeNumZero();
			Node e1 = model.getPresentNode();
			highlightPainter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(Color.GREEN);
			if (e1.flag == Node.DELETE) {
				try {
					leftController.getEditPanel().getEditorPane().getHighlighter().
					addHighlight(e1.leftIndex, e1.leftIndex + e1.context.length(), highlightPainter);
				} catch (BadLocationException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
			if (e1.flag == Node.ADD) {
				try {
					rightController.getEditPanel().getEditorPane().getHighlighter().
					addHighlight(e1.rightIndex, e1.rightIndex + e1.context.length(), highlightPainter);
				} catch (BadLocationException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
			model.setIsCompared(true);
			model.setIsHighlighted(true); 
			
			view.getMergePanel().setBtnsEnable();
		}

	}

	class CopyToLeftActionListener implements ActionListener {


		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			model.setIsHighlighted(false);
			if(!model.getIsCompared()) {
				int answer = JOptionPane.showConfirmDialog(null, "Files are not compared yet. Still want to merge?",
						"Warning", JOptionPane.WARNING_MESSAGE);
				if (answer == JOptionPane.OK_OPTION) {
					String str = rightController.getEditPanel().getContent();
					leftController.getEditPanel().setContent(str);
					model.setIsCompared(false);
				}
			}
			else {
				//need to listen up&down button action
				if(model.getPresentNode().flag == Node.ADD) {
					//1. UP/DOWN �씠 媛�由ы궎�뒗 NODE�쓽 踰덊샇 
					Node pNode = model.getPresentNode();  //�씠踰� �끂�뱶
					String head = null, mid = null, tail = null;

					//2. 0遺��꽣 leftinx 源뚯� string 
					head = leftModel.getSB().toString().substring(0, pNode.leftIndex);
					// �삤瑜몄そ �뙣�꼸 (flag媛� ADD�씤 �끂�뱶�뱾)�뿉�꽌 �빐�떦 idx�쓽 context 
					mid = pNode.context.toString();  

					// �쇊履� �뙣�꼸 (flag媛� DELETE�씤 �끂�뱶�뱾)�뿉�꽌 �빐�떦 idx�쓽 rightindex遺��꽣 file�쓽 �걹源뚯�.
					tail = leftModel.getSB().toString().substring(pNode.leftIndex);

					// leftpanel�쓽 �쟾泥� string update.
					head=head+mid+tail;
					leftModel.setSB(head);
					model.setIsCompared(false);
					//�궗�슜�옄媛� merge �븯硫� string �쓣 �쇊履� model�쓽 stringbuffer�뿉 �꽔�쓬, modified 瑜� true濡� �꽕�젙, updateEditpanel �샇異�.
					leftModel.setIsModified(true);
					leftController.updateEditPanel();
					//�빐�떦 以꾩쓽 �븯�씠�씪�씠�듃 吏���(compare �떎�떆�븯硫� �빐寃곌��뒫)
					this.compare();
				}
			}

		}
		public void compare() {
			highlightPainter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(Color.RED);

			leftModel.setSB(leftController.getEditPanel().getContent());
			rightModel.setSB(rightController.getEditPanel().getContent());

			LinkedList<Node> result = LCSubsequence.getDiff(leftModel.getSB().toString(),rightModel.getSB().toString());
			if(result == null) {
				System.out.println("List Error");
				return;
			}
			model.initResultNode();
			
			for(Node e1:result) {
				if(e1.flag != Node.DUMMY)
					model.add(e1);
			}

			for (Node e1 : result) {
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
			Node e1 = model.getPresentNode();
			highlightPainter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(Color.GREEN);
			if (e1.flag == Node.DELETE) {
				try {
					leftController.getEditPanel().getEditorPane().getHighlighter().
					addHighlight(e1.leftIndex, e1.leftIndex + e1.context.length(), highlightPainter);
				} catch (BadLocationException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
			if (e1.flag == Node.ADD) {
				try {
					rightController.getEditPanel().getEditorPane().getHighlighter().
					addHighlight(e1.rightIndex, e1.rightIndex + e1.context.length(), highlightPainter);
				} catch (BadLocationException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
			model.setIsCompared(true);
			model.setIsHighlighted(true); 
		}
	}

	class CopyToRightActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			model.setIsHighlighted(false);

			if (!model.getIsCompared()) {
				int answer = JOptionPane.showConfirmDialog(null, "Files are not compared yet. Still want to merge?",
						"Warning", JOptionPane.WARNING_MESSAGE);
				if (answer == JOptionPane.OK_OPTION) {
					String str = leftController.getEditPanel().getContent();
					rightController.getEditPanel().setContent(str);
					model.setIsCompared(false);
				}
			}else if (model.getIsCompared()) {
				model.setIsCompared(false);
				int answer = JOptionPane.showConfirmDialog(null, "Hightlighted words will be initialized, Still want to merge?",
						"Warning", JOptionPane.WARNING_MESSAGE);
				if (answer == JOptionPane.OK_OPTION) {
					String str = leftController.getEditPanel().getContent();
					rightController.getEditPanel().setContent(str);
					model.setIsCompared(false);
				}
			}
		}

	}
	class UpActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(model.getNodeNum() == 0) {
				return;
			}
			Node e1 = model.getPresentNode();
			highlightPainter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(Color.RED);
			if (e1.flag == Node.DELETE) {
				try {
					leftController.getEditPanel().getEditorPane().getHighlighter().
					addHighlight(e1.leftIndex, e1.leftIndex + e1.context.length(), highlightPainter);
				} catch (BadLocationException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
			if (e1.flag == Node.ADD) {
				try {
					rightController.getEditPanel().getEditorPane().getHighlighter().
					addHighlight(e1.rightIndex, e1.rightIndex + e1.context.length(), highlightPainter);
				} catch (BadLocationException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}

			model.decreaseNodeNum();
			leftController.getEditPanel().setScrollBar(e1.leftIndex);
			rightController.getEditPanel().setScrollBar(e1.rightIndex);
			e1 = model.getPresentNode();
			highlightPainter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(Color.GREEN);
			if (e1.flag == Node.DELETE) {
				try {
					leftController.getEditPanel().getEditorPane().getHighlighter().
					addHighlight(e1.leftIndex, e1.leftIndex + e1.context.length(), highlightPainter);
				} catch (BadLocationException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
			if (e1.flag == Node.ADD) {
				try {
					rightController.getEditPanel().getEditorPane().getHighlighter().
					addHighlight(e1.rightIndex, e1.rightIndex + e1.context.length(), highlightPainter);
				} catch (BadLocationException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
		}

	}
	class DownActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(model.getNodeNum() == model.getResultList().size()-1) {
				return;
			}
			Node e1 = model.getPresentNode();
			highlightPainter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(Color.RED);
			if (e1.flag == Node.DELETE) {
				try {
					leftController.getEditPanel().getEditorPane().getHighlighter().
					addHighlight(e1.leftIndex, e1.leftIndex + e1.context.length(), highlightPainter);
				} catch (BadLocationException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
			if (e1.flag == Node.ADD) {
				try {
					rightController.getEditPanel().getEditorPane().getHighlighter().
					addHighlight(e1.rightIndex, e1.rightIndex + e1.context.length(), highlightPainter);
				} catch (BadLocationException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}

			model.increaseNodeNum();
			leftController.getEditPanel().setScrollBar(e1.leftIndex);
			rightController.getEditPanel().setScrollBar(e1.rightIndex);
			e1 = model.getPresentNode();
			highlightPainter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(Color.GREEN);
			if (e1.flag == Node.DELETE) {
				try {
					leftController.getEditPanel().getEditorPane().getHighlighter().
					addHighlight(e1.leftIndex, e1.leftIndex + e1.context.length(), highlightPainter);
				} catch (BadLocationException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
			if (e1.flag == Node.ADD) {
				try {
					rightController.getEditPanel().getEditorPane().getHighlighter().
					addHighlight(e1.rightIndex, e1.rightIndex + e1.context.length(), highlightPainter);
				} catch (BadLocationException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
		}

	}
	
	public void isBothLoaded() {
		if((leftController.getEditPanelModel().getIsLoaded() && rightController.getEditPanelModel().getIsLoaded())
				|| (!leftController.getEditPanelModel().getIsLoaded() && !rightController.getEditPanelModel().getIsLoaded())) {
			this.view.getMergePanel().setBtnCmpEnable();
		} else {
			this.view.getMergePanel().setBtnsUnEnable();
		}
	}
}
