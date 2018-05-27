import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.text.Highlighter;
import javax.swing.border.EmptyBorder;

import controller.LCSubsequence;
import model.Node;

public class MainWindow extends JFrame {

   private JPanel contentPane;

   public StringBuffer text1;
   public StringBuffer text2;
   public Boolean      isFirst;

   /**
    * Launch the application.
    */
   public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            try {
               MainWindow frame = new MainWindow();
               frame.setVisible(true);
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      });
   }

   public JPanel createEditPanel() {
      JPanel pane = new JPanel();
      JFileChooser fileDlg = new JFileChooser();
      StringBuffer sb;
      isFirst = true; /*****************************************************************************/
      pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

      // three button panel
      JPanel buttonPanel = new JPanel();
      buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

      // three buttons
      JButton btnLoad = new JButton("Load..");
      buttonPanel.add(btnLoad);

      JButton btnSaveAs = new JButton("Save as..");
      buttonPanel.add(btnSaveAs);

      JButton btnEdit = new JButton("Edit");
      buttonPanel.add(btnEdit);

      JTextField tfFilename = new JTextField();

      // right edit panel on bottom Panel
      JPanel editPanel = new JPanel();
      pane.add(editPanel);
      GridBagLayout gbl_EditPanel = new GridBagLayout();
      gbl_EditPanel.columnWidths = new int[] { 10, 108, 0 };
      gbl_EditPanel.rowHeights = new int[] { 23, 0 };
      gbl_EditPanel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
      gbl_EditPanel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
      editPanel.setLayout(gbl_EditPanel);

      // right scrollPane that wraps left editorPane
      JScrollPane scrollPane = new JScrollPane();
      GridBagConstraints gbc_ScrollPane = new GridBagConstraints();
      gbc_ScrollPane.fill = GridBagConstraints.BOTH;
      gbc_ScrollPane.gridx = 1;
      gbc_ScrollPane.gridy = 0;
      editPanel.add(scrollPane, gbc_ScrollPane);
      scrollPane.setPreferredSize((new Dimension(400, 350)));

      // editorPane
      JEditorPane editorPane = new JEditorPane();
      scrollPane.setViewportView(editorPane);

      btnLoad.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
            int result = fileDlg.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) // ������ �����ϰ� �������� �̺�Ʈ
            {
               try {
                  File file = fileDlg.getSelectedFile();
                  Scanner scan = new Scanner(file);
                  StringBuffer sb = new StringBuffer();
                  while (scan.hasNextLine()) {
                     sb.append(scan.nextLine() + "\r\n");
                  }
                  
                  editorPane.setText(sb.toString() + "\r\n");
                  editorPane.setEditable(false); // ���� �Ұ�
                  /*****************************************************************************/
                  if(isFirst) {
                     text1    = sb;
                     isFirst   = false;
                  } else {
                     text2 = sb;
                     isFirst = true;
                  }
                  /*****************************************************************************/
               } catch (FileNotFoundException e1) {
                  // TODO Auto-generated catch block
                  e1.printStackTrace();
               }
               

            }
         }

      });
      btnEdit.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
            if (editorPane.isEditable())
               editorPane.setEditable(false);
            else
               editorPane.setEditable(true);
         }

      });

      btnSaveAs.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
            File file = fileDlg.getSelectedFile();

            try {
               if (!file.exists()) {
                  file.createNewFile();
               }

               FileWriter fw = new FileWriter(file, false);
               BufferedWriter bw = new BufferedWriter(fw);

               bw.write(editorPane.getText());
               bw.close();
            } catch (IOException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }

         }

      });

      pane.add(buttonPanel);
      pane.add(tfFilename);
      pane.add(scrollPane);
      return pane;

   }

   /**
    * Create the frame.
    */

   public MainWindow() {
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(10, 10, 1036, 586); // Default window size
      setTitle("Simple Merge - Team 5");

      // main content pane
      contentPane = new JPanel();
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      setContentPane(contentPane);

      JPanel mergePanel = new JPanel();
      mergePanel.setLayout(new BoxLayout(mergePanel, BoxLayout.Y_AXIS));

      // compare button
      JButton btnCmp = new JButton("Compare");
      mergePanel.add(btnCmp);
      btnCmp.setAlignmentX(Component.CENTER_ALIGNMENT);

      // merging button
      JButton btnCpy2Left = new JButton("�� Copy to Left");
      JButton btnCpy2Right = new JButton("Copy to Right ��");
      mergePanel.add(btnCpy2Left);
      btnCpy2Left.setAlignmentX(Component.CENTER_ALIGNMENT);
      mergePanel.add(btnCpy2Right);
      btnCpy2Right.setAlignmentX(Component.CENTER_ALIGNMENT);

      // creating edit panel and setting its border
      JPanel leftEditPanel = createEditPanel();
      JPanel rightEditPanel = createEditPanel();
      leftEditPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      mergePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      rightEditPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

      // edit panel on contentpane
      contentPane.add(leftEditPanel);
      contentPane.add(mergePanel);
      contentPane.add(rightEditPanel);

      btnCmp.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e1) {
	        LCSubsequence l = new LCSubsequence();
	        LinkedList<Node> r = LCSubsequence.getDiff(text1.toString(), text2.toString());
	        for(Node e : r) {
	            System.out.println(e.toString());
	        }
		}
      });
      
   }
}