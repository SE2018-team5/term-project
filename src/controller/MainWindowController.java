package controller;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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

    MainWindowView  view;
    MainWindowModel model;

    EditPanelController                                         leftController;
    EditPanelController                                         rightController;
    EditPanelModel                                              leftModel;
    EditPanelModel                                              rightModel;
    javax.swing.text.DefaultHighlighter.DefaultHighlightPainter highlightPainter;
    javax.swing.text.DefaultHighlighter.DefaultHighlightPainter highlightPainterGreen;
    ArrayList<Object>                                           highlighterLeftList;
    ArrayList<Object>                                           highlighterRightList;

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

    // mvc pattern
    MainWindowController(MainWindowView v, MainWindowModel m, EditPanelModel left, EditPanelModel right) {
        this.view = v;
        this.model = m;
        this.leftModel = left;
        this.rightModel = right;

        leftController = new EditPanelController(view.getLeftPanel(), this, left);
        rightController = new EditPanelController(view.getRightPanel(), this, right);

        this.view.getMergePanel().addCmpActionListener(new CmpActionListener());
        this.view.getMergePanel().addCopyToLeftActionListener(new CopyToLeftActionListener());
        this.view.getMergePanel().addCopyToRightActionListener(new CopyToRightActionListener());
        this.view.getMergePanel().addUpActionListener(new UpActionListener());
        this.view.getMergePanel().addDownActionListener(new DownActionListener());

    }

    /**
     * Compare 踰꾪듉 �닃�졇�쓣 �븣 泥섎━�븯�뒗 ActionListener 1. 鍮④컙�깋 �븯�씠�씪�씠�꽣 以�鍮� 2. �뼇履� editPanelModel�쓽
     * StringBuffer�뿉 �궡�슜 ���엯 3. 臾몄옣 遺꾩꽍 �썑 resultList�뿉 �끂�뱶由ъ뒪�듃 ���엯 4. �쇊履쎌쓽 �끂�뱶�� �삤瑜몄そ�쓽 �끂�뱶媛� 1��1濡�
     * ���쓳�릺�룄濡� �끂�뱶由ъ뒪�듃 遺꾨━ 5.
     */
    class CmpActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            highlighterLeftList  = new ArrayList();
            highlighterRightList = new ArrayList();

            compareAction();
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
                // need to listen up&down button action

                // 1. UP/DOWN �씠 媛�由ы궎�뒗 NODE�쓽 踰덊샇
                int idx      = model.getNodeNum();// 紐� 踰덉㎏ �끂�뱶�씤吏� UP/DOWN�씠 媛�由ы궎�뒗 nodeNum ���엯
                int leftidx  = model.getLeftList().get(idx).leftIndex;
                int rightidx = model.getRightList().get(idx).rightIndex;

                Node leftNode  = model.getLeftList().get(idx);
                Node rightNode = model.getRightList().get(idx);

                String head = null, mid = null, tail = null;

                // 2. 0遺��꽣 leftinx 源뚯� string

                head = leftModel.getSB().toString().substring(0, leftidx);
                // �삤瑜몄そ �뙣�꼸 (flag媛� ADD�씤 �끂�뱶�뱾)�뿉�꽌 �빐�떦 idx�쓽 context
                mid = model.getRightList().get(idx).context.toString();

                // �쇊履� �뙣�꼸 (flag媛� DELETE�씤 �끂�뱶�뱾)�뿉�꽌 �빐�떦 idx�쓽 rightindex遺��꽣 file�쓽 �걹源뚯�.
                tail = leftModel.getSB().toString().substring(leftidx + leftNode.context.length());

                // leftpanel�쓽 �쟾泥� string update.
                head = head.concat(mid);
                head = head.concat(tail);
                
                leftModel.setSB(head);
                model.setIsCompared(false);
                
                // �궗�슜�옄媛� merge �븯硫� string �쓣 �쇊履� model�쓽 stringbuffer�뿉 �꽔�쓬, modified 瑜� true濡� �꽕�젙,
                // updateEditpanel �샇異�.
                leftModel.setIsModified(true);
                leftController.updateEditPanel();
                // �빐�떦 以꾩쓽 �븯�씠�씪�씠�듃 吏���(compare �떎�떆�븯硫� �빐寃곌��뒫)
                compareAction();
            }
        }
    }

    class CopyToRightActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            
         // TODO Auto-generated method stub
            model.setIsHighlighted(false);
            int answer = JOptionPane.showConfirmDialog(null, "Files are not compared yet. Still want to merge?",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            if (answer == JOptionPane.OK_OPTION) {
                // need to listen up&down button action

                // 1. UP/DOWN �씠 媛�由ы궎�뒗 NODE�쓽 踰덊샇
                int idx      = model.getNodeNum();// 紐� 踰덉㎏ �끂�뱶�씤吏� UP/DOWN�씠 媛�由ы궎�뒗 nodeNum ���엯
                int leftidx  = model.getLeftList().get(idx).leftIndex;
                int rightidx = model.getRightList().get(idx).rightIndex;

                Node leftNode  = model.getLeftList().get(idx);
                Node rightNode = model.getRightList().get(idx);

                String head = null, mid = null, tail = null;

                // 2. 0遺��꽣 rightidx 源뚯� string

                head = rightModel.getSB().toString().substring(0, rightidx);
                // �쇊履� �뙣�꼸 (flag媛� ADD�씤 �끂�뱶�뱾)�뿉�꽌 �빐�떦 idx�쓽 context
                mid = model.getLeftList().get(idx).context.toString();

                // �삤瑜몄そ �뙣�꼸 (flag媛� DELETE�씤 �끂�뱶�뱾)�뿉�꽌 �빐�떦 idx�쓽 rightindex遺��꽣 file�쓽 �걹源뚯�.
                tail = rightModel.getSB().toString().substring(rightidx + rightNode.context.length());

                // rightpanel�쓽 �쟾泥� string update.
                head = head.concat(mid);
                head = head.concat(tail);
                
                rightModel.setSB(head);
                model.setIsCompared(false);
                
                // �궗�슜�옄媛� merge �븯硫� string �쓣 �쇊履� model�쓽 stringbuffer�뿉 �꽔�쓬, modified 瑜� true濡� �꽕�젙,
                // updateEditpanel �샇異�.
                rightModel.setIsModified(true);
                rightController.updateEditPanel();
                
                // �빐�떦 以꾩쓽 �븯�씠�씪�씠�듃 吏���(compare �떎�떆�븯硫� �빐寃곌��뒫)
                compareAction();
            }
        }
    }

    class UpActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            if (model.getNodeNum() == 0) {
                return;
            }
            if (!model.getLeftList().isEmpty() && !model.getRightList().isEmpty()) {
                Node nodeLeft = model.getLeftList().get(model.getNodeNum());
                Node nodeRight = model.getRightList().get(model.getNodeNum());
                try {
                    highlightPainter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(Color.RED);
                    leftController.getEditPanel().getEditorPane().getHighlighter()
                            .removeHighlight(highlighterLeftList.get(model.getNodeNum()));
                    highlighterLeftList.set(model.getNodeNum(),
                            leftController.getEditPanel().getEditorPane().getHighlighter().addHighlight(
                                    nodeLeft.leftIndex, nodeLeft.leftIndex + nodeLeft.context.length(),
                                    highlightPainter));

                    rightController.getEditPanel().getEditorPane().getHighlighter()
                            .removeHighlight(highlighterRightList.get(model.getNodeNum()));
                    highlighterRightList.set(model.getNodeNum(),
                            rightController.getEditPanel().getEditorPane().getHighlighter().addHighlight(
                                    nodeRight.rightIndex, nodeRight.rightIndex + nodeRight.context.length(),
                                    highlightPainter));
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
                    leftController.getEditPanel().getEditorPane().getHighlighter()
                            .removeHighlight(highlighterLeftList.get(model.getNodeNum()));
                    highlighterLeftList.set(model.getNodeNum(),
                            leftController.getEditPanel().getEditorPane().getHighlighter().addHighlight(
                                    nodeLeft.leftIndex, nodeLeft.leftIndex + nodeLeft.context.length(),
                                    highlightPainter));

                    rightController.getEditPanel().getEditorPane().getHighlighter()
                            .removeHighlight(highlighterRightList.get(model.getNodeNum()));
                    highlighterRightList.set(model.getNodeNum(),
                            rightController.getEditPanel().getEditorPane().getHighlighter().addHighlight(
                                    nodeRight.rightIndex, nodeRight.rightIndex + nodeRight.context.length(),
                                    highlightPainter));
                } catch (BadLocationException ex) {
                    // TODO Auto-generated catch block
                    ex.printStackTrace();
                }

            }
        }

    }

    class DownActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            if (model.getNodeNum() == model.getLeftList().size() - 1) {
                return;
            }

            if (!model.getLeftList().isEmpty() && !model.getRightList().isEmpty()) {
                Node nodeLeft = model.getLeftList().get(model.getNodeNum());
                Node nodeRight = model.getRightList().get(model.getNodeNum());
                try {
                    highlightPainter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(Color.RED);
                    leftController.getEditPanel().getEditorPane().getHighlighter()
                            .removeHighlight(highlighterLeftList.get(model.getNodeNum()));
                    highlighterLeftList.set(model.getNodeNum(),
                            leftController.getEditPanel().getEditorPane().getHighlighter().addHighlight(
                                    nodeLeft.leftIndex, nodeLeft.leftIndex + nodeLeft.context.length(),
                                    highlightPainter));

                    rightController.getEditPanel().getEditorPane().getHighlighter()
                            .removeHighlight(highlighterRightList.get(model.getNodeNum()));
                    highlighterRightList.set(model.getNodeNum(),
                            rightController.getEditPanel().getEditorPane().getHighlighter().addHighlight(
                                    nodeRight.rightIndex, nodeRight.rightIndex + nodeRight.context.length(),
                                    highlightPainter));
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
                    leftController.getEditPanel().getEditorPane().getHighlighter()
                            .removeHighlight(highlighterLeftList.get(model.getNodeNum()));
                    highlighterLeftList.set(model.getNodeNum(),
                            leftController.getEditPanel().getEditorPane().getHighlighter().addHighlight(
                                    nodeLeft.leftIndex, nodeLeft.leftIndex + nodeLeft.context.length(),
                                    highlightPainter));

                    rightController.getEditPanel().getEditorPane().getHighlighter()
                            .removeHighlight(highlighterRightList.get(model.getNodeNum()));
                    highlighterRightList.set(model.getNodeNum(),
                            rightController.getEditPanel().getEditorPane().getHighlighter().addHighlight(
                                    nodeRight.rightIndex, nodeRight.rightIndex + nodeRight.context.length(),
                                    highlightPainter));
                } catch (BadLocationException ex) {
                    // TODO Auto-generated catch block
                    ex.printStackTrace();
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
    
    public void compareAction() {
        
        // remove highlights
        leftController.getEditPanel().getEditorPane().getHighlighter().removeAllHighlights();
        rightController.getEditPanel().getEditorPane().getHighlighter().removeAllHighlights();
        
        // remove highlight tags
        highlighterLeftList.clear();
        highlighterRightList.clear();

        // set default highlighters
        highlightPainter      = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(Color.RED);
        highlightPainterGreen = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(Color.GREEN);
        
        // set left and right contents to StringBuffer
        leftModel.setSB(leftController.getEditPanel().getContent());
        rightModel.setSB(rightController.getEditPanel().getContent());

        // calculate LCS algorithm
        LinkedList<Node> resultList = LCSubsequence.getDiff(leftModel.getSB().toString(),
                rightModel.getSB().toString());

        if (resultList == null) {
            System.out.println("make diff list error : LCS algorithm");
            return;
        }

        model.setResultList(resultList);
        
        // init left and right Node list
        model.leftList  = new LinkedList<Node>();
        model.rightList = new LinkedList<Node>();
        
        // add Nodes left and right list
        for (Node e1 : resultList) {
            if (e1.flag == Node.DUMMY) {
                if (e1.leftIndex == -1) {
                    model.add("right", e1);
                } else {
                    model.add("left", e1);
                }
            } else {
                if (e1.flag == Node.ADD) {
                    model.add("right", e1);
                } else {
                    model.add("left", e1);
                }
            }
        }
        
        // set node number to zero (for select to copy)
        model.setNodeNumZero();
        
        // fill highlights 
        if (!model.getLeftList().isEmpty() && !model.getRightList().isEmpty()) {
            
            Node nodeLeft  = model.getLeftList().get(model.getNodeNum());
            Node nodeRight = model.getRightList().get(model.getNodeNum());
            
            // on left side
            for (Node n : model.getLeftList()) {
                // is selected?
                if (!n.equals(nodeLeft)) {
                    try {
                        highlighterLeftList.add(leftController.getEditPanel().getEditorPane().getHighlighter()
                                .addHighlight(n.leftIndex, n.leftIndex + n.context.length(), highlightPainter));

                    } catch (BadLocationException ex) {
                        // TODO Auto-generated catch block
                        ex.printStackTrace();
                    }
                } else {
                    try {
                        highlighterLeftList
                                .add(leftController.getEditPanel().getEditorPane().getHighlighter().addHighlight(
                                        n.leftIndex, n.leftIndex + n.context.length(), highlightPainterGreen));
                    } catch (BadLocationException ex) {
                        // TODO Auto-generated catch block
                        ex.printStackTrace();
                    }
                }
            }
            
            // on right side
            for (Node n : model.getRightList()) {
                // is selected?
                if (!n.equals(nodeRight)) {
                    try {
                        highlighterRightList.add(rightController.getEditPanel().getEditorPane().getHighlighter()
                                .addHighlight(n.rightIndex, n.rightIndex + n.context.length(), highlightPainter));

                    } catch (BadLocationException ex) {
                        // TODO Auto-generated catch block
                        ex.printStackTrace();
                    }

                } else {
                    try {
                        highlighterRightList
                                .add(rightController.getEditPanel().getEditorPane().getHighlighter().addHighlight(
                                        n.rightIndex, n.rightIndex + n.context.length(), highlightPainterGreen));
                    } catch (BadLocationException ex) {
                        // TODO Auto-generated catch block
                        ex.printStackTrace();
                    }
                }
            }

        }
        
        model.setIsCompared(true);
        model.setIsHighlighted(true);
        view.getMergePanel().setBtnsEnable();
    }
}
