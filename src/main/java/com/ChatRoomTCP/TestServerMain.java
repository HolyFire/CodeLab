package com.ChatRoomTCP;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by DELL on 14-4-2.
 */
public class TestServerMain {
    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(10000);
        Socket s = socket.accept();
        chatRoomServer server = new chatRoomServer();
        server.getSvOut(s).start();
        server.getSvReceive(s).start();

        ServerSocket socket2 = new ServerSocket(10001);
        Socket s2 = socket2.accept();
        server.getSvOut(s2).start();
        server.getSvReceive(s2).start();

    }
}
