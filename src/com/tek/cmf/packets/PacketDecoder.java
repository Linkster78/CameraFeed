package com.tek.cmf.packets;

import java.util.List;
import java.util.Optional;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class PacketDecoder extends ReplayingDecoder<Packet> {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		Optional<Packet> decoded = Packet.attemptDecode(in);
		if(decoded.isPresent()) {
			out.add(decoded.get());
		}
	}

}
