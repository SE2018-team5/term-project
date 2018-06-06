package view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 * Edit Panel Class
 * load, save as, edit Button, TextField for filename, EditorPane needed.
 * save as and edit Button should be disabled until load operation is successfully processed.
 */
public class EditPanel extends JPanel{
	private JPanel buttonPanel;
	private JButton btnLoad,btnSaveAs,btnEdit;
	private JTextField tfFilePath;
	private JPanel editPanel;
	private JScrollPane scrollPane;
	private JEditorPane editorPane;
	private JFileChooser fileDlg;


	public JFileChooser getFileDlg() {
		return this.fileDlg;
	}
	
	public JTextField getFilePathTextField() {
		return this.tfFilePath;
	}
	public String getFilePath() {
		return this.tfFilePath.getText();
	}
	public void setFilePath(String s) {
		this.tfFilePath.setText(s);
	}

	public String getContent() {
		return this.editorPane.getText();
	}

	public void setContent(String s) {
		this.editorPane.setText(s);
	}
	public void getContent(String s) {
		this.editorPane.getText();
	}
	
	public JButton getBtnLoad() {
		return this.btnLoad;
	}
	public JButton getBtnSaveAs() {
		return this.btnSaveAs;
	}
	public JButton getBtnEdit() {
		return this.btnEdit;
		
	}
	public JEditorPane getEditorPane() {
		
		return this.editorPane;
	}
	public void setEditorPaneEditable() {
		this.editorPane.setEditable(true);
	}
	public void setEditorPaneNotEditable() {
		this.editorPane.setEditable(false);
	}
	public Boolean isEditable() {
		return this.editorPane.isEditable();
	}
	public void setScrollBar(int index) {
		this.scrollPane.getVerticalScrollBar().setValue(index);
	}

	EditPanel(){
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		fileDlg = new JFileChooser();
		fileDlg.setFileFilter((FileFilter) new FileNameExtensionFilter("Text file", "txt")); // .txt 파일만 보이게
		fileDlg.setMultiSelectionEnabled(false);//다중 선택 불가
		fileDlg.setCurrentDirectory(new File(System.getProperty("user.dir") + "//" + "data"));

		// three button panel
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		// three buttons
		btnLoad = new JButton("Load..");
		btnLoad.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonPanel.add(btnLoad);


		btnSaveAs = new JButton("Save as..");
		btnSaveAs.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnSaveAs.setEnabled(false);
		buttonPanel.add(btnSaveAs);

		btnEdit = new JButton("Edit");
		btnEdit.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnEdit.setEnabled(false);
		buttonPanel.add(btnEdit);

		tfFilePath = new JTextField();
		tfFilePath.setEditable(false);

		// edit panel
		editPanel = new JPanel();
		this.add(editPanel);
		GridBagLayout gbl_EditPanel = new GridBagLayout();
		gbl_EditPanel.columnWidths = new int[] { 10, 108, 0 };
		gbl_EditPanel.rowHeights = new int[] { 23, 0 };
		gbl_EditPanel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_EditPanel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		editPanel.setLayout(gbl_EditPanel);

		// scrollPane that wraps editorPane
		scrollPane = new JScrollPane();
		GridBagConstraints gbc_ScrollPane = new GridBagConstraints();
		gbc_ScrollPane.fill = GridBagConstraints.BOTH;
		gbc_ScrollPane.gridx = 1;
		gbc_ScrollPane.gridy = 0;
		editPanel.add(scrollPane, gbc_ScrollPane);
		scrollPane.setPreferredSize((new Dimension(400, 350)));

		// editorPane
		editorPane = new JEditorPane();
		scrollPane.setViewportView(editorPane);

		this.add(buttonPanel);
		this.add(tfFilePath);
		this.add(scrollPane);


	}
	public void addLoadActionListener(ActionListener listenerForLoadAction) {
		btnLoad.addActionListener(listenerForLoadAction);
	}
	public void addSaveActionListener(ActionListener listenerForSaveAction) {
		btnSaveAs.addActionListener(listenerForSaveAction);
	}
	public void addEditActionListener(ActionListener listenerForEditAction) {
		btnEdit.addActionListener(listenerForEditAction);
	}
	

}
