package com.NewChatRoom;

import java.io.*;
import java.net.Socket;

/**
 * Created by DELL on 14-4-14.
 */

//创建服务器的接口 #
//接收服务器消息线程
//将消息发送给服务器线程

class ReceiveFromServ implements Runnable{
    Socket socket=null;
    public  ReceiveFromServ(Socket socket){
        this.socket=socket;
    }

    @Override
    public void run() {
        System.out.println("I'm here");

        try{
            InputStream in=socket.getInputStream();

            while (true){
//                BufferedReader buf=new BufferedReader(new InputStreamReader(in));
                byte[] buf=new byte[1024];
                int len=in.read(buf);

                String line=new String(buf,0,len);
                System.out.println(line);
                System.out.println("~~~~~~~");

            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

class SendToServer implements Runnable{
    Socket socket=null;

    public  SendToServer(Socket socket){
        this.socket=socket;
    }


    @Override
    public void run() {
        BufferedReader buf=new BufferedReader(new InputStreamReader(System.in));
        String line=null;

        try {
            OutputStream out=socket.getOutputStream();
            while((line=buf.readLine())!=null){
                out.write(line.getBytes());
                if(line.equals("886")){
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class NewClient {
    public static void main(String[] args) throws IOException {
        String ip="192.168.1.105";
        int port=10005;

        for(int i=0;i<10000;i++){
        Socket socket=new Socket(ip,port);
        new Thread(new ReceiveFromServ(socket)).start();
        new Thread(new SendToServer(socket)).start();
        }

    }

}
