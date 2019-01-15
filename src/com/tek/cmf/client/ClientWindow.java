package com.tek.cmf.client;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import com.tek.cmf.logging.Logger;

public class ClientWindow {
	
	private static Graphics graphics;
	private static Canvas canvas;
	private static BufferedImage frame;
	private static JFrame windowFrame;
	
	public static void setImage(BufferedImage image) {
		if(image == null) {
			Logger.error("Null image");
			return;
		}
		
		attemptLoad(image);
		
		frame = image;
		
		if(canvas.getBufferStrategy() == null) canvas.createBufferStrategy(2);
		graphics = canvas.getBufferStrategy().getDrawGraphics();
		
		int[] dimensions = getDimensions(frame);
		
		graphics.drawImage(image, 0, 0, dimensions[0], dimensions[1], null);
		
		canvas.getBufferStrategy().show();
	}
	
	public static void attemptLoad(BufferedImage image) {
		if(windowFrame != null) return;
		windowFrame = new JFrame("Client View");
		canvas = new Canvas();
		int[] dimensions = getDimensions(image);
		canvas.setSize(dimensions[0], dimensions[1]);
		windowFrame.add(canvas);
		windowFrame.setResizable(false);
		windowFrame.pack();
		windowFrame.setVisible(true);
	}
	
	public static void uninitialize() {
		windowFrame.setVisible(false);
		windowFrame.dispose();
		windowFrame = null;
	}
	
	public static int[] getDimensions(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		
		double factor;
		
		if(width < 500) {
			factor = 500 / width;
			width = 500;
			height *= factor;
		}
		
		return new int[]{width, height};
	}
	
	public static BufferedImage getFrame() {
		return frame;
	}
	
	public static JFrame getWindowFrame() {
		return windowFrame;
	}
	
}
