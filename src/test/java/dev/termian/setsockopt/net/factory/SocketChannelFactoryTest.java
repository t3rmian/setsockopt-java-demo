package dev.termian.setsockopt.net.factory;

import com.sun.jna.LastErrorException;
import dev.termian.setsockopt.net.config.Configuration;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SocketChannel;

import static org.junit.jupiter.api.Assertions.assertThrows;

class SocketChannelFactoryTest {

    public static final Configuration VALID_CONFIGURATION = (configurer, fileDescriptor) -> {
        configurer.setDontFragment(fileDescriptor, false);
        configurer.setTtl(fileDescriptor, 137);
    };

    @Test
    void open() throws IOException {
        SocketChannelFactory socketFactory = SocketChannelFactory.getInstance(VALID_CONFIGURATION);
        //noinspection EmptyTryBlock
        try (SocketChannel ignored = socketFactory.open()) {
        }
    }

    @Test
    void open_InvalidConfiguration() {
        int invalidTtl = 256;
        SocketChannelFactory socketFactory = SocketChannelFactory.getInstance((configurer, fileDescriptor) ->
                configurer.setTtl(fileDescriptor, invalidTtl)
        );
        assertThrows(LastErrorException.class, socketFactory::open);
    }

}