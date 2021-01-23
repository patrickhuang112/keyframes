package datatypes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

// A Draw instruction is the buffered image drawn on the screen.

// A lot of commented out stuff is for if I ever want to use the pixelArray stuff again.
public class DrawFrame extends BufferedImage {
	private int[][] pixelArray;
	private int width;
	private int height;
	// R,G,B,A
	private final int numChannels = 4;
	
	public DrawFrame(int width, int height) {
		this(width, height, BufferedImage.TYPE_4BYTE_ABGR);
	}
	
	public DrawFrame(int width, int height, int type) {
		super(width, height, type);
		this.width = width;
		this.height = height;
		//findPixelArray();
	}

	/*
	public void refreshPixelArray() {
		byte[] pixels = new byte[width * height * 4]; 
		System.out.println(width);
		System.out.println(height);
		int i = 0;
		for(int ro = 0; ro < height; ro++) {
			for (int c = 0; c < width; c++) {
				int abgr = pixelArray[ro][c];
				byte a = (byte)((abgr >> 24) & (0xFF)); 
				byte b = (byte)((abgr >> 16) & (0xFF));
				byte g = (byte)((abgr >> 8) & (0xFF));
				byte r = (byte)((abgr >> 0) & (0xFF));
				pixels[i] = a;
				pixels[i+1] = b;
				pixels[i+2] = g;
				pixels[i+3] = r;
				i++;
			}
		}
		this.getRaster().setDataElements(0, 0, width, height, pixels);
	}
	*/
	
	public int[][] getPixelArray() {
		int[][] p = new int[getHeight()][getWidth()];
		for(int r = 0; r < getHeight(); r++) {
			for(int c = 0; c < getWidth(); c++) {
				p[r][c] = this.getRGB(c, r);
			}
		}
		return p;		
		
		/*
		byte[] pixels = ((DataBufferByte) getRaster().getDataBuffer()).getData();
		int[] r = new int[pixels.length];
		for (int i = 0; i < pixels.length; i++) {
			r[i] = (int)(pixels[i] & 0xff);
		}
		return r;
		*/
		/*
		int[] result = new int[getWidth() * getHeight()];
		int j = 0;
		for (int i = 0; i < pixels.length; i += numChannels) {
			int argb = 0;
			argb += (((int) pixels[i] & 0xff) << 24); // alpha
            argb += ((int) pixels[i + 1] & 0xff); // blue
            argb += (((int) pixels[i + 2] & 0xff) << 8); // green
            argb += (((int) pixels[i + 3] & 0xff) << 16); // red
            result[j] = argb;
            j++;
		}
		return result;
		*/
	}
	
	/*
	public int[] findPixelArray() {
		pixelArray = new int[height][width];
		byte[] pixels = ((DataBufferByte) getRaster().getDataBuffer()).getData();
		int row = 0;
		int col = 0;
		for (int i = 0; i < pixels.length; i += numChannels) {
			int argb = 0;
			argb += (((int) pixels[i] & 0xff) << 24); // alpha
            argb += ((int) pixels[i + 1] & 0xff); // blue
            argb += (((int) pixels[i + 2] & 0xff) << 8); // green
            argb += (((int) pixels[i + 3] & 0xff) << 16); // red
            pixelArray[row][col] = argb;
			col++;
			if (col == width) {
				col = 0;
				row++;
			}
		}
		return pixelArray;
	}
	*/
	
	public void updateDrawFrameFromPixelArray() {
		int [] newPixels = new int[width * height];
		int i = 0;
		for (int r = 0 ; r < height; r++) {
			for (int c = 0; c < width; c++) {
				newPixels[i] = pixelArray[r][c];
				i++;
			}
		}
		getRaster().setPixels(0, 0, width, height, newPixels);
	}
	
	public DrawFrame deepCopy() {
		DrawFrame b = new DrawFrame(this.getWidth(), this.getHeight(), this.getType());
	    Graphics g = b.getGraphics();
	    g.drawImage(this, 0, 0, null);
	    g.dispose();
	    return b;
	}
	
	public static DrawFrame createResizedCopy(DrawFrame df, int newWidth, int newHeight) {
		Image tmp = df.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
		DrawFrame newDf= new DrawFrame(newWidth, newHeight);
		Graphics2D g2d = newDf.createGraphics();
		
		g2d.drawImage(tmp, 0,0, null);
		g2d.dispose();
		
		return newDf;
	}
	
	public int getABGR(int x, int y) {
		return getRGB(x, y);
		//return pixelArray[y][x];
	}
	
	public int getABGR(Point p) {
		return getRGB(p.x, p.y);
		//return getABGR(p.x,p.y);
	}
	
	public Color getColorAtPoint(int x, int y) {
		//int val = pixelArray[y][x];
		int val = getRGB(x,y);
		return new Color(val, true);
	}
	
	public Color getColorAtPoint(Point p) {
		return getColorAtPoint(p.x,p.y);
	}
	
	public void setColorAtPixelArrayPoint(Color color, int x, int y) {
		/*
		int a = color.getAlpha();
		int b = color.getBlue();
		int g = color.getGreen();
		int r = color.getRed();
		int abgr = (a << 24) + (b << 16) + (g << 8) + r;
		pixelArray[y][x] = abgr;
		*/
		this.setRGB(x, y, color.getRGB());
	}
	
	public void setColorAtPixelArrayPoint(Color color, Point p) {
		setColorAtPixelArrayPoint(color, p.x,p.y);
	}
}
