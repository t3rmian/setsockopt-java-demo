package dev.termian.setsockopt.net.factory;

import com.sun.jna.Platform;
import dev.termian.setsockopt.net.config.Configuration;
import dev.termian.setsockopt.net.config.LinuxSocketConfigurer;
import dev.termian.setsockopt.net.impl.NativeEpollSocketChannelFactory;
import io.netty.channel.epoll.EpollSocketChannel;

import java.io.IOException;

public abstract class EpollSocketChannelFactory {

    public static EpollSocketChannelFactory getInstance(Configuration configuration) {
        if (Platform.getOSType() == Platform.LINUX) {
            return new NativeEpollSocketChannelFactory(new LinuxSocketConfigurer(configuration));
        }
        throw new UnsupportedOperationException("Not implemented");
    }

    public abstract void configure(EpollSocketChannel socketChannel) throws IOException;

}
