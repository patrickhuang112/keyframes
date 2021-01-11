package keyframes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import javax.swing.JFrame;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import settings.Settings;

import com.formdev.flatlaf.*;

public class Main {
	
	
	public static void main(String[] args) {
		
		initializeLookAndFeel();
		
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setTitle("Keyframes");
		window.setVisible(true);
		window.setMinimumSize(new Dimension(MagicValues.windowMinimumWidth, MagicValues.windowMinimumHeight));
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		Settings settings = Utils.getSettings();
		MainView mv = new MainView(window, new Session(settings));
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				mv.buildUI();
				// Once we now have access to the draw panel, we should also update all the draw frames in
				// the layers with the new dimensions
				//getSession().updateDrawFrameDimensions();
			}
			
		});
		System.out.println("App Started...");
	}
	
	
	private static void initializeLookAndFeel() {
		try {
			FlatDarkLaf.install();
			UIManager.setLookAndFeel(new FlatDarkLaf());
		} catch (UnsupportedLookAndFeelException e) {
		       // handle exception
	    }
	}
	
}
