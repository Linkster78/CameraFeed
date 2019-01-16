package com.tek.cmf.server;

import java.io.IOException;

import com.tek.cmf.capture.Camera;
import com.tek.cmf.exceptions.NoWebcamException;
import com.tek.cmf.exceptions.UninitializedException;
import com.tek.cmf.logging.Logger;

public class Launcher {
	
	public static void main(String[] args) throws IOException {
		int port;
		
		if(args.length != 1) {
			throw new IOException("Invalid arguments provided, expected <PORT>");
		} else {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				throw new IOException("Invalid arguments provided, expected <PORT>");
			}
		}
		
		try {
			Camera.initialize();
			Logger.info(String.format("Opened webcam %s", Camera.getDevice()));
			
			Server server = new Server();
			server.start(port);
			Logger.info(String.format("Bound server to port %d", port));
			
			new Thread(new ImageLoop()).start();
			Logger.info("Started the ImageLoop");
		} catch (IOException | InterruptedException e) {
			Logger.error("Error while starting server");
		} catch (NoWebcamException e) {
			Logger.error("No webcam found");
		} catch (UninitializedException e) {
			Logger.error("Webcam didn't initialize correctly");
		}
	}
	
}
