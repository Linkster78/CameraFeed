package com.tek.cmf.capture;

import java.awt.image.BufferedImage;

import com.github.sarxos.webcam.Webcam;
import com.tek.cmf.exceptions.NoWebcamException;
import com.tek.cmf.exceptions.UninitializedException;

public class Camera {
	
	private final static int FRAMERATE = 30;
	
	private static Webcam webcam;
	private static BufferedImage lastCapture;
	private static long lastCaptureTime;
	
	public static void initialize() throws NoWebcamException {
		webcam = Webcam.getDefault();
		if(webcam == null) throw new NoWebcamException();
		webcam.open();
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
	
	public static int getFramerate() {
		return FRAMERATE;
	}
	
}
