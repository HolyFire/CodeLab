package com.NewChatRoom;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by DELL on 14-4-14.
 */


//开启服务器 #
//收集客户端端口信息 放入list中 #
//接收客户端信息#
//将客户端信息群发到list中的客户端中#
//群发群发信息功能#


public class NewServer {

}


class Send implements Runnable{
    ArrayList<Socket> list=null;
//    Socket socket=null;

    public Send (ArrayList list){
        this.list=list;
    }

    @Override
    public void run() {

        try {
            String line=null;
            BufferedReader buf=new BufferedReader(new InputStreamReader(System.in));
            while ((line=buf.readLine())!=null){
                for(int i=0;i<list.size();i++){
                    OutputStream out = list.get(i).getOutputStream();
                    out.write(("Server: "+line).getBytes());
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Receive implements Runnable{
//接收客户端信息 群发到list表中的所有客户端中
    ArrayList<Socket> list = null;
    Socket socket=null;
    InetAddress IP=null;
    int count=-1;

    public Receive (ArrayList list,Socket socket,int count){
        this.list=list;
        this.socket=socket;
        this.IP=socket.getInetAddress();//
        this.count=count;
        socket.getLocalSocketAddress();

    }

    @Override
    public void run() {

        while(true){
            try {
                byte[] buf=new byte[1024];
                InputStream in=socket.getInputStream();
                int len=in.read(buf);
                String line= new String(buf, 0, len);
                String content="Client "+count+": "+line;
                System.out.println(content);


                for(int i=0;i<list.size();i++){
                    //群发
                    OutputStream out=list.get(i).getOutputStream();
//                    System.out.println(content);
                    out.write(content.getBytes());
                    System.out.println("已发送");
                }

                if(line.equals("886")){
                    System.out.println(IP+" left");
                    socket.close();
                    break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}

class StartServer{
    ArrayList<Socket> list = null;
    int port=10005;
    public StartServer(ArrayList<Socket> list,int port){
        this.list=list;
        this.port=port;
    }

    public void Start() throws IOException {
        ServerSocket ss=new ServerSocket(10005);

        int count=0;

        while(true){
            Socket socket=ss.accept();
            list.add(socket);
            count++;
            System.out.println(count+" Client added");
//            线程群发客户端信息
            new Thread(new Receive(list,socket,count)).start();
//            服务器发送信息给客户端

            Send sToAll=new Send(list);
            new Thread(sToAll).start();
        }

    }

    public static void main(String[] args) {
        try {
            ArrayList<Socket> list=new ArrayList<Socket>();
            StartServer s=new StartServer(list,10005);
            s.Start();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
