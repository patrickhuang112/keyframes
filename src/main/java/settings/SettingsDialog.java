package settings;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSlider;

import factories.SliderFactory;

public class SettingsDialog extends JDialog {
	
	private Settings settings;
	
	public SettingsDialog(Frame parent, String title, Settings settings) {
		super(parent, title, true);
		this.settings = settings;
	}
	
	public void buildUI() {
		setLayout( new GridBagLayout() );  

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10,10,10,10);
		
		JSlider brushSizeSlider = SliderFactory.createBasicSettingsSlider(settings.getBrushSize(), 
				settings.brushMin, settings.brushMax);
		
		JSlider eraserSizeSlider = SliderFactory.createBasicSettingsSlider(settings.getEraserSize(), 
				settings.eraserMin, settings.eraserMax);
		
		JSlider fpsSlider = SliderFactory.createBasicSettingsSlider(settings.getFps(), 
				settings.fpsMin, settings.fpsMax);
		
		JSlider compositionLengthSlider = SliderFactory.createBasicFiveTickSlider(settings.getCompLength(), 
				settings.lengthMin, settings.lengthMax);
		
		
		gbc.gridy = 0;
		gbc.gridx = 0;
		gbc.weightx = .5;
		JLabel brushText = new JLabel("Brush Size");
		add(brushText, gbc);
		
		gbc.gridx = 1;
		gbc.weightx = .5;
		add(brushSizeSlider, gbc);
		
		gbc.gridy = 1;
		
		gbc.gridx = 0;
		gbc.weightx = .5;
		JLabel eraserText = new JLabel("Eraser Size");
		add(eraserText, gbc);
		
		gbc.gridx = 1;
		gbc.weightx = .5;
		add(eraserSizeSlider, gbc);
		
		gbc.gridy = 2;
		
		gbc.gridx = 0;
		gbc.weightx = .5;
		JLabel fpsText = new JLabel("Frames per second");
		add(fpsText, gbc);
		
		gbc.gridx = 1;
		gbc.weightx = .5;
		add(fpsSlider, gbc);
		
		gbc.gridy = 3;
		gbc.gridx = 0;
		gbc.weightx = .5;
		JLabel compText = new JLabel("Composition Length");
		add(compText, gbc);
		
		
		gbc.gridx = 1;
		gbc.weightx = .5;
		add(compositionLengthSlider, gbc);
		
		JButton okButton = new JButton ("OK");  
        okButton.addActionListener ( new ActionListener()  
        {  
            public void actionPerformed( ActionEvent e )  
            {  
                setVisible(false);  
            }  
        });  
		
        JButton applyButton = new JButton ("Apply");  
        applyButton.addActionListener ( new ActionListener()  
        {  
            public void actionPerformed( ActionEvent e )  
            {  
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
    				
            }  
        });
		
        gbc.gridy = 4;
		gbc.gridx = 0;
		gbc.weightx = .5;
        add(okButton, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = .5;
        add(applyButton, gbc);
        
        setSize(500,400);    
        setVisible(true);  
	}
}
