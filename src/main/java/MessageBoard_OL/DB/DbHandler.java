package MessageBoard_OL.DB;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by DELL on 14-8-2.
 */
public class DbHandler {
    private Connection connection=null;

    private DbHandler(){

    }

    private static DbHandler dbHandler=new DbHandler();
    public static DbHandler getDbHandler(){
        return  dbHandler;
    }

    public void init(){

//读取sql.cfg文件
            BufferedReader reader = null;
        HashMap<String,String> map=new HashMap();
            try
            {

                reader = new BufferedReader(new FileReader(System.getProperty("user.dir")+"/src/main/java/MessageBoard_OL/sql.cfg"));

                String line;
                while ((line = reader.readLine()) != null)
                {
                    line = line.trim();
                    //如果行长度为0或者首字节是#或[
                    if ((line.length() == 0) || (line.charAt(0) == '#') || (line.charAt(0) == '['))
                    {
                        continue;
                    }
                    int splitPos = line.indexOf('=');
                    if (splitPos != -1)
                    {
                        //等号前为键 等号后为值
                        line.substring(0,splitPos);
                        map.put(line.substring(0, splitPos).toLowerCase(Locale.ENGLISH).trim(),
                                line.substring(splitPos + 1, line.length()).trim());
                    }
                }
            }
            catch (Exception e)
            {
                // TODO: handle exception
            }
            finally
            {
                // 关闭文件句柄
                try
                {
                    if (reader != null)
                    {
                        reader.close();
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }



//        String url="jdbc:postgresql://127.0.0.1:5432/mydb";
//        String url="jdbc:mysql://SAE_MYSQL_HOST_M:SAE_MYSQL_PORT";
        String url=map.get("url");

//        String name="deepfuture";
//        String name="SAE_MYSQL_USER";
        String name=map.get("username");

//        String password="SAE_MYSQL_PASS";
//        String password="123123";
        String password=map.get("password");

        try {
//            Class.forName("org.postgresql.Driver");
            Class.forName(map.get("sqlname"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection= DriverManager.getConnection(url,name,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ResultSet rsTable=connection.getMetaData().getTables(null,null,"messcontent",null);
            if(rsTable.next()){
                System.err.println("The table MessContent exists");
            }
            else {
                String sql="create table MessContent (id int primary key,Content varchar(8192));";
                Statement statement=connection.createStatement();
                statement.executeUpdate(sql);
                System.out.println("The Table MessContent is created");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void des(){
        try {

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean write(int id,String content){
        try {
            Statement statement=connection.createStatement();
            String sql="INSERT INTO MessContent VALUES ("+id+",'"+content+"')";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  true;
    }

    public int readcount(String str){
        int count=0;
        String sql="select count(*) from messcontent;";
        try {
            Statement statement=connection.createStatement();
            ResultSet rs=statement.executeQuery(sql);
            ResultSetMetaData rsm=rs.getMetaData();
//            int num=rsm.getColumnCount();
            rs.next();
            String ColumnName=rsm.getColumnName(1);
            Object sqlview=rs.getString(ColumnName);
            count=Integer.valueOf(sqlview.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    public String read(int id){

        String content=new String();
        try {
            Statement statement=connection.createStatement();
            String sql="select content from messcontent where id="+id;
            ResultSet rs=statement.executeQuery(sql);
            ResultSetMetaData rsm=rs.getMetaData();
            int count=rsm.getColumnCount();

            if(!rs.next()){
                System.out.println("No Data in");
            }else {
                for(int i=0;i<count;i++){
                    String ColumnName=rsm.getColumnName(i+1);
                    Object sqlview=rs.getString(ColumnName);
                    content=sqlview.toString();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  content;
    }




}
