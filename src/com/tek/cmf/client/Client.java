package com.tek.cmf.client;

import java.io.IOException;

import com.tek.cmf.packets.PacketDecoder;
import com.tek.cmf.packets.PacketEncoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
	
	public void start(String host, int port) throws IOException, InterruptedException {
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		Bootstrap b = new Bootstrap();
		b.group(workerGroup)
			.channel(NioSocketChannel.class)
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new PacketDecoder(),
							new PacketEncoder(),
							new ClientInboundHandler());
				}
			}).option(ChannelOption.SO_KEEPALIVE, true);
		
		try {
			ChannelFuture future = b.connect(host, port).sync();
			
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
