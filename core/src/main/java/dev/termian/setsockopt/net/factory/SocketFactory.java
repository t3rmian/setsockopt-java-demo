package dev.termian.setsockopt.net.factory;

import com.sun.jna.Platform;
import dev.termian.setsockopt.net.config.Configuration;
import dev.termian.setsockopt.net.config.LinuxSocketConfigurer;
import dev.termian.setsockopt.net.config.WindowsSocketConfigurer;
import dev.termian.setsockopt.net.impl.NativeSocketFactory;

import java.io.IOException;
import java.net.Socket;

public abstract class SocketFactory {

    public static SocketFactory getInstance(Configuration configuration) {
        switch (Platform.getOSType()) {
            case Platform.LINUX:
                return new NativeSocketFactory(new LinuxSocketConfigurer(configuration));
            case Platform.WINDOWS:
                return new NativeSocketFactory(new WindowsSocketConfigurer(configuration));
            default:
                throw new UnsupportedOperationException("Not implemented");
        }
    }

    public abstract void configure(Socket socket) throws IOException;

}
