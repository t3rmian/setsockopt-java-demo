package dev.termian.setsockopt.net.config;

import com.sun.jna.ptr.IntByReference;
import dev.termian.setsockopt.net.impl.FileDescriptorWrapper;

public class WindowsSocketConfigurer extends SocketConfigurer {

    public WindowsSocketConfigurer(Configuration configuration) {
        super(configuration);
    }

    @Override
    public void setDontFragment(FileDescriptorWrapper fileDescriptor, boolean dontFragment) {
        WinSocket.INSTANCE.setsockopt(
                fileDescriptor.getFd(),
                WinSocket.IPPROTO_IP,
                WinSocket.IP_DONTFRAGMENT,
                new IntByReference(dontFragment ? 1 : 0).getPointer(),
                4
        );
    }

    @Override
    public void setTtl(FileDescriptorWrapper fileDescriptor, int ttl) {
        WinSocket.INSTANCE.setsockopt(
                fileDescriptor.getFd(),
                WinSocket.IPPROTO_IP,
                WinSocket.IP_TTL,
                new IntByReference(ttl).getPointer(),
                4
        );
    }

}
