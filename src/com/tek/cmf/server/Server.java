package com.tek.cmf.server;

import java.io.IOException;

import com.tek.cmf.packets.PacketDecoder;
import com.tek.cmf.packets.PacketEncoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
	
	public void start(int port) throws IOException, InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		ServerBootstrap sb = new ServerBootstrap();
		
		sb.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new PacketDecoder(),
							new PacketEncoder(),
							new ServerInboundHandler());
				}
			}).childOption(ChannelOption.SO_KEEPALIVE, true)
			.option(ChannelOption.SO_BACKLOG, 128);
		
		try {
			ChannelFuture future = sb.bind(port).sync();
			
			new Thread(() -> {
				try {
					future.channel().closeFuture().sync();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				workerGroup.shutdownGracefully();
			}).start();
		} catch(Exception e) {
			workerGroup.shutdownGracefully();
			throw new IOException(e.getMessage());
		}
	}
	
}
