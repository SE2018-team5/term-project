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

			LinkedList<Node> resultList = LCSubsequence.getDiff(leftModel.getSB().toString(),rightModel.getSB().toString());
			if(resultList == null) {
				System.out.println("List Error");
				return;
			}
			model.setResultList(resultList);

			model.leftList = new LinkedList<Node>();
			model.rightList = new LinkedList<Node>();
			for(Node e1 : resultList) {
				if(e1.flag == Node.DUMMY) {
					if(e1.leftIndex == -1) {
						model.add("right", e1);
					}else {
						model.add("left", e1);
					}
				}else {
					if(e1.flag == Node.ADD ) {
						model.add("right",e1);
					}else {
						model.add("left",e1);
					}
				}
			}

			for (Node e1 : resultList) {
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
			Node nodeLeft = model.getLeftList().get(model.getNodeNum());
			Node nodeRight = model.getRightList().get(model.getNodeNum());
			try {
				highlightPainter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(Color.GREEN);
				leftController.getEditPanel().getEditorPane().getHighlighter().addHighlight(nodeLeft.leftIndex, nodeLeft.leftIndex + nodeLeft.context.length(), highlightPainter);
				rightController.getEditPanel().getEditorPane().getHighlighter().addHighlight(nodeRight.rightIndex, nodeRight.rightIndex + nodeRight.context.length(), highlightPainter);
			} catch (BadLocationException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
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
			int answer = JOptionPane.showConfirmDialog(null, "Files are not compared yet. Still want to merge?",
					"Warning", JOptionPane.WARNING_MESSAGE);
			if (answer == JOptionPane.OK_OPTION) {
				//need to listen up&down button action

				//1. UP/DOWN 이 가리키는 NODE의 번호 
				int idx = model.getNodeNum();//몇 번째 노드인지 UP/DOWN이 가리키는 nodeNum 대입
				int leftidx = model.getLeftList().get(idx).leftIndex;
				int rightidx = model.getRightList().get(idx).rightIndex;
				String head = null, mid = null, tail = null;

				//2. 0부터 leftinx 까지 string 
				if(model.getLeftList().get(idx).leftIndex == 0) {
					head = "";//0보다 작을경우 exception
				}else {
					head = leftModel.getSB().toString().substring(0, leftidx);
				}
				// 오른쪽 패널 (flag가 ADD인 노드들)에서 해당 idx의 context 
				mid = model.getRightList().get(idx).context.toString();  

				// 왼쪽 패널 (flag가 DELETE인 노드들)에서 해당 idx의 rightindex부터 file의 끝까지.
				tail = leftModel.getSB().toString().substring(leftidx + mid.length());

				// leftpanel의 전체 string update.
				head=head.concat(mid);
				head=head.concat(tail);
				leftModel.setSB(head);
				model.setIsCompared(false);
				//사용자가 merge 하면 string 을 왼쪽 model의 stringbuffer에 넣음, modified 를 true로 설정, updateEditpanel 호출.
				leftModel.setIsModified(true);
				leftController.updateEditPanel();
				//해당 줄의 하이라이트 지움(compare 다시하면 해결가능)
				this.compare();
			}
		}
		public void compare() {
			highlightPainter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(Color.RED);
			leftModel.setSB(leftController.getEditPanel().getContent());
			rightModel.setSB(rightController.getEditPanel().getContent());

			LinkedList<Node> resultList = LCSubsequence.getDiff(leftModel.getSB().toString(),rightModel.getSB().toString());
			if(resultList == null) {
				System.out.println("List Error");
				return;
			}
			model.setResultList(resultList);

			model.leftList = new LinkedList<Node>();
			model.rightList = new LinkedList<Node>();
			for(Node e1 : resultList) {
				if(e1.flag == Node.DUMMY) {
					if(e1.leftIndex == -1) {
						model.add("right", e1);
					}else {
						model.add("left", e1);
					}
				}else {
					if(e1.flag == Node.ADD ) {
						model.add("right",e1);
					}else {
						model.add("left",e1);
					}
				}
			}


			for (Node e1 : resultList) {
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
			Node nodeLeft = model.getLeftList().get(model.getNodeNum());
			Node nodeRight = model.getRightList().get(model.getNodeNum());
			try {
				highlightPainter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(Color.GREEN);
				leftController.getEditPanel().getEditorPane().getHighlighter().addHighlight(nodeLeft.leftIndex, nodeLeft.leftIndex + nodeLeft.context.length(), highlightPainter);
				rightController.getEditPanel().getEditorPane().getHighlighter().addHighlight(nodeRight.rightIndex, nodeRight.rightIndex + nodeRight.context.length(), highlightPainter);
			} catch (BadLocationException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
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
			Node nodeLeft = model.getLeftList().get(model.getNodeNum());
			Node nodeRight = model.getRightList().get(model.getNodeNum());
			try {
				highlightPainter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(Color.RED);
				leftController.getEditPanel().getEditorPane().getHighlighter().addHighlight(nodeLeft.leftIndex, nodeLeft.leftIndex + nodeLeft.context.length(), highlightPainter);
				rightController.getEditPanel().getEditorPane().getHighlighter().addHighlight(nodeRight.rightIndex, nodeRight.rightIndex + nodeRight.context.length(), highlightPainter);
			} catch (BadLocationException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}

			model.decreaseNodeNum();
			nodeLeft = model.getLeftList().get(model.getNodeNum());
			nodeRight = model.getRightList().get(model.getNodeNum());
			leftController.getEditPanel().setScrollBar(nodeLeft.leftIndex);
			rightController.getEditPanel().setScrollBar(nodeRight.rightIndex);
			try {
				highlightPainter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(Color.GREEN);
				leftController.getEditPanel().getEditorPane().getHighlighter().addHighlight(nodeLeft.leftIndex, nodeLeft.leftIndex + nodeLeft.context.length(), highlightPainter);
				rightController.getEditPanel().getEditorPane().getHighlighter().addHighlight(nodeRight.rightIndex, nodeRight.rightIndex + nodeRight.context.length(), highlightPainter);
			} catch (BadLocationException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		}

	}
	class DownActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(model.getNodeNum() == model.getLeftList().size()-1) {
				return;
			}
			Node nodeLeft = model.getLeftList().get(model.getNodeNum());
			Node nodeRight = model.getRightList().get(model.getNodeNum());
			try {
				highlightPainter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(Color.RED);
				leftController.getEditPanel().getEditorPane().getHighlighter().addHighlight(nodeLeft.leftIndex, nodeLeft.leftIndex + nodeLeft.context.length(), highlightPainter);
				rightController.getEditPanel().getEditorPane().getHighlighter().addHighlight(nodeRight.rightIndex, nodeRight.rightIndex + nodeRight.context.length(), highlightPainter);
			} catch (BadLocationException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}

			model.increaseNodeNum();
			nodeLeft = model.getLeftList().get(model.getNodeNum());
			nodeRight = model.getRightList().get(model.getNodeNum());
			leftController.getEditPanel().setScrollBar(nodeLeft.leftIndex);
			rightController.getEditPanel().setScrollBar(nodeRight.rightIndex);
			try {
				highlightPainter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(Color.GREEN);
				leftController.getEditPanel().getEditorPane().getHighlighter().addHighlight(nodeLeft.leftIndex, nodeLeft.leftIndex + nodeLeft.context.length(), highlightPainter);
				rightController.getEditPanel().getEditorPane().getHighlighter().addHighlight(nodeRight.rightIndex, nodeRight.rightIndex + nodeRight.context.length(), highlightPainter);
			} catch (BadLocationException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		}

	}
	
	public void isBothLoaded() {
		if(leftController.getEditPanelModel().getIsLoaded() && rightController.getEditPanelModel().getIsLoaded()) {
			this.view.getMergePanel().setBtnsEnable();
		}
	}
}
