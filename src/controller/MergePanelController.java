package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import view.MergePanel;

public class MergePanelController {
	private MergePanel mergePanel;
	
	MergePanelController(MergePanel m){
		this.mergePanel = m;
		
		this.mergePanel.addCmpActionListener(new CmpActionListener());
		this.mergePanel.addCopyToLeftActionListener(new CopyToLeftActionListener());
		this.mergePanel.addCopyToRightActionListener(new CopyToRightActionListener());
	}
	
	class CmpActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class CopyToLeftActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class CopyToRightActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
}
