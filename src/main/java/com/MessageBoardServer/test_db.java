package com.MessageBoardServer;
import java.sql.*;
import java.util.*;

import org.postgresql.*;

/**
 * Created by DELL on 14-3-12.
 */
public class test_db {
    public static void main(String[] argv) {



                System.out.println("-------- PostgreSQL "
                + "JDBC Connection Testing ------------");

        try {

            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {

            System.out.println("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            e.printStackTrace();
            return;

        }

        System.out.println("PostgreSQL JDBC Driver Registered!");

        Connection connection = null;

        try {

             connection = DriverManager.getConnection(
                    "jdbc:postgresql://127.0.0.1:5432/mydb", "deepfuture",
                    "123123");

        } catch (SQLException e) {

            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;

        }

        if (connection != null) {
            System.out.println("You made it, take control your database now!");
        } else {
            System.out.println("Failed to make connection!");
        }
        //检测数据库连接

        try {
            Statement stmt = connection.createStatement();
            String sql = "select * from cities;";
            String insert1="insert into cities values"+"('Sichuan',"+"'(100.,21)');";
            System.out.println(sql);
            System.out.println(insert1);
//            stmt.executeUpdate(insert1);
//            stmt.executeQuery(sql);

           ArrayList weather=new ArrayList();

            ResultSet rs=stmt.executeQuery(sql);
            ResultSetMetaData rsm=rs.getMetaData();
            int count = rsm.getColumnCount();

            while (rs.next()){
                HashMap map = new HashMap();
                for(int i=0;i<count;i++){
                String columnName =rsm.getColumnName((i+1));
                    int sqlType=rsm.getColumnType(i + 1);
                    Object sqlView = rs.getString(columnName);
                    if(Types.CHAR==sqlType&&null !=sqlView)  {
                        map.put(columnName, sqlView.toString().trim());//trim 去空格
                    }else {
                        map.put(columnName,sqlView);
                        System.out.println("------->" + columnName+sqlView);
                    }
                } 
                weather.add(map);//将HashMap 添加到ArrayList
            }

            for (Object a:weather){
                System.out.println("~~~~~~~~~~~~~~~~"+a.toString());

            }
              

            Iterator<HashMap<String, String>> iterator = weather.iterator();
            while (iterator.hasNext()) {
                HashMap<String, String> map = iterator.next();
                Set<String> set = map.keySet();
                Iterator<String> it = set.iterator();

                while (it.hasNext()) {
                    String key = it.next();
                    System.out.println(key + "***" + map.get(key));
                }
            }

//            for (Map<String, Object> map : weather) {
//                for (Map.Entry<String, Object> entry : map.entrySet()) {
//                    System.out.println(entry.getKey() + " " + entry.getValue());
//                }
//            }
            
            rs.close();
            stmt.close();
            connection.close();



        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}
