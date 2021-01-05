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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import settings.Settings;
import settings.SettingsView;

public class MainMenu implements Serializable {
	
	private static final long serialVersionUID = -3854588841281613072L;

	private JPanel menuPanel;
	private JFrame parent;
	private Settings settings;
	
	public MainMenu(JFrame parent, Settings settings) {
		this.parent = parent;
		this.settings = settings;
		menuPanel = new JPanel();
		this.parent.add(menuPanel, BorderLayout.CENTER);
	}
	
	public void buildUI() {
		
		int lpw = MagicValues.mainMenuLeftPanelDefaultPreferredWidth;
		int lph = MagicValues.mainMenuLeftPanelDefaultPreferredHeight;
		int rpw = MagicValues.mainMenuLeftPanelDefaultPreferredWidth;
		int rph = MagicValues.mainMenuLeftPanelDefaultPreferredHeight;
		
		JPanel leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension(lpw,lph));
		JPanel rightPanel = new JPanel();
		rightPanel.setPreferredSize(new Dimension(rpw,rph));
		parent.add(leftPanel, BorderLayout.LINE_START);
		parent.add(rightPanel, BorderLayout.LINE_END);
		
		menuPanel.setLayout(new GridBagLayout());
		menuPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		menuPanel.revalidate();
		menuPanel.repaint();
		menuPanel.setBackground(Color.gray);
		
		buildButtons();
		parent.revalidate();
		parent.repaint();
	}
	
	private void buildButtons() {
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		int iSize = MagicValues.mainMenuInsetsDefaultSize;
		int bW = MagicValues.mainMenuButtonsDefaultWidth;
		int bH = MagicValues.mainMenuButtonsDefaultHeight;
		
		
		gbc.insets = new Insets(iSize, iSize, iSize, iSize);
		
		JButton newProjectButton = new JButton("New Project");
		newProjectButton.setEnabled(true);
		newProjectButton.setPreferredSize(new Dimension(bW, bH));
		newProjectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.getContentPane().removeAll();
				MainView mv = new MainView(parent, new Session(settings));
				mv.buildUI();
			}
		});
		
		
		gbc.gridy = 0;
		menuPanel.add(newProjectButton, gbc);

		JButton loadProjectButton = new JButton("Load Project");
		loadProjectButton.setEnabled(true);
		loadProjectButton.setPreferredSize(new Dimension(bW, bH));
		loadProjectButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Utils.openFile(e, null, menuPanel, parent);
			}
		});
		gbc.gridy = 1;
		menuPanel.add(loadProjectButton, gbc);
		
		JButton settingsButton = new JButton("Settings");
		settingsButton.setEnabled(true);
		settingsButton.setPreferredSize(new Dimension(bW, bH));
		settingsButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				parent.getContentPane().removeAll();
				
				settings = Utils.getSettings();
				SettingsView sv = new SettingsView(parent, settings);
				sv.buildUI();
			}
			
		});
		gbc.gridy = 2;
		menuPanel.add(settingsButton, gbc);

	}

}
