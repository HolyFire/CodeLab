package com.MessageBoardServer;

import com.MessageBoardServer.Sample.SamplerRecord;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.ServiceLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        List list=new ArrayList();
        String a=JSONObject.toJSONString(samplerRecord);
        System.out.print(a+"~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        list.add(samplerRecord);
        System.out.println(JSONObject.toJSONString(list));
        System.out.println(JSON.toJSONString(list));
        System.out.println(list);
        System.out.println();
        System.out.println(outPut(samplerRecord));
        System.out.println(outPut2(samplerRecord));
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


