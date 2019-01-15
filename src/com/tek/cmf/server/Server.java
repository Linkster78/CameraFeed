package com.tek.cmf.server;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import com.tek.cmf.capture.Camera;
import com.tek.cmf.exceptions.UninitializedException;
import com.tek.cmf.logging.Logger;

public class Server {
	
	private ServerSocket serverSocket;
	private ExecutorService clientHandler;
	
	public void start(int port) throws IOException {
		clientHandler = Executors.newFixedThreadPool(4);
		
		serverSocket = new ServerSocket(port);
		
		Logger.info(String.format("Bound server to port %d", port));
		
		while(true) {
			Socket clientSocket = serverSocket.accept();
			String ip = (((InetSocketAddress) clientSocket.getRemoteSocketAddress()).getAddress()).toString().replace("/","");
			
			clientHandler.execute(() -> {
				try {
					Logger.info(String.format("Connection from %s opened", ip));
					
					OutputStream outputStream = clientSocket.getOutputStream();
					
					BufferedImage testFrame = Camera.captureImage();
					outputStream.write(ByteBuffer.allocate(4).putInt(getImageData(testFrame).length).array());
					
					long deltaFramerate = 1000 / Camera.getFramerate();
					
					while(clientSocket.isConnected()) {
						long preImage = System.currentTimeMillis();
						
						BufferedImage frame = Camera.captureImage();
						byte[] data = getImageData(frame);
						
						outputStream.write(data);
						System.out.println(arrayValue(data));
						
						long postImage = System.currentTimeMillis();
						long waitTime = deltaFramerate - (postImage - preImage);
						
						if(waitTime < 0) {
							Logger.warning(String.format("(%s) Thread is behind %dms", ip, Math.abs(waitTime)));
						} else {
							Thread.sleep(waitTime);
						}
					}
				} catch (IOException e) {
					Logger.info(String.format("Connection from %s closed", ip));
				} catch (InterruptedException | UninitializedException e) {
					e.printStackTrace();
				}
			});
		}
	}
	
	public byte[] getImageData(BufferedImage image) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(image, "png", bos);
		return bos.toByteArray();
	}
	
	public ServerSocket getServerSocket() {
		return serverSocket;
	}
	
	private long arrayValue(byte[] bs) {
		long sum = 0;
		for(byte b : bs) {
			sum += b;
		}
		return sum;
	}
	
}
