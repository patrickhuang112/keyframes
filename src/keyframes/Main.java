package keyframes;

import java.awt.GridLayout;

import javax.swing.JFrame;

public class Main {
	
	public static JFrame window;
	
	private static JFrame createWindow() {
		return new JFrame();
	}
	
	public static void main(String[] args) {
		
		window = createWindow();
		Menu menu = new Menu(window);
		menu.buildUI();
		
		
		
		System.out.println("LOL");
		
	}

}
