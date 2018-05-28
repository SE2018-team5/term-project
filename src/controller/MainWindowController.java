package controller;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

import model.MainWindowModel;
import view.MainWindowView;

public class MainWindowController {
	
	
	MainWindowView view;
	MainWindowModel model;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindowView v = new MainWindowView();
					MainWindowModel m = new MainWindowModel();
					MainWindowController controller = new MainWindowController(v, m);
					v.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	MainWindowController(MainWindowView v, MainWindowModel m){
		this.view = v;
		this.model = m;
		
		
		EditPanelController leftController = new EditPanelController(view.getLeftPanel(),m);
		EditPanelController rightController = new EditPanelController(view.getRightPanel(),m);
		this.view.getMergePanel().addCmpActionListener(new CmpActionListener());
		this.view.getMergePanel().addCopyToLeftActionListener(new CopyToLeftActionListener());
		this.view.getMergePanel().addCopyToRightActionListener(new CopyToRightActionListener());
	}
	
	
	
	class CmpActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			MainWindowModel.compared();
		}
		
	}
	class CopyToLeftActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(!MainWindowModel.getIsCompared()) {
				int answer = JOptionPane.showConfirmDialog(null, 
						"Files are not compared yet. Still want to merge?", 
						"Warning", 
						JOptionPane.WARNING_MESSAGE);
				if(answer == JOptionPane.OK_OPTION) {
					
				}
			}
		}
		
	}
	class CopyToRightActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(!MainWindowModel.getIsCompared()) {
				int answer = JOptionPane.showConfirmDialog(null, 
						"Files are not compared yet. Still want to merge?", 
						"Warning", 
						JOptionPane.WARNING_MESSAGE);

			}
		}
		
	}
}
