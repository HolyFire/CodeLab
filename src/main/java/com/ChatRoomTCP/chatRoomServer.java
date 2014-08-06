package com.ChatRoomTCP;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by DELL on 14-4-2.
 */
public class chatRoomServer {


    public Thread getSvOut(Socket socket) {
        SvOut s = new SvOut(socket);
        Thread t1 = new Thread(s, "服务器1号线");
        return t1;
    }

    public Thread getSvReceive(Socket socket) {
        svReceive s = new svReceive(socket);
        Thread t2 = new Thread(s, "服务器2号线");
//        t2.setDaemon(true);
        return t2;
    }

    private class SvOut implements Runnable {
        private Socket ClSocket = null;

        public SvOut(Socket socket) {
            this.ClSocket = socket;
        }

        private Object obj = new Object();

        @Override
        public void run() {

            String chatContent = null;
//            System.out.println("i'm in Svout 1~~~~~~`");
            try {

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                OutputStream out = ClSocket.getOutputStream();
                while ((chatContent = bufferedReader.readLine()) != null) {
//                    synchronized (obj){
                    byte[] buf = chatContent.getBytes();
                    out.write(buf);
                    if (chatContent.equals("886")) {
                        break;
                    }
//                    System.out.println("~~~~~~~~~~~I'm in Svout 2");
//                    }
                }
//                ClSocket.close();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class svReceive implements Runnable {
        private Socket ClSocket = null;

        public svReceive(Socket socket) {
            this.ClSocket = socket;
        }

        private Object obj = new Object();

        @Override
        public void run() {

//            System.out.println("I'm in svReceive 1~~~~~~~");
            try {


                String line = null;

                InputStream in = ClSocket.getInputStream();


                while (true) {
//                    synchronized(obj){
                    byte[] buf = new byte[1024];
                    if (ClSocket.isConnected()) {
                        int len = in.read(buf);
                        String text = new String(buf, 0, len);
                        System.out.println("Client Said: " + text);

                        if (text.equals("886")) {
                            System.out.println("Client is exited");
                        }
                    }
//                    }
//                    System.out.println("I'm in svReceive 1~~~~~~~");

                }

            } catch (IOException e) {
                e.printStackTrace();

            }


        }
    }

/*
将客户端信息广播
获取所有客户端的IP
然后广播
 */
    private void forward(Socket socket){

    }

}
