package keyframes;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

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
		
		MainMenu menu = new MainMenu(window);
		menu.buildUI();
		
		System.out.println("LOL");
	}

}
