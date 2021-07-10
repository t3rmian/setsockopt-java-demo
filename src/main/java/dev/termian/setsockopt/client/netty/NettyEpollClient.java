package dev.termian.setsockopt.client.netty;

import dev.termian.setsockopt.Server;
import dev.termian.setsockopt.net.factory.EpollSocketChannelFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;

import java.net.InetAddress;

public class NettyEpollClient {
    public static void main(String[] args) throws Exception {
        Epoll.ensureAvailability();
        EpollSocketChannelFactory socketChannelFactory = EpollSocketChannelFactory
                .getInstance((configurer, fileDescriptor) -> {
                    configurer.setDontFragment(fileDescriptor, false);
                    configurer.setTtl(fileDescriptor, 2);
                });

        EventLoopGroup workerGroup = new EpollEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(workerGroup)
                    .channel(EpollSocketChannel.class)
                    .handler(new ChannelInitializer<EpollSocketChannel>() {
                        @Override
                        public void initChannel(EpollSocketChannel ch)
                                throws Exception {
                            socketChannelFactory.configure(ch);
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