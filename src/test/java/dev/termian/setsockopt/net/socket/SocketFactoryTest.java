package dev.termian.setsockopt.net.socket;

import com.sun.jna.LastErrorException;
import dev.termian.setsockopt.net.config.Configuration;
import dev.termian.setsockopt.net.factory.SocketFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertThrows;

class SocketFactoryTest {

    public static final Configuration VALID_CONFIGURATION = (configurer, fileDescriptor) -> {
        configurer.setDontFragment(fileDescriptor, false);
        configurer.setTtl(fileDescriptor, 137);
    };

    @Test
    void configure() throws IOException {
        SocketFactory socketFactory = SocketFactory.getInstance(VALID_CONFIGURATION);
        try (Socket socket = new Socket("google.com", 80)) {
            socketFactory.configure(socket);
        }
    }

    @Test
    void configure_BeforeConnect_NullPointerException_NoFileDescriptor() throws IOException {
        SocketFactory socketFactory = SocketFactory.getInstance(VALID_CONFIGURATION);
        try (Socket socket = new Socket()) {
            assertThrows(NullPointerException.class, () -> socketFactory.configure(socket));
        }
    }

    @Test
    void configure_AfterConnect() throws IOException {
        SocketFactory socketFactory = SocketFactory.getInstance(VALID_CONFIGURATION);
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("google.com", 80));

            socketFactory.configure(socket);
        }
    }

    @Test
    void configure_AfterConnect_InvalidConfiguration() throws IOException {
        int invalidTtl = 256;
        SocketFactory socketFactory = SocketFactory.getInstance((configurer, fileDescriptor) ->
                configurer.setTtl(fileDescriptor, invalidTtl)
        );
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("google.com", 80));

            assertThrows(LastErrorException.class, () -> socketFactory.configure(socket));
        }
    }

}