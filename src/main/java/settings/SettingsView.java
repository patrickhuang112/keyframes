package settings;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

import keyframes.MainMenu;
import ui.slider.SliderFactory;

public class SettingsView {

	// CURRENTLY NOT IN USE AT ALL.
	/* 
	private JFrame parent;
	private Settings settings;

	private JSlider brushSizeSlider;
	private JSlider eraserSizeSlider;
	private JSlider fpsSlider;
	private JSlider compositionLengthSlider;
	
	public SettingsView(JFrame parent, Settings settings) {
		this.settings = settings;
		this.parent = parent;
	}
	
	public void buildUI() {
		JPanel leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension(400,400));
		JPanel rightPanel = new JPanel();
		rightPanel.setPreferredSize(new Dimension(400,400));
		parent.add(leftPanel, BorderLayout.LINE_START);
		parent.add(rightPanel, BorderLayout.LINE_END);
		buildButtons();
		buildSettings();
		parent.repaint();
		parent.revalidate();
	}
	
	private void buildButtons() {
		JButton backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					FileOutputStream fos = new FileOutputStream(Settings.settingsPath);
					ObjectOutputStream oos = new ObjectOutputStream(fos);
					// write object to file
					oos.writeObject(settings);
					System.out.println("Settings saving successful!");
					// closing resources
					oos.close();
					fos.close();
				} catch (Exception ex) {
					System.out.println("Failed to save settings");
				}
				
				parent.getContentPane().removeAll();
				MainMenu menu = new MainMenu(parent, settings);
				menu.buildUI();
			}
		});
		backButton.setVisible(true);
		backButton.setPreferredSize(new Dimension(100,40));
		
		JButton applyButton = new JButton("Apply");
		applyButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				applySettings();
			}
		});
		applyButton.setVisible(true);
		applyButton.setPreferredSize(new Dimension(100,40));
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 50));
		buttonPanel.setPreferredSize(new Dimension(100,200));
		buttonPanel.add(backButton);
		buttonPanel.add(applyButton);
		this.parent.add(buttonPanel, BorderLayout.PAGE_END);
	}
	
	private void buildSettings() {

		JPanel settingsPanel = new JPanel();
		settingsPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10,10,10,10);
		
		brushSizeSlider = SliderFactory.createBasicSettingsSlider(settings.getBrushSize(), 
				settings.brushMin, settings.brushMax);
		
		eraserSizeSlider = SliderFactory.createBasicSettingsSlider(settings.getEraserSize(), 
				settings.eraserMin, settings.eraserMax);
		
		fpsSlider = SliderFactory.createBasicSettingsSlider(settings.getFps(), 
				settings.fpsMin, settings.fpsMax);
		
		
		compositionLengthSlider = SliderFactory.createBasicFiveTickSlider(settings.getCompLength(), 
				settings.lengthMin, settings.lengthMax);
		
		
		gbc.gridy = 0;
		gbc.gridx = 0;
		gbc.weightx = .5;
		JLabel brushText = new JLabel("Brush Size");
		settingsPanel.add(brushText, gbc);
		
		gbc.gridx = 1;
		gbc.weightx = .5;
		settingsPanel.add(brushSizeSlider, gbc);
		
		gbc.gridy = 1;
		
		gbc.gridx = 0;
		gbc.weightx = .5;
		JLabel eraserText = new JLabel("Eraser Size");
		settingsPanel.add(eraserText, gbc);
		
		gbc.gridx = 1;
		gbc.weightx = .5;
		settingsPanel.add(eraserSizeSlider, gbc);
		
		gbc.gridy = 2;
		
		gbc.gridx = 0;
		gbc.weightx = .5;
		JLabel fpsText = new JLabel("Frames per second");
		settingsPanel.add(fpsText, gbc);
		
		gbc.gridx = 1;
		gbc.weightx = .5;
		settingsPanel.add(fpsSlider, gbc);
		
		gbc.gridy = 3;
		gbc.gridx = 0;
		gbc.weightx = .5;
		JLabel compText = new JLabel("Composition Length");
		settingsPanel.add(compText, gbc);
		
		
		gbc.gridx = 1;
		gbc.weightx = .5;
		settingsPanel.add(compositionLengthSlider, gbc);
		
		parent.add(settingsPanel, BorderLayout.CENTER);
	}
	
	private void applySettings() {
		int brushSizeVal = brushSizeSlider.getValue();
		int eraserSizeVal = eraserSizeSlider.getValue();
		int fpsVal = fpsSlider.getValue();
		int compLengthVal = compositionLengthSlider.getValue();
		
		if (brushSizeVal == 0) {
			brushSizeVal++;
			brushSizeSlider.setValue(brushSizeVal);
		}
		if (eraserSizeVal == 0) {
			eraserSizeVal++;
			eraserSizeSlider.setValue(eraserSizeVal);
		}
		if (fpsVal == 0) {
			fpsVal++;
			fpsSlider.setValue(fpsVal);
		}
		if (compLengthVal == 0) {
			compLengthVal++;
			compositionLengthSlider.setValue(compLengthVal);
		}
		
		settings.setBrushSize(brushSizeVal);
		settings.setEraserSize(eraserSizeVal);
		settings.setFps(fpsVal);
		settings.setCompLength(compLengthVal);
		refreshUI();
	}
	
	private void refreshUI() {
		// Empty for now
	}
	*/
}
