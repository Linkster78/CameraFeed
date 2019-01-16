package com.tek.cmf.client;

import java.io.IOException;

import com.tek.cmf.logging.Logger;

public class Launcher {
	
	public static void main(String[] args) throws IOException {
		String host;
		int port;
		
		if(args.length != 2) {
			throw new IOException("Invalid arguments provided, expected <HOST> <PORT>");
		} else {
			try {
				host = args[0];
				port = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				throw new IOException("Invalid arguments provided, expected <HOST> <PORT>");
			}
		}
		
		try {
			Client client = new Client();
			client.start(host, port);
			Logger.info(String.format("Connected to host %s on port %d", host, port));
		} catch (IOException | InterruptedException e) {
			Logger.error("Couldn't connect to server");
		}
	}
	
}
