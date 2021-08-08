package dev.termian.setsockopt.net.factory;

import dev.termian.setsockopt.net.config.Configuration;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Run with --illegal-access=permit --add-modules jdk.incubator.foreign -Dforeign.restricted=warn
 */
class ForeignSocketChannelFactoryTest {

    public static final Configuration VALID_CONFIGURATION = (configurer, fileDescriptor) -> {
        configurer.setDontFragment(fileDescriptor, false);
        configurer.setTtl(fileDescriptor, 137);
    };

    @Test
    void open() throws IOException {
        SocketChannelFactory socketFactory = ForeignSocketChannelFactory.getInstance(VALID_CONFIGURATION);
        //noinspection EmptyTryBlock
        try (SocketChannel ignored = socketFactory.open()) {
        }
    }

    @Test
    void open_InvalidConfiguration() {
        int invalidTtl = 256;
        SocketChannelFactory socketFactory = ForeignSocketChannelFactory.getInstance((configurer, fileDescriptor) ->
                configurer.setTtl(fileDescriptor, invalidTtl)
        );
        assertThrows(RuntimeException.class, socketFactory::open);
    }

}