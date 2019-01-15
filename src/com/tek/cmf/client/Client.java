package com.tek.cmf.client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import com.tek.cmf.logging.Logger;

public class Client {
	
	private Socket socket;
	
	public void start(String host, int port) throws UnknownHostException, IOException {
		socket = new Socket(host, port);
		socket.setTcpNoDelay(true);
		socket.setReceiveBufferSize(65536);
		
		String serverIp = socket.getRemoteSocketAddress().toString();
		Logger.info(String.format("Connected to %s", serverIp));
		
		InputStream input = (InputStream) socket.getInputStream();
		
		int length;
		
		while(true) {
			if(input.available() == 0) continue;
			
			byte[] lengthData = new byte[4];
			input.read(lengthData);
			length = ByteBuffer.wrap(lengthData).getInt();
			
			break;
		}
		
		ByteArrayOutputStream buffer = null;
		
		while(ClientWindow.getWindowFrame() == null || ClientWindow.getWindowFrame().isVisible()) {
			if(input.available() == 0) continue;
			if(buffer == null) buffer = new ByteArrayOutputStream();
			
			byte[] byteBuffer = new byte[Math.min(length - buffer.size(), input.available())];
			DataInputStream dis = new DataInputStream(input);
			dis.readFully(byteBuffer);
			buffer.write(byteBuffer);
			
			if(buffer.size() == length) {
				byte[] imageData = buffer.toByteArray().clone();
				BufferedImage image = createImageFromBytes(imageData);
				ClientWindow.setImage(image);
				System.out.println(arrayValue(imageData));
				buffer.close();
				buffer.flush();
				buffer = null;
			}
		}
		
		socket.close();
		ClientWindow.uninitialize();
	}
	
	private BufferedImage createImageFromBytes(byte[] imageData) {
	    ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
	    
	    try {
	        return ImageIO.read(bais);
	    } catch (IOException e) {
	        throw new RuntimeException(e);
	    }
	}
	
	private long arrayValue(byte[] bs) {
		long sum = 0;
		for(byte b : bs) {
			sum += b;
		}
		return sum;
	}
	
}
