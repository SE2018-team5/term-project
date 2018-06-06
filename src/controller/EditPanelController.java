package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFileChooser;

import model.EditPanelModel;
import model.MainWindowModel;
import view.EditPanel;

public class EditPanelController {
	
	EditPanelModel model;
	MainWindowController mainController;
	private EditPanel editPanel;
	public StringBuffer text;
	
	FileReader fr;
	BufferedReader br = null;
	
	EditPanelController(EditPanel e, MainWindowModel m){
	
	EditPanelController(EditPanel e, MainWindowController m, EditPanelModel em){
		this.editPanel = e;
		this.mainController = m;
		this.model = em;
		
		this.editPanel.addLoadActionListener(new LoadActionListener());
		this.editPanel.addSaveActionListener(new SaveActionListener());
		this.editPanel.addEditActionListener(new EditActionListener());
		
	}
	class LoadActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			int result = editPanel.getFileDlg().showOpenDialog(null);
			String line;
			if (result == JFileChooser.APPROVE_OPTION) // ������ �����ϰ� �������� �̺�Ʈ
			{
				try {
					File file = editPanel.getFileDlg().getSelectedFile();
					String str = "";
					fr = new FileReader(file);
					br = new BufferedReader(fr);
					while((line = br.readLine()) != null){
						str += line + "\n";
					}

					
					editPanel.getFilePathTextField().setText(file.getPath());
					editPanel.setContent(str);
					editPanel.setEditorPaneNotEditable(); // ���� �Ұ�
					text = new StringBuffer(str);
					editPanel.getBtnSaveAs().setEnabled(true);
					editPanel.getBtnEdit().setEnabled(true);
					
					model.loaded();
					mainController.isBothLoaded();
					scan.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		}
		
	}
	class SaveActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			File file = editPanel.getFileDlg().getSelectedFile();

			try {
				if (!file.exists()) {
					file.createNewFile();
				}

				FileWriter fw = new FileWriter(file, false);
				BufferedWriter bw = new BufferedWriter(fw);

				bw.write(model.getSB().toString());
				bw.close();

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	class EditActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (editPanel.isEditable()) {
				editPanel.setEditorPaneNotEditable();
				editPanel.getBtnEdit().setText("Edit");
				model.setIsModified(false);
				model.setSB(editPanel.getContent());
			}
			else {
				editPanel.setEditorPaneEditable();
				editPanel.getBtnEdit().setText("Editing...");
				model.setIsModified(true);
			}
		}
		
	}
	public EditPanel getEditPanel() {
		// TODO Auto-generated method stub
		return this.editPanel;
	}
	public void setStringBuffer(String s) {
		text.delete(0, text.length());
		text.append(s);
	}
	
	public EditPanelModel getEditPanelModel() {
		return this.model;
	}
}
