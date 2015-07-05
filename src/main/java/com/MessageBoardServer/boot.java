package com.MessageBoardServer;

import MessageBoard_OL.App.MessageBoard;
import com.MessageBoardServer.Sample.SamplerRecord;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.ServiceLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by DELL on 14-3-8.
 */



public class boot {

    public static void main(String[] args) {
        //从这里开始
        System.out.println("hello");
        //创建一个实例
        SamplerRecord samplerRecord = new SamplerRecord();
        samplerRecord.setId(1);
        samplerRecord.setName("Zhang");
        samplerRecord.setBirthDay(19900101);
        samplerRecord.setLocation("SiChuan");

        SamplerRecord samplerRecord1 = new SamplerRecord();
        samplerRecord1.setId(1);
        samplerRecord1.setName("Zhang");
        samplerRecord1.setBirthDay(19900101);
        samplerRecord1.setLocation("SiChuan");

        List list=new ArrayList();
        System.err.println(samplerRecord);
        String a=JSONObject.toJSONString(samplerRecord);
        System.out.println(a + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        HashMap map=new HashMap();
        map.put("aaa","asdasda");
        map.put("aaa","dddddsd");

        System.err.println(JSONObject.toJSONString(map));

        list.add(samplerRecord);
        list.add(samplerRecord1);

        MessageBoard messageBoard=new MessageBoard("cccc","cccc","cccc");
        System.err.println(messageBoard);
        System.err.println(JSONObject.toJSONString(messageBoard));

//        System.err.println(list);
//        System.err.println(JSONObject.toJSONString(list));
//        System.err.println(JSON.toJSONString(list));
//        System.out.println(list);
//        System.out.println();
//        System.out.println(outPut(samplerRecord));
//        System.out.println(outPut2(samplerRecord));
    }

        public static String outPut(SamplerRecord samplerRecord){
            String temp=("["+"{"+"\"Name\""+":"+"\""+samplerRecord.getName()+"\""+","+"\"ID\""+":"+"\""+samplerRecord.getId()+"\""+","+"\"BrithDay\""+":"+"\""+samplerRecord.getBirthDay()+"\""+","+"\"From\""+":"+"\""+samplerRecord.getLocation()+"\""+"}"+"]");
            return temp;
        }


        public static StringBuffer outPut2(SamplerRecord samplerRecord){
            StringBuffer temp=new StringBuffer();
            temp.append("[");
            temp.append("{");
            temp.append("\"Name\""+"\":"+samplerRecord.getName()+"\",");
            temp.append("\"ID\""+"\":"+samplerRecord.getId()+"\",");
            temp.append("\"BirthDay\":"+"\""+samplerRecord.getBirthDay()+"\",");
            temp.append("\"From\""+"\":"+samplerRecord.getLocation()+"\"");
            temp.append("}");
            temp.append("]");
            return temp;
        }
//        System.out.println(samplerRecord.toString());
//
//        System.out.println(JSON.toJSONString(samplerRecord.toString()));
//
//        ServiceLoader s=new ServiceLoader();


        ///以json打印出来

    }


