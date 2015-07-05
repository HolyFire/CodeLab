package com.Test;

/**
 * Created by DELL on 2014-11-03.
 */
public class Person{
    public String name="Person";
    int age=0;
    public Person(){

        System.err.println("dddddd");
    }
}
 class Child extends Person{

     public Child(){
         super();
     }
//     static {
//         System.err.println("aaa");
//     }
    public String grade;
    public static void main(String[] args){
//        new Child().name="ddd";
//        System.out.println("ss");
        int x = 3;
        int y = 4;
        int ret = x++ * ++y;

    }
}
