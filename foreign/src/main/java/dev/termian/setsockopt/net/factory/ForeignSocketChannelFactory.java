package dev.termian.setsockopt.net.factory;

import com.sun.jna.Platform;
import dev.termian.setsockopt.net.config.Configuration;
import dev.termian.setsockopt.net.config.LinuxForeignSocketConfigurer;
import dev.termian.setsockopt.net.config.WindowsForeignSocketConfigurer;
import dev.termian.setsockopt.net.impl.NativeSocketChannelFactory;

public abstract class ForeignSocketChannelFactory {

    public static SocketChannelFactory getInstance(Configuration configuration) {
        return switch (Platform.getOSType()) {
            case Platform.LINUX -> new NativeSocketChannelFactory(new LinuxForeignSocketConfigurer(configuration));
            case Platform.WINDOWS -> new NativeSocketChannelFactory(new WindowsForeignSocketConfigurer(configuration));
            default -> throw new UnsupportedOperationException("Not implemented");
        };
    }

}
