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

import model.MainWindowModel;
import view.EditPanel;

public class EditPanelController {

	private EditPanel editPanel;
	public StringBuffer text;
	
	FileReader fr;
	BufferedReader br = null;
	
	EditPanelController(EditPanel e, MainWindowModel m){
		this.editPanel = e;
		
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
					

					
//					File file = editPanel.getFileDlg().getSelectedFile();
//					editPanel.getFilePathTextField().setText(file.getPath());
//					Scanner scan = new Scanner(file);
//					StringBuffer sb = new StringBuffer();
//					while (scan.hasNextLine()) {
//						sb.append(scan.nextLine() + "\n");
//					}
//					editPanel.setContent(sb.toString() + "\n");
//					editPanel.setEditorPaneNotEditable(); // 수정 불가
//					text = sb;
//					editPanel.getBtnSaveAs().setEnabled(true);
//					editPanel.getBtnEdit().setEnabled(true);
//					
//					MainWindowModel.loaded();
//					scan.close();
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
			// TODO Auto-generated method stub
			File file = editPanel.getFileDlg().getSelectedFile();

			try {
				if (!file.exists()) {
					file.createNewFile();
				}

				FileWriter fw = new FileWriter(file, false);
				BufferedWriter bw = new BufferedWriter(fw);

				bw.write(editPanel.getContent());
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
			if (editPanel.isEditable())
				editPanel.setEditorPaneNotEditable();
			else
				editPanel.setEditorPaneEditable();
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
}
