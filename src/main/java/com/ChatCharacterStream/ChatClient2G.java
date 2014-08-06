package com.ChatCharacterStream;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by DELL on 14-4-8.
 */
public class ChatClient2G {
    public static void main(String[] args) throws UnknownHostException,
            IOException {
        ChatClient2 chatClient = new ChatClient2("172.168.1.101");
        chatClient.startWork();
        ChatClient2 chatClient2= new ChatClient2("172.168.1.101");
        chatClient.startWork();
        ChatClient2 chatClient3 = new ChatClient2("172.168.1.101");
        chatClient.startWork();
    }
}
