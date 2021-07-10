package dev.termian.setsockopt.client;

import dev.termian.setsockopt.net.factory.SocketChannelFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.Socket;

import static org.apache.http.conn.ssl.SSLConnectionSocketFactory.getDefaultHostnameVerifier;

public class HttpClient {
    public static void main(String[] args) throws IOException {
        SocketChannelFactory socketChannelFactory = SocketChannelFactory.getInstance((configurer, fileDescriptor) -> {
            configurer.setDontFragment(fileDescriptor, false);
            configurer.setTtl(fileDescriptor, 137);
        });
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(new BasicHttpClientConnectionManager(RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", new PlainConnectionSocketFactory() {
                            @Override
                            public Socket createSocket(HttpContext context) throws IOException {
                                return socketChannelFactory.open().socket();
                            }
                        })
                        .register("https", new SSLConnectionSocketFactory(SSLContexts.createDefault(), getDefaultHostnameVerifier()) {
                            @Override
                            public Socket createSocket(HttpContext context) throws IOException {
                                return socketChannelFactory.open().socket();
                            }
                        })
                        .build(), new ManagedHttpClientConnectionFactory()))
                .build();
        try (CloseableHttpResponse response = httpClient.execute(new HttpGet("https://google.com"))) {
            System.out.println(response.getProtocolVersion());
            System.out.println(response.getStatusLine().getStatusCode());
            System.out.println(response.getStatusLine().getReasonPhrase());
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                System.out.println(result);
            }
        }
    }
}
