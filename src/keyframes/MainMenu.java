package keyframes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainMenu {
	
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JPanel menuPanel;
	private JFrame parent;
	
	
	private JButton settingsButton;
	private JButton newProjectButton;
	private JButton loadProjectButton;
	
	public MainMenu(JFrame parent) {
		this.parent = parent;
		menuPanel = new JPanel();
		this.parent.add(menuPanel, BorderLayout.CENTER);
	}
	
	public void buildUI() {
		
		
		
		
		leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension(400,400));
		rightPanel = new JPanel();
		rightPanel.setPreferredSize(new Dimension(400,400));
		parent.add(leftPanel, BorderLayout.LINE_START);
		parent.add(rightPanel, BorderLayout.LINE_END);
		
		menuPanel.setLayout(new GridBagLayout());

		
		menuPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		menuPanel.revalidate();
		menuPanel.repaint();
		menuPanel.setBackground(Color.red);
		
		buildButtons();
		parent.revalidate();
		parent.repaint();
	}
	
	private void buildButtons() {
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		// 1 column
		//gbc.gridwidth = 1;
		// 3 rows
		//gbc.gridheight = 3;
		gbc.insets = new Insets(10,10,10,10);
		
		
		newProjectButton = new JButton("New Project");
		newProjectButton.setEnabled(true);
		newProjectButton.setPreferredSize(new Dimension(200, 80));
		newProjectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.getContentPane().removeAll();
				MainView mv = new MainView(parent);
				mv.buildUI();
			}
		});
		
		
		gbc.gridy = 0;
		menuPanel.add(newProjectButton, gbc);

		
		
		loadProjectButton = new JButton("Load Project");
		loadProjectButton.setEnabled(true);
		loadProjectButton.setPreferredSize(new Dimension(200, 80));
		loadProjectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("CLICKED BRO");
			}
		});
		gbc.gridy = 1;
		menuPanel.add(loadProjectButton, gbc);
		
		settingsButton = new JButton("Settings");
		settingsButton.setEnabled(true);
		settingsButton.setPreferredSize(new Dimension(200, 80));
		gbc.gridy = 2;
		menuPanel.add(settingsButton, gbc);

	}
}
