package dev.termian.setsockopt.net.impl;

import dev.termian.setsockopt.net.config.SocketConfigurer;
import dev.termian.setsockopt.net.factory.SocketFactory;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Socket;
import java.net.SocketImpl;

public class NativeSocketFactory extends SocketFactory {

    private static final Field SOCKET_IMPL;
    private static final Field SOCKET_FD;

    static {
        try {
            Class<?> socket = Class.forName("java.net.Socket");
            SOCKET_IMPL = socket.getDeclaredField("impl");
            SOCKET_IMPL.setAccessible(true);
            Class<?> socketImpl = Class.forName("java.net.SocketImpl");
            SOCKET_FD = socketImpl.getDeclaredField("fd");
            SOCKET_FD.setAccessible(true);
        } catch (NoSuchFieldException | ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private final SocketConfigurer socketConfigurer;

    public NativeSocketFactory(SocketConfigurer socketConfigurer) {
        this.socketConfigurer = socketConfigurer;
    }

    @Override
    public void configure(Socket socket) throws IOException {
        FileDescriptorWrapper fileDescriptor = getFileDescriptor(socket);
        socketConfigurer.setOptions(fileDescriptor);
    }

    private FileDescriptorWrapper getFileDescriptor(Socket socket) throws IOException {
        try {
            SocketImpl socketImpl = (SocketImpl) SOCKET_IMPL.get(socket);
            FileDescriptor fileDescriptor = (FileDescriptor) SOCKET_FD.get(socketImpl);
            return new FileDescriptorWrapper(fileDescriptor);
        } catch (IllegalAccessException iae) {
            throw new IOException(iae);
        }
    }

}
