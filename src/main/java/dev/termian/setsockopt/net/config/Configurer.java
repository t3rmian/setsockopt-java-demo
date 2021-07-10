package dev.termian.setsockopt.net.config;

import com.sun.jna.LastErrorException;
import dev.termian.setsockopt.net.impl.FileDescriptorWrapper;

import java.io.IOException;

public interface Configurer {

    void setDontFragment(FileDescriptorWrapper fileDescriptor, boolean dontFragment) throws LastErrorException, IOException;

    void setTtl(FileDescriptorWrapper fileDescriptor, int ttl) throws LastErrorException, IOException;

}
