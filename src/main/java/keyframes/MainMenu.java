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
		
		JPanel leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension(400,400));
		JPanel rightPanel = new JPanel();
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
		
		gbc.insets = new Insets(10,10,10,10);
		
		JButton newProjectButton = new JButton("New Project");
		newProjectButton.setEnabled(true);
		newProjectButton.setPreferredSize(new Dimension(200, 80));
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
		loadProjectButton.setPreferredSize(new Dimension(200, 80));
		loadProjectButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Session session;
				try {
					System.out.println("Loading project...");
					final JFileChooser fc = new JFileChooser();
					int returnVal = fc.showOpenDialog(loadProjectButton);
					
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						String l = Utils.getExtension(file);
						if(Utils.getExtension(file).equals("ser")) {
							FileInputStream fileIn = new FileInputStream(file);
					        ObjectInputStream in = new ObjectInputStream(fileIn);
					        session = (Session) in.readObject();
					        in.close();
					        fileIn.close();
					        parent.getContentPane().removeAll();
							MainView mv = new MainView(parent, session);
							mv.buildUI();
						} else {
							System.out.println("Incompatible file");
						}
					} else {
						System.out.println("Loading aborted");
					}
					
				} catch (Exception ex) {
					System.out.println("Error loading file");
					session = new Session();
				}
				System.out.println("Loaded project");
			}
		});
		gbc.gridy = 1;
		menuPanel.add(loadProjectButton, gbc);
		
		JButton settingsButton = new JButton("Settings");
		settingsButton.setEnabled(true);
		settingsButton.setPreferredSize(new Dimension(200, 80));
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
