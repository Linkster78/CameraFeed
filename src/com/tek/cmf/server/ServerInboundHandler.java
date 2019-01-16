package com.tek.cmf.server;

import java.awt.Dimension;

import com.tek.cmf.capture.Camera;
import com.tek.cmf.packets.HeaderPacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ServerInboundHandler extends ChannelInboundHandlerAdapter {
	
	public static ChannelGroup connectedClients;
	
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		connectedClients.add(ctx.channel());
		
		Dimension dimensions = Camera.getDimensions();
		HeaderPacket headerPacket = new HeaderPacket(dimensions.width, dimensions.height);
		ctx.channel().writeAndFlush(headerPacket);
	}
	
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		connectedClients.remove(ctx.channel());
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		connectedClients.remove(ctx.channel());
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception { }
	
	static {
		connectedClients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	}
	
}
