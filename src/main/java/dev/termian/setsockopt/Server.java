package dev.termian.setsockopt;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final int PORT = 14321;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Starting socket server on port " + PORT);
        //noinspection InfiniteLoopStatement
        while (true) {
            Socket accept = serverSocket.accept();
            InputStream inputStream = accept.getInputStream();
            int read = inputStream.read();
            while (read >= 0) {
                System.out.print(read);
                read = inputStream.read();
            }
            System.out.println();
        }
    }
}
