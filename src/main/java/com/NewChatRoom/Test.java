package com.NewChatRoom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by DELL on 14-4-15.
 */
public class Test {
    public static void main(String[] args) throws IOException, InterruptedException {
        while(true){
            InputStreamReader in=new InputStreamReader(System.in);
            BufferedReader buf=new BufferedReader(in);
            String line=buf.readLine();
            System.out.println(line);
            if(line.equals("bye")){
                break;
            }

        }
    }
}
