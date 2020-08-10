package keyframes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class SettingsView {

	
	private JFrame parent;
	private JButton backButton;

	public SettingsView(JFrame parent) {
		this.parent = parent;
	}
	
	public void buildUI() {
		buildBackButton();
	}
	
	private void buildBackButton() {
		JButton backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parent.getContentPane().removeAll();
				MainMenu menu = new MainMenu(parent);
				menu.buildUI();
			}
		});
	}
}
