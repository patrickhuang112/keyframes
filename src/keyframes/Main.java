package keyframes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;

public class Main {
	
	public static JFrame window;
	
	private static JFrame createWindow() {
		return new JFrame();
	}
	
	public static void main(String[] args) {
		
		Session currentSession = new Session();
		
		// FIX LATER WITH SETTINGS OR DEFAULTS
		currentSession.setBrushSize(5);
		
		
		window = createWindow();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setTitle("Keyframes");
		window.setVisible(true);
		window.setBackground(Color.RED);
		
		window.setMinimumSize(new Dimension(1600,900));
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		MainMenu menu = new MainMenu(window, currentSession);
		menu.buildUI();
		
		System.out.println("App Started...");
	}

}
