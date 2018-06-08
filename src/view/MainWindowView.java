package view;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
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


