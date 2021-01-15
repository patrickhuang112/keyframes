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
import ui.MainView;

import com.formdev.flatlaf.*;

public class Main {
	
	
	public static void main(String[] args) {
		
		initializeLookAndFeel();
		Controller.createController();
		Controller.getController().initialize();
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
