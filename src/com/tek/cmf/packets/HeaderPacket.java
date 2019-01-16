package com.tek.cmf.packets;

import java.io.IOException;

import io.netty.buffer.ByteBuf;

public class HeaderPacket extends Packet {

	private int width, height;
	
	public HeaderPacket() {
		super(PACKET.HEADER.getOp());
	}
	
	public HeaderPacket(int width, int height) {
		super(PACKET.HEADER.getOp());
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void decode(ByteBuf in) throws IOException {
		this.width = in.readInt();
		this.height = in.readInt();
	}
	
	@Override
	public void encode(ByteBuf out) {
		out.writeInt(this.width);
		out.writeInt(this.height);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

}
