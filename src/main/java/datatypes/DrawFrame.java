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
		findPixelArray();
	}
	
	public void refreshPixelArray() {
		findPixelArray();
	}
	
	private void findPixelArray() {
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
	}
	
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
	
	public int getRGBA(int x, int y) {
		return pixelArray[y][x];
	}
	
	public int getRGBA(Point p) {
		return getRGB(p.x, p.y);
		//return getRGBA(p.x,p.y);
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
		pixelArray[y][x] = color.getRGB();
		this.setRGB(x, y, color.getRGB());
	}
	
	public void setColorAtPixelArrayPoint(Color color, Point p) {
		setColorAtPixelArrayPoint(color, p.x,p.y);
	}
	
	/*
	public DrawInstruction deepCopy() {
		DrawInstruction newDps = new DrawInstruction();
		for (int i = 0; i < this.size(); i++) {
			ArrayList<DrawPoint>dpss = this.get(i);
			ArrayList<DrawPoint> newDpss = new ArrayList<DrawPoint>();
			for (DrawPoint dp : dpss) {
				DrawPoint newDp = dp.deepCopy();
				newDpss.add(newDp);
			}
			newDps.add(newDpss);
		}
		return newDps;
	}
	*/
}
