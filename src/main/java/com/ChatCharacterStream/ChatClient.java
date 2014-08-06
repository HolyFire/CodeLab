package com.ChatCharacterStream;

import java.io.*;
import java.net.Socket;

/**
 * Created by DELL on 14-4-4.
 */
public class ChatClient {
    public static void main(String[] args) throws IOException {
        Socket s=new Socket("192.168.1.103",22345);
        new Thread(new output(s)).start();
        new Thread(new input(s)).start();

    }

}

class output implements Runnable{
    Socket socket=null;

    public output(Socket socket){
        this.socket=socket;
    }
    String chatContent=null;
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public void run() {
        try {
            while ((chatContent = input.readLine()) != null) {
                OutputStream out = socket.getOutputStream();
                byte[] buf = chatContent.getBytes();
                out.write(buf);
    //                    System.out.println("2~~~~~~~~~~output");

                if (chatContent.equals("886")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

class input implements Runnable{
    Socket socket=null;
    public input(Socket socket){
        this.socket=socket;
    }

    String line=null;
    @Override
    public void run() {
        while (true){
            try {
                InputStream  in = socket.getInputStream();
                byte [] buf=new byte[1024];
                String line=new String(buf,0,buf.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}