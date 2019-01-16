package com.tek.cmf.client;

import java.awt.image.BufferedImage;

import com.tek.cmf.logging.Logger;
import com.tek.cmf.packets.HeaderPacket;
import com.tek.cmf.packets.ImagePacket;
import com.tek.cmf.packets.Packet;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class ClientInboundHandler extends ChannelInboundHandlerAdapter {
	
	private boolean displayLaunched;
	
	public ClientInboundHandler() {
		displayLaunched = false;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Packet packet = (Packet) msg;
		
		if(packet instanceof ImagePacket) {
			ImagePacket imagePacket = (ImagePacket) packet;
			
			if(displayLaunched) {
				BufferedImage image = imagePacket.getImage();
				Image fxImage = SwingFXUtils.toFXImage(image, null);
				ClientDisplay.frame = fxImage;
			}
		}
		
		if(packet instanceof HeaderPacket) {
			HeaderPacket headerPacket = (HeaderPacket) packet;
			
			if(!displayLaunched) {
				ClientDisplay.width = headerPacket.getWidth();
				ClientDisplay.height = headerPacket.getHeight();
				new Thread(() -> {
					ClientDisplay.run(new String[] {});
				}).start();
				Logger.info("Started ClientDisplay");
				displayLaunched = true;
			}
		}
	}
	
}
