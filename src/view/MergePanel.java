package view;

import java.awt.Component;
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
	

}
