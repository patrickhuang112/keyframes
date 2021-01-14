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
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingWorker;

import org.jcodec.api.awt.AWTSequenceEncoder;

import external.GifSequenceWriter;
import settings.Settings;
import ui.MainView;


public class Utils {
	
	//KeyFrame Project File
	public static final String fileExtension = ".kfpf";

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
	
	public static void newFile(Session oldSession, JFrame frame) {
		// DO STUFF WITH OLD SESSION (NOT IMPLEMENTED YET)
		
		frame.getContentPane().removeAll();
		MainView.createNewInstance();
		
		System.out.println("New project created");
	}
	
	public static void openFile(Session oldSession, JFrame frame) {
		// DO STUFF WITH OLD SESSION (NOT IMPLEMENTED YET)
		
		
		try {
			System.out.println("Loading project...");
			final JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(frame);
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				if(Utils.getExtension(file).equals(Utils.fileExtension)) {
					FileInputStream fileIn = new FileInputStream(file);
			        ObjectInputStream in = new ObjectInputStream(fileIn);
			        SessionSave ss = (SessionSave) in.readObject();
			        in.close();
			        fileIn.close();
			        Session newSession = new Session(ss);
			        frame.getContentPane().removeAll();
					MainView.createNewInstance();
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
	
	public static void saveAsFile(Session session, JFrame frame) {
		try {
			System.out.println("Loading project...");
			final JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Save session");
			int returnVal = fc.showSaveDialog(frame);
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				String savePath = file.getAbsolutePath() + Utils.fileExtension;
				session.setSavePath(savePath);
				SessionSave ss = new SessionSave(session);
				FileOutputStream fos = new FileOutputStream(savePath);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				// write object to file
				oos.writeObject(ss);
				System.out.println("Saving Successful!");
				// closing resources
				oos.close();
				fos.close();
				
			} else {
				System.out.println("Saving aborted");
			}
			
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			System.out.println("Error saving file");
			session = new Session();
		}
	}
	
	public static void saveFile(Session session, JFrame frame) {
		
		if(session.getSavePath() == null) {
			Utils.saveAsFile(session, frame);
		} else {
			try {
				SessionSave ss = new SessionSave(session);
				assert(ss.savePath.equals(session.getSavePath()));
				FileOutputStream fos = new FileOutputStream(ss.savePath);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				// write object to file
				oos.writeObject(ss);
				System.out.println("Saving Successful!");
				// closing resources
				oos.close();
				fos.close();
			} catch (Exception e) {
				System.out.println("SAVING FAILED");
				System.out.println(e.getMessage());
			}
		}
	}
	
	public static void renderMP4(Session session, JFrame frame, int fps) {
		SwingWorker sw = new SwingWorker() {
			@Override
			protected Object doInBackground() throws Exception {
				try {
					System.out.println("Rendering video...");
					final JFileChooser fc = new JFileChooser();
					fc.setDialogTitle("Render video");
					int returnVal = fc.showSaveDialog(frame);
					
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						String filePath = file.getAbsolutePath() + ".mp4";
						File newFile = new File(filePath);
						
						session.setProgressBarVisible(true);
						
						AWTSequenceEncoder enc = AWTSequenceEncoder.createSequenceEncoder(newFile, fps);
						HashMap<Integer, BufferedImage> images = session.getImages();
						
						for(int i = 0; i <= session.getLongestTimepoint(); i++) {
							BufferedImage img = images.get(i);
							enc.encodeImage(img);
							
							//Update progress bar
							session.updateProgressBar(i);
						}
						enc.finish();
						
						// After 3 seconds of finishing, toggle off the progress bar and reset it to 0
					    new Timer().schedule( 
					            new TimerTask() {
					                @Override
					                public void run() {
					                    session.setProgressBarVisible(false);
					                    session.resetProgressBar();
					                }
					            }, MagicValues.utilsRenderDefaultTimeBeforeProgressBarDisappearsAfterRender);
						
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
	
	public static void renderGIF(Session session, JFrame frame, int fps) {
		SwingWorker sw = new SwingWorker() {
			@Override
			protected Object doInBackground() throws Exception {
				try {
					System.out.println("Rendering gif...");
					final JFileChooser fc = new JFileChooser();
					fc.setDialogTitle("Render gif");
					int returnVal = fc.showSaveDialog(frame);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						String filePath = file.getAbsolutePath() + ".gif";
						File newFile = new File(filePath);
						
						session.setProgressBarVisible(true);
						
						HashMap<Integer, BufferedImage> images = session.getImages();
						ImageOutputStream output = new FileImageOutputStream(newFile);
						
						int millisecondsInSecond = 1000;
						int timeBetweenFrames = millisecondsInSecond / fps;
						
						//Don't change unless I change how buffered images are made.1
						int imgType = BufferedImage.TYPE_INT_RGB;
						
						// create a gif sequence with the type of the first image, 1 second
					    // between frames, which loops continuously
						
						GifSequenceWriter writer = 
								new GifSequenceWriter(output, imgType, timeBetweenFrames, true);
					      
					    // write images to the sequence
						for(int i = 0; i <= session.getLongestTimepoint(); i++) {
							BufferedImage img = images.get(i);
							writer.writeToSequence(img);
							
							//Update progress bar
							session.updateProgressBar(i);
						}
						
					    writer.close();
					    output.close();
					    
					    // After 3 seconds of finishing, toggle off the progress bar and reset it to 0
					    new Timer().schedule( 
					            new TimerTask() {
					                @Override
					                public void run() {
					                    session.setProgressBarVisible(false);
					                    session.resetProgressBar();
					                }
					            }, MagicValues.utilsRenderDefaultTimeBeforeProgressBarDisappearsAfterRender);
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
