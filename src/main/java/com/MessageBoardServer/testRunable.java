package com.MessageBoardServer;

import java.util.Objects;

/**
 * Created by DELL on 14-3-19.
 */
public class testRunable implements Runnable{
    private int count =5;
//    public void run() {
//
//        while(true){
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        if (count>0){
//            System.out.println(Thread.currentThread().getName()+"  >>>"+count--);
//        }else{
//            return;
//        }
//    }
//
//}
    public String getName(){
//        System.out.println("before print Name");

        return String.valueOf(Thread.currentThread().getName());

    }

//    private Object obj=new Object();
    public void run(){
//        System.out.println("before new"+Thread.currentThread().getName());

        dbHandler a=dbHandler.getInstance();

//            System.out.println("finish new"+Thread.currentThread().getName());
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        String b=new String();
            try {
                 b=a.testAdd("哈").toString();
            }catch (Exception e){
                System.out.println("$$$$$$$$$$$$$$$$-An error in lisa.toString");
            }
        System.out.println(Thread.currentThread().getName()+ b);
//        System.out.println(Thread.currentThread().getName()+ a.testAdd("哈"));


    }



    public static void main(String[] args) throws InterruptedException {
        testRunable a1=new testRunable();



        new Thread(a1,"1号").start();
        new Thread(a1,"2号").start();
//        Thread tread2=new Thread(a1,"5号");
//        tread2.start();
////        tread2.join();
//
//        Thread tread1=new Thread(a1,"4号");
//        tread1.start();
//        tread1.join();
        new Thread(a1,"3号").start();
        new Thread(a1,"4号").start();
        new Thread(a1,"5号").start();
        new Thread(a1,"6号").start();
        new Thread(a1,"7号").start();
        new Thread(a1,"7号").start();
        new Thread(a1,"7号").start();
        new Thread(a1,"7号").start();
        new Thread(a1,"7号").start();

        System.out.println(Thread.currentThread().getName());


//        dbHandler b=dbHandler.getInstance();
//        b.testAdd("啊");

        dbHandler a=dbHandler.getInstance();
        dbHandler b=dbHandler.getInstance();
        if(a==b){
            System.out.println("YES");
        }else {
            System.out.println("NO");
        }

    }



}
