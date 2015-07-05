package com.ChatCharacterStream;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by DELL on 14-4-6.
 */
public class ActionServer implements Runnable {

ChatServer chat= null;
public String clientSay =null;
public ArrayList<Socket> socketlist=new ArrayList();

public ActionServer(ChatServer a){
this.chat=a;
}

    @Override
    public void run() {
        socketlist.add(chat.s);


        while(true){
            try {
               String line= chat.getClientInfo();
               System.out.println(chat.SocketName+" Said: "+line);
               clientSay=chat.SocketName+" Said: "+line;

                for(int i=0;i<socketlist.size();i++){
                    byte [] buf=clientSay.getBytes();
                    socketlist.get(i).getOutputStream().write(buf);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }




}