package view;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


public class MainWindowView extends JFrame {

	private JPanel contentPane;
	private EditPanel leftEditPanel;
	private EditPanel rightEditPanel;
	private MergePanel mergePanel;
	
	
	public EditPanel getLeftPanel() {
		return this.leftEditPanel;
	}
	public EditPanel getRightPanel() {
		return this.rightEditPanel;
	}
	public MergePanel getMergePanel() {
		return this.mergePanel;
	}
	
	/**
	 * Main Window Constructor. 
	 * MergePanel and EditPanel(both left and right) needed.
	 */
	public MainWindowView() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(10, 10, 1060, 586); // Default window size
		this.setTitle("Simple Merge - Team 5");
		this.setLocationRelativeTo(null);

		// main content pane
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(contentPane);

		mergePanel = new MergePanel();

		// creating edit panel and setting its border
		leftEditPanel = new EditPanel();
		rightEditPanel = new EditPanel();
		leftEditPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		mergePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		rightEditPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// edit panel on contentpane
		contentPane.add(leftEditPanel);
		contentPane.add(mergePanel);
		contentPane.add(rightEditPanel);

		
	}
}


