package dev.termian.setsockopt.client.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.ByteBuffer;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ByteBuf out = Unpooled.buffer(5002);
        out.writeByte(1);
        out.writeBytes(new byte[5000]);
        out.writeByte(2);
        ctx.writeAndFlush(out)
                .addListener(ignored -> System.out.println("Send buffer flushed. Closing the channel. Check the Server/Wireshark logs."))
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ctx.close();
    }

}
