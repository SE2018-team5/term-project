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
	 * Compare 버튼 눌렸을 때 처리하는 ActionListener
	 * 1. 빨간색 하이라이터 준비
	 * 2. 양쪽 editPanelModel의 StringBuffer에 내용 대입
	 * 3. 문장 분석 후 resultList에 노드리스트 대입
	 * 4. 왼쪽의 노드와 오른쪽의 노드가 1대1로 대응되도록 노드리스트 분리
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
					//1. UP/DOWN 이 가리키는 NODE의 번호 
					Node pNode = model.getPresentNode();  //이번 노드
					String head = null, mid = null, tail = null;

					//2. 0부터 leftinx 까지 string 
					head = leftModel.getSB().toString().substring(0, pNode.leftIndex);
					// 오른쪽 패널 (flag가 ADD인 노드들)에서 해당 idx의 context 
					mid = pNode.context.toString();  

					// 왼쪽 패널 (flag가 DELETE인 노드들)에서 해당 idx의 rightindex부터 file의 끝까지.
					tail = leftModel.getSB().toString().substring(pNode.leftIndex);

					// leftpanel의 전체 string update.
					head=head+mid+tail;
					leftModel.setSB(head);
					model.setIsCompared(false);
					//사용자가 merge 하면 string 을 왼쪽 model의 stringbuffer에 넣음, modified 를 true로 설정, updateEditpanel 호출.
					leftModel.setIsModified(true);
					leftController.updateEditPanel();
					//해당 줄의 하이라이트 지움(compare 다시하면 해결가능)
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
		if(leftController.getEditPanelModel().getIsLoaded() && rightController.getEditPanelModel().getIsLoaded()) {
			this.view.getMergePanel().setBtnsEnable();
		}
	}
}
