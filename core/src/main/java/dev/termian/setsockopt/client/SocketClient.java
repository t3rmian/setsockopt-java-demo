package dev.termian.setsockopt.client;

import dev.termian.setsockopt.Server;
import dev.termian.setsockopt.net.factory.SocketChannelFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;

public class SocketClient {
    public static void main(String[] args) throws IOException {
        SocketChannelFactory socketChannelFactory = SocketChannelFactory
                .getInstance((configurer, fileDescriptor) -> {
                    configurer.setDontFragment(fileDescriptor, false);
                    configurer.setTtl(fileDescriptor, 2);
                });

        try (SocketChannel channel = socketChannelFactory.open()) {
            Socket socket = channel.socket();
            channel.connect(new InetSocketAddress(InetAddress.getLoopbackAddress(), Server.PORT));

            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(1);
            outputStream.write(new byte[5000]);
            outputStream.write(2);
        }
    }
}
