package com.ChatRoomTCP;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by DELL on 14-4-4.
 */
public class TestClient2Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        Socket socket = new Socket("192.168.1.105", 10001);
        chatRoomClient client = new chatRoomClient();
        client.getReceive(socket).start();
        Thread t1=client.getOutput(socket);
        t1.start();
        t1.join();
        socket.close();

    }
}
