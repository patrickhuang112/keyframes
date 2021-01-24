package keyframes;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;

import org.jcodec.api.awt.AWTSequenceEncoder;

import external.GifSequenceWriter;
import settings.Settings;
import ui.MainView;


public class Utils {
	
	//KeyFrame Project File
	public static final String fileExtension = "kfpf";

	private static void setFileChooserToViewDetailsAsDefault(JFileChooser fc) {
		Action details = fc.getActionMap().get("viewTypeDetails");
		details.actionPerformed(null);
	}
	
	private static void setFileChooserToOnlyAllowValidFileExtension(JFileChooser fc, String ext, String desc) {
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileFilter(new FileFilter() {

			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
			           return true;
			       } else {
			           String filename = f.getName().toLowerCase();
			           return filename.endsWith(ext);
			       }
			}

			@Override
			public String getDescription() {
				return desc;
			}
		
		});
	}
	
	public static Settings getSettings() {
		Settings settings;
		try {
			String file = Settings.settingsPath;
			FileInputStream fileIn = new FileInputStream(file);
	        ObjectInputStream in = new ObjectInputStream(fileIn);
	        settings = (Settings)in.readObject();
	        System.out.println("Successfully loaded settings");
	        in.close();
	        fileIn.close();
		} catch (Exception e) {
			System.out.println("Settings don't preexist");
			settings = new Settings();
		}
		return settings;
		
	}
	
	public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
	
	public static void newFile(JFrame frame) {
		Controller.getController().newSessionLayers();
		System.out.println("New project created");
	}
	
	public static void openFile( JFrame frame) {
		try {
			final JFileChooser fc = new JFileChooser();
			
			fc.setDialogTitle("Open project");
			Utils.setFileChooserToOnlyAllowValidFileExtension(fc, ".kfpf", "Keyframes project file (*.kfpf)");
			Utils.setFileChooserToViewDetailsAsDefault(fc);
			
			int returnVal = fc.showOpenDialog(frame);
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				Controller.getController().prepareOpeningFileProgressBar();
				File file = fc.getSelectedFile();
				if(Utils.getExtension(file).equals(Utils.fileExtension)) {
					FileInputStream fileIn = new FileInputStream(file);
			        ObjectInputStream in = new ObjectInputStream(fileIn);
			        SessionSave ss = (SessionSave) in.readObject();
			        in.close();
			        fileIn.close();
			        Controller.getController().newSessionFromSessionSave(ss);
			        Controller.getController().displayCompletedIndeterminateProgressBar();
				} else {
					System.out.println("Incompatible file");
				}
			} else {
				System.out.println("Loading aborted");
			}
			
		} catch (Exception ex) {
			System.out.println("Error loading file");
		}
		System.out.println("Loaded project");
	}
	
	public static void saveAsFile( JFrame frame) {
		try {
			final JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Save project as...");
			Utils.setFileChooserToOnlyAllowValidFileExtension(fc, ".kfpf", "Keyframes project file (*.kfpf)");
			Utils.setFileChooserToViewDetailsAsDefault(fc);
			int returnVal = fc.showSaveDialog(frame);
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				Controller.getController().prepareSavingFileProgressBar();
				File file = fc.getSelectedFile();
				String savePath = file.getAbsolutePath() + "." + Utils.fileExtension;
				Controller.getController().setSavePath(savePath);
				SessionSave ss = Controller.getController().createCurrentSessionSave();
				FileOutputStream fos = new FileOutputStream(savePath);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				// write object to file
				oos.writeObject(ss);
				// closing resources
				oos.close();
				fos.close();
				
				Controller.getController().displayCompletedIndeterminateProgressBar();
				
			} else {
				System.out.println("Saving aborted");
			}
			
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			System.out.println("Error saving file");
		}
	}
	
	public static void saveFile(JFrame frame) {
		if(Controller.getController().getSavePath() == null) {
			Utils.saveAsFile(frame);
		} else {
			try {
				Controller.getController().prepareSavingFileProgressBar();
				SessionSave ss = Controller.getController().createCurrentSessionSave();
				FileOutputStream fos = new FileOutputStream(ss.savePath);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				// write object to file
				oos.writeObject(ss);
				// closing resources
				oos.close();
				fos.close();
				Controller.getController().displayCompletedIndeterminateProgressBar();
			} catch (Exception e) {
				System.out.println("SAVING FAILED");
				System.out.println(e.getMessage());
			}
		}
	}
	
	public static void renderMP4(JFrame frame, int fps) {
		SwingWorker sw = new SwingWorker() {
			@Override
			protected Object doInBackground() throws Exception {
				try {
					System.out.println("Rendering video...");
					final JFileChooser fc = new JFileChooser();
					
					fc.setDialogTitle("Render video");
					Utils.setFileChooserToOnlyAllowValidFileExtension(fc, ".mp4", "MP4 Video");
					Utils.setFileChooserToViewDetailsAsDefault(fc);
					
					int returnVal = fc.showSaveDialog(frame);
					
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						String filePath = file.getAbsolutePath() + ".mp4";
						File newFile = new File(filePath);
						
						Controller.getController().prepareRenderingProgressBar();
						
						AWTSequenceEncoder enc = AWTSequenceEncoder.createSequenceEncoder(newFile, fps);
						HashMap<Integer, BufferedImage> images = Controller.getController().getImages();
						
						for(int i = 0; i <= Controller.getController().getLongestTimepoint(); i++) {
							BufferedImage img = images.get(i);
							enc.encodeImage(img);
							
							//Update progress bar
							Controller.getController().updateProgressBar(i);
						}
						enc.finish();
						Controller.getController().displayCompletedDeterminateProgressBar();
						
						
					} else {
						System.out.println("Rendering video aborted");
					}
					
				} catch (Exception ex) {
					System.out.println("Error rendering video");
				}
				return "VIDEO FINISHED";
			}
			
			@Override
			protected void done() {
				System.out.println("Rendering mp4 finished");
			}
		};
		
		sw.execute();
			
	}
	
	public static void renderGIF(JFrame frame, int fps) {
		SwingWorker sw = new SwingWorker() {
			@Override
			protected Object doInBackground() throws Exception {
				try {
					System.out.println("Rendering gif...");
					final JFileChooser fc = new JFileChooser();
					
					fc.setDialogTitle("Render gif");
					Utils.setFileChooserToOnlyAllowValidFileExtension(fc, ".gif", "GIF Image");
					Utils.setFileChooserToViewDetailsAsDefault(fc);
					
					int returnVal = fc.showSaveDialog(frame);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						String filePath = file.getAbsolutePath() + ".gif";
						File newFile = new File(filePath);
						
						Controller.getController().prepareRenderingProgressBar();
						
						HashMap<Integer, BufferedImage> images = Controller.getController().getImages();
						ImageOutputStream output = new FileImageOutputStream(newFile);
						
						int millisecondsInSecond = 1000;
						int timeBetweenFrames = millisecondsInSecond / fps;
						
						//Don't change unless I change how buffered images are made.1
						int imgType = BufferedImage.TYPE_4BYTE_ABGR;
						
						// create a gif sequence with the type of the first image, 1 second
					    // between frames, which loops continuously
						
						GifSequenceWriter writer = 
								new GifSequenceWriter(output, imgType, timeBetweenFrames, true);
					      
					    // write images to the sequence
						int end = Controller.getController().getLongestTimepoint();
						for(int i = 0; i <= end; i++) {
							BufferedImage img = images.get(i);
							writer.writeToSequence(img);
							
							//Update progress bar
							Controller.getController().updateProgressBar(i);
						}
						
					    writer.close();
					    output.close();
					    
					    Controller.getController().displayCompletedDeterminateProgressBar();
					    
					} else {
						System.out.println("Rendering gif aborted");
					}
					
				} catch (Exception ex) {
					System.out.println("Error rendering");
				}
				return "GIF FINISHED";
			}
			
			@Override
			protected void done() {
				System.out.println("Rendering gif finished");
			}
		};
		
		sw.execute();
	}
}
