package com.tek.cmf.capture;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import com.github.sarxos.webcam.Webcam;
import com.tek.cmf.exceptions.NoWebcamException;
import com.tek.cmf.exceptions.UninitializedException;

public class Camera {
	
	private final static int FRAMERATE = 24;
	private final static int OPTIMAL_MAX_WIDTH = 500;
	
	private static Webcam webcam;
	private static BufferedImage lastCapture;
	private static long lastCaptureTime;
	
	public static void initialize() throws NoWebcamException {
		webcam = Webcam.getDefault();
		webcam.setViewSize(getOptimalViewSize(webcam.getViewSizes()));
		if(webcam == null) throw new NoWebcamException();
		webcam.open();
	}
	
	private static Dimension getOptimalViewSize(Dimension[] dimensions) {
		Dimension goodDimension = null;
		
		for(Dimension dimension : dimensions) {
			if(dimension.getWidth() > OPTIMAL_MAX_WIDTH) break;
			goodDimension = dimension;
		}
		
		return goodDimension;
	}
	
	public static BufferedImage captureImage() throws UninitializedException {
		if(webcam == null) throw new UninitializedException("Webcam");
		if(System.currentTimeMillis() - lastCaptureTime <= 1000 / FRAMERATE) return lastCapture;
		lastCapture = webcam.getImage();
		lastCaptureTime = System.currentTimeMillis();
		return lastCapture;
	}
	
	public static String getDevice() throws UninitializedException {
		if(webcam == null) throw new UninitializedException("Webcam");
		return webcam.getDevice().getName();
	}
	
	public static void uninitialize() throws UninitializedException {
		if(webcam == null) throw new UninitializedException("Webcam");
		webcam.close();
	}
	
	public static Dimension getDimensions() throws UninitializedException {
		if(webcam == null) throw new UninitializedException("Webcam");
		return webcam.getViewSize();
	}
	
	public static int getFramerate() {
		return FRAMERATE;
	}
	
}
