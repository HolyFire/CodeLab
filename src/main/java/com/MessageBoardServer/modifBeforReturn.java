package com.MessageBoardServer;

/**
 * Created by DELL on 14-3-31.
 */
public class modifBeforReturn implements Runnable {
    public void run(){
        dbHandler b=dbHandler.getInstance();
        b.addInt(1);
        System.out.println(Thread.currentThread().getName()+b.count);
    }
      
    public static void main(String[] args){
        modifBeforReturn a=new modifBeforReturn();

        new Thread(a,"1号").start();
        new Thread(a,"2号").start();
        new Thread(a,"3号").start();
        new Thread(a,"4号").start();
        new Thread(a,"5号").start();
        new Thread(a,"6号").start();


    }
    /*

    另一个线程可以在该线程return前 更改数据
     */
}
