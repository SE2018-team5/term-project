import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class MainWindow extends JFrame {

    private JPanel contentPane;

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

    public JPanel createEditPanel(){
    	JPanel pane = new JPanel();
    	pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
    	
        //  three button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        
        //  three buttons
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
        gbl_EditPanel.columnWidths = new int[]{10, 108, 0};
        gbl_EditPanel.rowHeights = new int[]{23, 0};
        gbl_EditPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gbl_EditPanel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
        editPanel.setLayout(gbl_EditPanel);
        
        // right scrollPane that wraps left editorPane
        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc_ScrollPane = new GridBagConstraints();
        gbc_ScrollPane.fill = GridBagConstraints.BOTH;
        gbc_ScrollPane.gridx = 1;
        gbc_ScrollPane.gridy = 0;
        editPanel.add(scrollPane, gbc_ScrollPane);
        scrollPane.setPreferredSize((new Dimension(400,350)));
        
        //editorPane
        JEditorPane editorPane = new JEditorPane();
        scrollPane.setViewportView(editorPane);
        
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
        setBounds(10, 10, 1036, 586);  // Default window size
        setTitle("Simple Merge - Team 5");
        
        //  main content pane
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        
        JPanel mergePanel = new JPanel();
        mergePanel.setLayout(new BoxLayout(mergePanel, BoxLayout.Y_AXIS));
        
        
        
        //compare button
        JButton btnCmp = new JButton("Compare");
        mergePanel.add(btnCmp);
        btnCmp.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        //merging button
        JButton btnCpy2Left = new JButton("<- Copy to Left");
        JButton btnCpy2Right = new JButton("Copy to Right ->");
        mergePanel.add(btnCpy2Left);
        btnCpy2Left.setAlignmentX(Component.CENTER_ALIGNMENT);
        mergePanel.add(btnCpy2Right);
        btnCpy2Right.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        
        //creating edit panel and setting its border
        JPanel leftEditPanel = createEditPanel();
        JPanel rightEditPanel = createEditPanel();
        leftEditPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        mergePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        rightEditPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        //edit panel on contentpane
        contentPane.add(leftEditPanel);
        contentPane.add(mergePanel);
        contentPane.add(rightEditPanel);
        
        
    }
}
