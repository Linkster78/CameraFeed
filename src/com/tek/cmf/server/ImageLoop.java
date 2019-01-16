package com.tek.cmf.server;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.tek.cmf.capture.Camera;
import com.tek.cmf.exceptions.UninitializedException;
import com.tek.cmf.logging.Logger;
import com.tek.cmf.packets.ImagePacket;

public class ImageLoop implements Runnable {
	
	@Override
	public void run() {
		long framerateDelta = 1000 / Camera.getFramerate();
		
		while(true) {
			try {
				long preCapture = System.currentTimeMillis();
				
				BufferedImage capture = Camera.captureImage();
				ImagePacket imagePacket = new ImagePacket(capture);
				ServerInboundHandler.connectedClients.writeAndFlush(imagePacket);
				
				long postCapture = System.currentTimeMillis();
				long msCapture = postCapture - preCapture;
				long waitTime = framerateDelta - msCapture;
				
				if(waitTime >= 0) {
					try {
						Thread.sleep(waitTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else if(waitTime >= 100) {
					Logger.info(String.format("ImageLoop is behind %dms", Math.abs(waitTime)));
				}
			} catch (UninitializedException | IOException e1) {
				Logger.error("Error in the ImageLoop");
			}
		}
	}
	
}
