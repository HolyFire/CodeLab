package com.ChatRoomTCP;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.SortedMap;

/**
 * Created by DELL on 14-4-2.
 */
public class chatRoomClient {

    public Thread getOutput(Socket sorket) {

        output o = new output(sorket);
        Thread thread = new Thread(o, "一号线");
        return thread;

    }

    public Thread getReceive(Socket sorket) {
        receive r = new receive(sorket);
        Thread thread = new Thread(r, "二号线");
//        thread.setDaemon(true);
        return thread;
    }


    private class output implements Runnable {
        Socket socket = null;

        public output(Socket socket) {

            this.socket = socket;

        }

        @Override
        public void run() {
            String chatContent = null;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
//System.out.println("1~~~~~~~~~~output");
            try {

                while ((chatContent = bufferedReader.readLine()) != null) {
                    OutputStream out = socket.getOutputStream();
                    byte[] buf = chatContent.getBytes();
                    out.write(buf);
//                    System.out.println("2~~~~~~~~~~output");

                    if (chatContent.equals("886")) {
                        break;
                    }
                }

            socket.close();


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public class receive implements Runnable {
        Socket socket = null;

        public receive(Socket socket) {

            this.socket = socket;
        }

        @Override
        public void run() {
//            System.out.println("1~~~~~~~~~~receive");


            try {

                InputStream in = socket.getInputStream();


                while (true) {

//                    socket.setKeepAlive(true);

                    try{
                        socket.sendUrgentData(0xFF);
                    }catch(Exception e){
                        System.out.println("reconnect");
                        break;
                    }

                    byte[] buf = new byte[1024];
                    if (socket.isConnected()) {
//                        TODO socket.close 后 下条语句会报错
                        int len = in.read(buf);
                        String text = new String(buf, 0, len);
                        System.out.println("Server Said: " + text);
                        if (text.equals("886")) {
                            System.out.println("Server is exited");

                        }
//                    System.out.println("2~~~~~~~~~~receive");
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

}
