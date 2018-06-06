package view;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;


/**
 * Merge Panel Class
 * cmp, cpy2left, cpy2right Button needed.
 * copy buttons should ask user to overwrite contents unless compare operation is done.
 */
public class MergePanel extends JPanel{
	private JButton btnCmp;
	private JButton btnCpy2Left;
	private JButton btnCpy2Right;
	private JButton btnUp;
	private JButton btnDown;
	
	MergePanel(){
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		// compare button
		btnCmp = new JButton("Compare");
		this.add(btnCmp);
		btnCmp.setAlignmentX(Component.CENTER_ALIGNMENT);

		// merging button
		btnCpy2Left = new JButton("<- Copy to Left");
		btnCpy2Right = new JButton("Copy to Right ->");
		this.add(btnCpy2Left);
		btnCpy2Left.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(btnCpy2Right);
		btnCpy2Right.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JPanel updownPan = new JPanel();
		updownPan.setLayout(new GridLayout(1,2));
		
		btnUp = new JButton("UP");
		updownPan.add(btnUp);
		btnUp.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnDown = new JButton("DOWN");
		updownPan.add(btnDown);
		btnDown.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		this.add(updownPan);
		
		btnCmp.setEnabled(false);
		btnCpy2Left.setEnabled(false);
		btnCpy2Right.setEnabled(false);
		btnUp.setEnabled(false);
		btnDown.setEnabled(false);
	}
	
	public void setBtnCmpEnable() {
		this.btnCmp.setEnabled(true);
	}
	
	public void setBtnsEnable() {
		this.btnCpy2Left.setEnabled(true);
		this.btnCpy2Right.setEnabled(true);
		btnUp.setEnabled(true);
		btnDown.setEnabled(true);
	}
	public void setBtnsUnEnable() {
		this.btnCmp.setEnabled(false);
		this.btnCpy2Left.setEnabled(false);
		this.btnCpy2Right.setEnabled(false);
		btnUp.setEnabled(false);
		btnDown.setEnabled(false);
	}

	public void addCmpActionListener(ActionListener CmpActionListener) {
		this.btnCmp.addActionListener(CmpActionListener);
	}
	public void addCopyToLeftActionListener(ActionListener copyToLeftActionListener) {
		this.btnCpy2Left.addActionListener(copyToLeftActionListener);
	}
	public void addCopyToRightActionListener(ActionListener copyToRightActionListener) {
		this.btnCpy2Right.addActionListener(copyToRightActionListener);
	}
	public void addUpActionListener(ActionListener upActionListener) {
		this.btnUp.addActionListener(upActionListener);
	}
	public void addDownActionListener(ActionListener downActionListener) {
		this.btnDown.addActionListener(downActionListener);
	}

}
