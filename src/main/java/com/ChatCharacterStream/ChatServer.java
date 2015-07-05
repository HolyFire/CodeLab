package com.ChatCharacterStream;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by DELL on 14-4-4.
 * 1. 开启服务器
 *      连接客户端
 *
 * 2. 接收所有客户端消息
 *      记录所有客户端的socket
 *
 * 3. 将客户端的消息群发到所有已经连接的客户端上
 *      发送前判断socket的连接状态
 *      群发
 */
public class ChatServer {

    public String SocketName=null;
    public ServerSocket ss=null;
    public Socket s = null;
    public int port = 0;

    public ChatServer(String name,int port){
        this.SocketName=name;
        this.port=port;
    }


    public void initServer() throws IOException {
        this.ss=new ServerSocket(port);
        this.s = ss.accept();
    }

    public String getClientInfo() throws IOException {
        byte [] buf=new byte[1024];
        InputStream mes=s.getInputStream();
        int len =mes.read(buf);//从流mes中读取数据存入数组中 并返回数组中的总字节数
        String line = new String(buf,0,len);
        return line;
    }

    public void sendmessage(String mes) throws IOException {
//        InputStreamReader sysIn= new InputStreamReader(System.in);
//        BufferedReader bufferedReader = new BufferedReader(sysIn);
//        String content=bufferedReader.readLine();
        OutputStream out = s.getOutputStream();
        byte [] buf=mes.getBytes();
        out.write(buf);
    }

    public void des() throws IOException {
        s.close();
        ss.close();
    }

    public void sendToAll(){

    }


}
