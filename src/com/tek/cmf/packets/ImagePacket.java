package com.tek.cmf.packets;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import io.netty.buffer.ByteBuf;

public class ImagePacket extends Packet {

	private byte[] imageData;
	
	public ImagePacket() {
		super(PACKET.IMAGE.getOp());
	}
	
	public ImagePacket(byte[] imageData) {
		super(PACKET.IMAGE.getOp());
		this.imageData = imageData;
	}
	
	public ImagePacket(BufferedImage image) throws IOException {
		super(PACKET.IMAGE.getOp());
		this.imageData = encodeImage(image);
	}
	
	@Override
	public void decode(ByteBuf in) throws IOException {
		int length = in.readInt();
		ByteBuf imageDataBuffer = in.readBytes(length);
		this.imageData = new byte[imageDataBuffer.readableBytes()];
		imageDataBuffer.readBytes(this.imageData);
	}
	
	@Override
	public void encode(ByteBuf out) {
		out.writeInt(imageData.length);
		out.writeBytes(imageData);
	}
	
	public byte[] getImageData() {
		return imageData;
	}
	
	public BufferedImage getImage() throws IOException {
		return decodeImage(imageData);
	}
	
	private byte[] encodeImage(BufferedImage image) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(image, "png", bos);
		return bos.toByteArray();
	}
	
	private BufferedImage decodeImage(byte[] imageData) throws IOException {
	    ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
	    return ImageIO.read(bais);
	}

}
