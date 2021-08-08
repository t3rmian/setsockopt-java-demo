package dev.termian.setsockopt.client.netty;

import dev.termian.setsockopt.net.factory.SocketChannelFactory;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;

public class NioChannelWrapper extends NioSocketChannel {

    private static final SocketChannelFactory CHANNEL_FACTORY = SocketChannelFactory
            .getInstance((configurer, fileDescriptor) -> {
                configurer.setDontFragment(fileDescriptor, false);
                configurer.setTtl(fileDescriptor, 2);
            });

    public NioChannelWrapper() throws IOException {
        super(CHANNEL_FACTORY.open());
    }

}
