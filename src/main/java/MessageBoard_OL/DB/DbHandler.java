package MessageBoard_OL.DB;

import MessageBoard_OL.App.MyBlog;
import MessageBoard_OL.App.MessageBoard;
import MessageBoard_OL.GetLocation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.util.*;

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

//                reader = new BufferedReader(new FileReader(System.getProperty("user.dir")+"/src/Web/sql.cfg"));
//                reader = new BufferedReader(new FileReader(System.getProperty("user.dir")+"/Web/sql.cfg"));
                reader = new BufferedReader(new FileReader(new GetLocation().getLocation()+"/Web/sql.cfg"));

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


//        从Heroku DATABASE_URL 中获取
//        DATABASE_URL      postgres://<username>:<password>@<host>/<dbname>=> postgres://foo:foo@heroku.com:5432/hellodb
////
//        URI dbUri = null;
//        String username=null;
//        String password=null;
//        String dbUrl=null;
//        try {
//
//            dbUri = new URI(System.getenv("DATABASE_URL"));
//            username = dbUri.getUserInfo().split(":")[0];
//            password = dbUri.getUserInfo().split(":")[1];
//            dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();
//
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }




//        String url="jdbc:postgresql://127.0.0.1:5432/mydb";
        //        url=jdbc:postgresql://127.0.0.1:5432/mydb
//        String url="jdbc:mysql://SAE_MYSQL_HOST_M:SAE_MYSQL_PORT/app_wittyc";
        String url=map.get("url");

//        String name="deepfuture";
//        String name="3354www32o";
        String name=map.get("username");

//        String password="4hkxxyj5w4m04kyhwlxhyjj55055m5jj03k05i14";
//        String password="123123";
        String password2=map.get("password");

        try {
            Class.forName("org.postgresql.Driver");
//            com.mysql.jdbc.Driver
//            Class.forName(map.get("sqlname"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
//            connection=DriverManager.getConnection(dbUrl,username,password);
            connection= DriverManager.getConnection(url,name,password2);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ResultSet rsusTable=connection.getMetaData().getTables(null,null,"clientuser",null);
            if(rsusTable.next()){
                System.out.println("The table clientuser exists");
            }else{
                String sql="create table clientuser(username varchar(50) primary key, password varchar(50))";
                Statement statement=connection.createStatement();
                statement.executeUpdate(sql);
                System.out.println("The table user is created");
            }

            ResultSet rsTable=connection.getMetaData().getTables(null,null,"messcontent",null);
            if(rsTable.next()){
                System.err.println("The table MessContent exists");
            }
            else {
//                String sql="create table MessContent (id int primary key,Content varchar(8192));";
//                留言表
                String sql="create table messcontent (id int primary key,content varchar(8192),username varchar(50) references clientuser(username),location varchar(50) default '未知');";
//                文章表
                String blog="create table myblog (id serial primary key ,title varchar(100) ,blogtext text,category varchar(50), tag varchar (100),createtime DATE,updatetime DATE);";


//                String addLocation="alter table messcontent add location varchar(50)";
//                String setDefaultLocation="alter table messcontent alter column location set default '未知';";

                Statement statement=connection.createStatement();
                statement.executeUpdate(sql);
                statement.executeUpdate(blog);
//                statement.executeUpdate(addLocation);
//                statement.executeUpdate(setDefaultLocation);
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

    public boolean write(int id,String content,String username,String location){
        try {
            Statement statement=connection.createStatement();
            String sql="INSERT INTO messcontent VALUES ("+id+",'"+content+"','"+username+"','"+location+"')";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  true;
    }

    public boolean write(String username,String password){
        try {
            Statement statement=connection.createStatement();
            String sql="insert into clientuser values('"+username+"','"+password+"')";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    public int readcount(String table){
        int count=0;
        String sql="select count(*) from "+table+";";
        try {
            Statement statement=connection.createStatement();
            ResultSet rs=statement.executeQuery(sql);
            ResultSetMetaData rsm=rs.getMetaData();
//            int num=rsm.getColumnCount();
            rs.next();
            String ColumnName=rsm.getColumnName(1);
            System.err.println("ColumName in readcount="+ColumnName);
            Object sqlview=rs.getString(ColumnName);
            count=Integer.valueOf(sqlview.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public int readcount(String table,String column,String value){
        int count=0;
        String sql="select count(*) from "+table+" where "+column+"='"+value+"';";
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

//    查询留言板中全部信息
    public MessageBoard showInMes(int id){
        MessageBoard board=new MessageBoard();
        try {
            Statement statement=connection.createStatement();
            String sql="select * from messcontent where id="+id;
            ResultSet rs=statement.executeQuery(sql);
            ResultSetMetaData rsm=rs.getMetaData();
            int count=rsm.getColumnCount();
            if(!rs.next()){

            }else {
                board.setContent(rs.getString("content"));
                board.setUsername(rs.getString("username"));
                board.setLocation(rs.getString("location"));
//                System.err.println(board);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return board;
    }

    public String read(int id){
//read board content
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
//                for(int i=0;i<count;i++){

//                String ColumnName=rsm.getColumnName(i+1);
                String ColumnName=rsm.getColumnName(1);
                    Object sqlview=rs.getString(ColumnName);
                    content=sqlview.toString();
//                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  content;
    }

    public String readusername(int id){
        String username=new String();
        try {
            Statement statement=connection.createStatement();
            String sql="select username from messcontent where id="+id;
            ResultSet rs=statement.executeQuery(sql);
            ResultSetMetaData rsm=rs.getMetaData();
            int count=rsm.getColumnCount();

            if(!rs.next()){
                System.out.println("No Data in");
            }else {
                for(int i=0;i<count;i++){
                    String ColumnName=rsm.getColumnName(i+1);
                    Object sqlview=rs.getString(ColumnName);
                    username=sqlview.toString();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  username;
    }

    public String read(String username){
//        readpassword
        String password=new String();
        try {
            Statement statement=connection.createStatement();
            String sql="select password from clientuser where username='"+username+"'";
            ResultSet rs=statement.executeQuery(sql);
            ResultSetMetaData rsm=rs.getMetaData();
            int count=rsm.getColumnCount();
            if(!rs.next()){
                System.out.println("no password in");
            }else {
                String ColumnName=rsm.getColumnName(1);
                Object sqlview=rs.getString(ColumnName);
                password=sqlview.toString();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return password;
    }

//~~~~~~~~  blog 添加
    public void writeBlog(MyBlog myBlog){
// "create table myblog (id serial primary key ,title varchar(100) ,blogtext text,category varchar(50), tag varchar (100),createtime DATE,updatetime DATE ;"
        Timestamp now =new Timestamp(new java.util.Date().getTime());
        int id=myBlog.getId();
        String title=myBlog.getTitle();
        String blogtext=myBlog.getBlogText();
        String category=myBlog.getCategory();
        String tag=myBlog.getTag();
        Timestamp createtime=now;
        Timestamp updatetime=now;

        String sql="insert into myblog (title,blogtext,category,tag,createtime,updatetime) values(null,'"+title+"','"+blogtext+"','"+category+"','"+tag+"','"+createtime+"','"+updatetime+"');";
        try {
            Statement statement=connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

//~~~~~~~~~read last blog id
    public int readBlogLastId() throws SQLException {
        int lastid=0;
        Statement statement=connection.createStatement();
        String sql="select MAX(id) from myblog";
        ResultSet rs=statement.executeQuery(sql);
        ResultSetMetaData rsm=rs.getMetaData();
        if (rs.next()){
            lastid=rs.getInt(rsm.getColumnName(1));
        }
        return  lastid;
    }


//~~~~~~~~~ blog 读取

    public MyBlog readBlogIndex(int id) throws SQLException {
        MyBlog blog=new MyBlog();

        Statement statement=connection.createStatement();
        String sql="select * from myblog where id="+id;
        ResultSet rs=statement.executeQuery(sql);
        ResultSetMetaData rsm=rs.getMetaData();
        int count=rsm.getColumnCount();
        if(!rs.next()){
            System.err.println("No blog with this id");
        }else {
            blog.setId(rs.getInt("id"));
            blog.setBlogText(rs.getString("blogtext"));
            blog.setCategory(rs.getString("category"));
            blog.setTag(rs.getString("tag"));
            blog.setCreatetime(rs.getDate("createtime"));
            blog.setTitle(rs.getString("title"));
            blog.setUpdatetime(rs.getDate("updatetime"));
        }

        return blog;

    }

//    tag_pool tag池读取
    public HashSet<String> readTagPool(){
        String sql="";
        HashSet<String> set=new HashSet<String>();
        try {
            Statement statement=connection.createStatement();
            ResultSet rs=statement.executeQuery(sql);
            while (rs.next()){
               set.add(rs.getString("id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return set;

    }


}
