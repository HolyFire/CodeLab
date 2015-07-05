package com.MessageBoardServer;
import java.sql.*;

/**
 * Created by DELL on 14-3-19.
 */
public class testTime {
    public static void main(String[] args) {
        Connection connection = null;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String URL=("jdbc:postgresql://127.0.0.1:5432/mydb");
        String User=("deepfuture");
        String PS=("123123");

        try {
            connection= DriverManager.getConnection(URL,User,PS);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Statement stmt=connection.createStatement();
            int i=0;



//            long startMili=System.currentTimeMillis();
//            for(i=0;i<10000;i++){
//                String insert="insert into cities values"+"('Sichuan',"+"'(100.,21"+i+")');";
//                System.out.println(insert);
//            stmt.executeUpdate(insert);
//                System.out.println(i);
//            }
//            long endMili= System.currentTimeMillis();
//            System.out.println("用时"+(endMili-startMili)/1000+"秒");

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}
