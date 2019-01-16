package com.tek.cmf.packets;

import java.io.IOException;
import java.util.Optional;

import io.netty.buffer.ByteBuf;

public class Packet {
	
	private byte op;
	
	public Packet(byte op) {
		this.op = op;
	}
	
	public void decode(ByteBuf in) throws IOException { }
	
	public void encode(ByteBuf out) { }
	
	public void setOp(byte op) {
		this.op = op;
	}
	
	public byte getOp() {
		return op;
	}
	
	public enum PACKET {
		IMAGE((byte)0x01, ImagePacket.class),
		HEADER((byte)0x02, HeaderPacket.class);
		
		private byte op;
		private Class<? extends Packet> packetClass;
		
		PACKET(byte op, Class<? extends Packet> packetClass) {
			this.op = op;
			this.packetClass = packetClass;
		}
		
		public byte getOp() {
			return op;
		}
		
		public Class<? extends Packet> getPacketClass() {
			return packetClass;
		}	
	}
	
	public static Optional<Packet> attemptDecode(ByteBuf buffer) throws IOException {
		byte op = buffer.readByte();
		
		for(PACKET packetEnum : PACKET.values()) {
			if(packetEnum.getOp() == op) {
				Class<?> packetClass = packetEnum.getPacketClass();
				
				try {
					Packet packet = (Packet) packetClass.newInstance();
					packet.decode(buffer);
					return Optional.of(packet);
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		
		return Optional.empty();
	}
	
}
