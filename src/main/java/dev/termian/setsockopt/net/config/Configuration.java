package dev.termian.setsockopt.net.config;

import com.sun.jna.LastErrorException;
import dev.termian.setsockopt.net.impl.FileDescriptorWrapper;

import java.io.IOException;

public interface Configuration {

    void apply(Configurer configurer, FileDescriptorWrapper fileDescriptor) throws LastErrorException, IOException;

}
