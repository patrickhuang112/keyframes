package keyframes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import javax.swing.JFrame;

public class Main {
	
	public static JFrame window;
	
	private static JFrame createWindow() {
		return new JFrame();
	}
	
	public static void main(String[] args) {
		
		window = createWindow();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setTitle("Keyframes");
		window.setVisible(true);
		window.setBackground(Color.RED);
		
		window.setMinimumSize(new Dimension(1600,900));
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		MainMenu menu = new MainMenu(window);
		menu.buildUI();
		
		System.out.println("App Started...");
	}

}
