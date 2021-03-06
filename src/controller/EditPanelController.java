package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;

import model.EditPanelModel;
import view.EditPanel;

public class EditPanelController {

	EditPanelModel model;
	MainWindowController mainController;
	private EditPanel editPanel;
	public StringBuffer text;

	FileReader fr;
	BufferedReader br = null;

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



			if (result == JFileChooser.APPROVE_OPTION) // 파일을 선택하고 열었을때 이벤트
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
					editPanel.setEditorPaneNotEditable(); // 수정 불가
					text = new StringBuffer(str);

					editPanel.getBtnSaveAs().setEnabled(true);
					editPanel.getBtnEdit().setEnabled(true);

					model.loaded();
					mainController.isBothLoaded();
					model.setSB(editPanel.getContent());
					br.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		}

	}
	class SaveActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			int result = editPanel.getFileDlg().showSaveDialog(null);

			if (result == JFileChooser.APPROVE_OPTION) {
				try {
					File file = editPanel.getFileDlg().getSelectedFile();
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
				mainController.view.getMergePanel().setBtnCmpEnable();
			}
			else {
				editPanel.setEditorPaneEditable();
				editPanel.getBtnEdit().setText("Editing...");
				model.setIsModified(true);
				// remove highlights 
		        mainController.leftController.getEditPanel().getEditorPane().getHighlighter().removeAllHighlights();
		        mainController.rightController.getEditPanel().getEditorPane().getHighlighter().removeAllHighlights();
		        
		        // remove highlight list
		        mainController.highlighterLeftList.clear();
		        mainController.highlighterRightList.clear();
		 

		        // remove node list 
		        mainController.model.getResultList().clear();
		        mainController.model.getLeftList().clear();
		        mainController.model.getRightList().clear();
		        
		        // set node number zero
		        mainController.model.setNodeNumZero();
		 
		        // set copy up down buttons disable
				mainController.model.setIsCompared(false);
				mainController.view.getMergePanel().setBtnsUnEnable();
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
	public void updateEditPanel() {
		if(this.model.getIsModified()) {
			this.getEditPanel().setContent(this.model.getSB());
		}

	}

}
