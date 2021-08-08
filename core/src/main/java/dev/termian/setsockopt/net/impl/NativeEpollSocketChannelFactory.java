package dev.termian.setsockopt.net.impl;

import dev.termian.setsockopt.net.config.SocketConfigurer;
import dev.termian.setsockopt.net.factory.EpollSocketChannelFactory;
import io.netty.channel.epoll.EpollSocketChannel;

import java.io.IOException;

public class NativeEpollSocketChannelFactory extends EpollSocketChannelFactory {

    private final SocketConfigurer socketConfigurer;

    public NativeEpollSocketChannelFactory(SocketConfigurer socketConfigurer) {
        this.socketConfigurer = socketConfigurer;
    }

    @Override
    public void configure(EpollSocketChannel socketChannel) throws IOException {
        FileDescriptorWrapper fileDescriptor = getFileDescriptor(socketChannel);
        socketConfigurer.setOptions(fileDescriptor);
    }

    private FileDescriptorWrapper getFileDescriptor(EpollSocketChannel channel) {
        return new FileDescriptorWrapper(channel.fd().intValue());
    }

}
