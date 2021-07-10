package dev.termian.setsockopt.client.netty;

import dev.termian.setsockopt.Server;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;

import java.net.InetAddress;

public class NettyClient {
    public static void main(String[] args) throws Exception {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(workerGroup)
                    .channel(NioChannelWrapper.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<NioChannelWrapper>() {
                        @Override
                        public void initChannel(NioChannelWrapper ch) {
                            ch.pipeline().addLast(new ClientHandler());
                        }
                    });

            ChannelFuture f = bootstrap.connect(InetAddress.getLocalHost(), Server.PORT).sync();

            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}