package com.ChatCharacterStream;

import java.io.IOException;

/**
 * Created by DELL on 14-4-9.
 */
public class ServerMain {
    public static void main(String[] args) throws IOException {
        ChatServer server=new ChatServer("socket 1",22345);
        server.initServer();
        ActionServer a1=new ActionServer(server);
        new Thread(a1).start();
    }
}
