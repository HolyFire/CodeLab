package com.ChatCharacterStream;

/**
 * Created by DELL on 14-4-8.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
class Chat extends Thread {
    private Socket socket;
    private List<Socket> socketList;
    private int count;

    public Chat(int count, Socket socket, List<Socket> socketList) {
        this.count = count;
        this.socket = socket;
        this.socketList = socketList;
    }

    public void run() {
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));

            String message = null;
            while (true) {
                message = reader.readLine();
                // 接收到客户端的bye信息，客户端即将退出，并将bye写入到该客户端
                if (message.equals("bye")) {
                    writer = new PrintWriter(socket.getOutputStream());
                    writer.println("bye");
                    writer.flush();
                    continue;
                }

                // 向所有的客户端发送接收到信息
                for (int i = 0; i < socketList.size(); i++) {
                    writer = new PrintWriter(socketList.get(i)
                            .getOutputStream());
                    writer.println(count + " say: " + message);
                    writer.flush();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class ChatServer2 {


    public void startWork() throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);
        List<Socket> socketList = new ArrayList<Socket>();
        Socket socket = null;
        int count = 0;
        while (true) {
            socket = serverSocket.accept();
            count++;
            System.out.println(count + " clinet connected to the server!");
            // 将每一个连接到该服务器的客户端，加到List中
            socketList.add(socket);
            // 每一个连接到服务器的客户端，服务器开启一个新的线程来处理
            new Chat(count, socket, socketList).start();
        }
    }




    public static void main(String[] args) throws IOException {
        ChatServer2 chatServer = new ChatServer2();
        chatServer.startWork();
    }

}