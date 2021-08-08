package dev.termian.setsockopt.client;

import dev.termian.setsockopt.net.factory.SocketChannelFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.conn.ManagedNHttpClientConnectionFactory;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.Socket;

public class HttpAsyncClient {
    public static void main(String[] args) throws IOException {
        SocketChannelFactory socketChannelFactory = SocketChannelFactory.getInstance((configurer, fileDescriptor) -> {
            configurer.setDontFragment(fileDescriptor, false);
            configurer.setTtl(fileDescriptor, 137);
        });

        CloseableHttpAsyncClient httpClient = HttpAsyncClientBuilder.create()
                .setConnectionManager(new PoolingNHttpClientConnectionManager(
                        new DefaultConnectingIOReactor(IOReactorConfig.DEFAULT, null) {
                            @Override
                            protected void prepareSocket(Socket socket) throws IOException {
                                socketChannelFactory.configure(socket.getChannel());
                                super.prepareSocket(socket);
                            }
                        },
                        new ManagedNHttpClientConnectionFactory()
                ))
                .build();

        httpClient.start();

        httpClient.execute(new HttpGet("http://google.com"), new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse httpResponse) {
                System.out.println(httpResponse.getProtocolVersion());
                System.out.println(httpResponse.getStatusLine().getStatusCode());
                System.out.println(httpResponse.getStatusLine().getReasonPhrase());
                System.out.println(httpResponse.getStatusLine().toString());

                HttpEntity entity = httpResponse.getEntity();
                if (entity != null) {
                    System.out.println(readSilently(entity));
                }
                closeSilently(httpClient);
            }

            @Override
            public void failed(Exception e) {
                e.printStackTrace();
                closeSilently(httpClient);
            }

            @Override
            public void cancelled() {
                System.err.println("cancelled");
                closeSilently(httpClient);
            }
        });
    }

    private static String readSilently(HttpEntity entity) {
        try {
            return EntityUtils.toString(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void closeSilently(CloseableHttpAsyncClient httpClient) {
        try {
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
