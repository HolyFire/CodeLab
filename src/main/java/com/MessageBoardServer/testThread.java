package com.MessageBoardServer;

/**
 * Created by DELL on 14-3-19.
 */
public class testThread extends Thread{
    private static int count=5;

    public void run(){

        while (count>0){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(testThread.currentThread().getName()+"    >>"+count--);
            }

    }

    public static void main(String[] args) {
        testThread r1=new testThread();
        testThread r2=new testThread();
        testThread r3=new testThread();
        r1.start();
        r2.start();
        r3.start();
    }

}
