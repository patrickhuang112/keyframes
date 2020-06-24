package keyframes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Menu {
	
	private JPanel menuPanel;
	private JFrame parent;
	
	private JButton settingsButton;
	private JButton newProjectButton;
	private JButton loadProjectButton;
	
	public Menu(JFrame parent) {
		this.parent = parent;
		menuPanel = new JPanel();
	}
	
	public void buildUI() {
		
		parent.setMinimumSize(new Dimension(1600,900));
		parent.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		//mainPanel.setBorder(BorderFactory.createEmptyBorder(1000,1000,10,30));
		//menuPanel.setLayout(new GridLayout(2,2));
		buildButtons();
		menuPanel.setPreferredSize(new Dimension(600,400));
		
		parent.add(menuPanel, BorderLayout.CENTER);
		parent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		parent.setTitle("Animator");
		parent.pack();
		parent.setVisible(true);
		
	}
	
	private void buildButtons() {
		newProjectButton = new JButton("New Project");
		newProjectButton.setEnabled(true);
		newProjectButton.setPreferredSize(new Dimension(40, 30));
		menuPanel.add(newProjectButton);
		
		loadProjectButton = new JButton("Load Project");
		loadProjectButton.setEnabled(true);
		loadProjectButton.setPreferredSize(new Dimension(40, 30));
		menuPanel.add(loadProjectButton);
	}
}
