package dev.termian.setsockopt.net.config;

import com.sun.jna.LastErrorException;
import dev.termian.setsockopt.net.impl.FileDescriptorWrapper;

import java.io.IOException;

public abstract class SocketConfigurer implements Configurer {

    private final Configuration configuration;

    public SocketConfigurer(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setOptions(FileDescriptorWrapper fileDescriptor) throws LastErrorException, IOException {
        configuration.apply(this, fileDescriptor);
    }

}
