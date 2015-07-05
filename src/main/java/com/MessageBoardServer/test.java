package com.MessageBoardServer;

import com.MessageBoardServer.Sample.SamplerRecord;

import java.util.ArrayList;

/**
 * Created by DELL on 14-3-22.
 */
public class test {
    public static void main(String[] args) {
        SamplerRecord samplerRecord = new SamplerRecord();
        samplerRecord.setId(1);
        samplerRecord.setName("Zhang");
        samplerRecord.setBirthDay(19900101);
        samplerRecord.setLocation("SiChuan");

        SamplerRecord li = new SamplerRecord();
        li.setId(2);
        li.setName("li");
        li.setBirthDay(19900102);
        li.setLocation("SiChuan");


        dbHandler db=dbHandler.getInstance();

//        dbHandler dbs=new dbHandler();

        db.inti("jdbc:postgresql://127.0.0.1:5432/mydb", "DELL", "123123");
        db.clear();
//        db.writeRecord(samplerRecord);
//        db.writeRecord(li);
//        db.read("select * from student");
//        db.clear();
        db.des();

//        db.des();

    }

//    public static void writeRecord(SamplerRecord record){
//
//        String name=record.getName();
//        int id=record.getId();
//        int birthDay=record.getBirthDay();
//        String from=record.getLocation();
//        String a="abc";
//        String insert="INSERT INTO student VALUES ('"+name+"',"+id+","+birthDay+",'"+from+"')";
//        System.out.println(insert);
//
//
//    }
//
}
