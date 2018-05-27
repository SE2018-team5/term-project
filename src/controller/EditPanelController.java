package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFileChooser;

import model.MainWindowModel;
import view.EditPanel;

public class EditPanelController {

	private EditPanel editPanel;
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
			if (result == JFileChooser.APPROVE_OPTION) // 파일을 선택하고 열었을때 이벤트
			{
				try {
					File file = editPanel.getFileDlg().getSelectedFile();
					editPanel.getFilePathTextField().setText(file.getPath());
					Scanner scan = new Scanner(file);
					StringBuffer sb = new StringBuffer();
					while (scan.hasNextLine()) {
						sb.append(scan.nextLine() + "\r\n");
					}
					editPanel.setContent(sb.toString() + "\r\n");
					editPanel.setEditorPaneNotEditable(); // 수정 불가

					editPanel.getBtnSaveAs().setEnabled(true);
					editPanel.getBtnEdit().setEnabled(true);
					
					MainWindowModel.loaded();
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
}
