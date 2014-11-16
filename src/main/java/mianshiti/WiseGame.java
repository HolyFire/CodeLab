package mianshiti;

import com.Test.TestNetty;

import java.io.*;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by DELL on 2014-11-04.
 */
public class WiseGame {
//    输入11223334 返回(1,[1,1]) (2,[22]))(3,[333])(4,[4])
    public Map<Integer,List<Integer>> ReadMap (Collection<Integer> collection){
        Map<Integer,List<Integer>> map=new HashMap<Integer, List<Integer>>();
//        for(int i=0;i<10;i++){
//            map.put(i,new ArrayList<Integer>());
//        }

        Iterator<Integer> iterator=collection.iterator();
        while (iterator.hasNext()){
            int temp=iterator.next();
            if(!map.containsKey(temp)){
                map.put(temp,new ArrayList<Integer>());
                map.get(temp).add(temp);
            }else {
                map.get(temp).add   (temp);
                System.err.println(map.get(temp));
            }
;
        }
        return map;

    }

//    求两个时间之间的毫秒数 2011-11-12 23:52:34
    public long getTime(String date1,String date2){
        SimpleDateFormat dfs=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long between=0;
        try {
            java.util.Date begin=dfs.parse(date1);
            java.util.Date end=dfs.parse(date2);
            between=end.getTime()-begin.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return between;
    }

    private void dddd(){

    }

    private void FFFF(){

    }
//list中的内容分页放入数组  a[0][2] 表示第一页第三个数
    public int[][] readPage(List<Integer> list,int pageSize){
        int page=(int)Math.ceil((double)list.size()/(double)pageSize);
        int a[][]=new int[pageSize][page];
        int count=0;
        for(int i=0;i<pageSize;i++){
            if(count>=list.size()){
                break;
            }
            for(int j=0;j<list.size();j++){
                a[i][j]=list.get(count++);
            }
        }
        return a;
    }

//    将类中所有的private 方法打印出来
    public String[] readMethod(Object object){
        String[] methodNames=new String[10];
        int count=0;
        Method[] methods= object.getClass().getDeclaredMethods();
        for(int i=0;i<methods.length;i++){
//            System.out.println(methods[i].getName());
            if(!methods[i].isAccessible()){
                String verifyMethod=methods[i].getName();

                try{
                    object.getClass().getMethod(verifyMethod,methods[i].getParameterTypes());
                } catch (NoSuchMethodException e) {
                    System.err.println(verifyMethod);
                    methodNames[count++]=verifyMethod;
                }
//                System.err.println(methods[i].getName());
            }
        }

        return methodNames;
    }

//    遍历目录中所有的文件 包括子目录
    public Collection<String> getFileName(String path){
        ArrayList<String> list=new ArrayList<String>();
        new WiseGame().GetFile(path,list);
        return list;
    }
    public Collection<String> GetFile(String path,ArrayList<String> list){
        File file=new File(path);
        File[] files=file.listFiles();
        for(int i=0;i<files.length;i++){
            if(files[i].isFile()){
//                synchronized (this){
                    list.add(files[i].getName());
//                }
            }else if(files[i].isDirectory()){
                System.err.println(files[i].toString());
                new WiseGame().GetFile(files[i].toString(),list);
            }
        }
        return list;
    }

    public static void main(String[] args) throws IOException {
//        WiseGame wiseGame=new WiseGame();
//        new WiseGame().readMethod(wiseGame);
//        double page=Math.ceil(10.00/3.00);
//        System.err.println((int)page);
//        System.err.println(new WiseGame().getFileName("E:\\newJ"));
//        ArrayList<Integer> list=new ArrayList<Integer>();
//        list.add(2);list.add(2);list.add(2);
//        list.add(1);list.add(1);
//        list.add(3);list.add(3);list.add(3);
//        try {
//            FileOutputStream file=new FileOutputStream("F:\\新建文件夹\\a.txt");
//            file.write("dasdsadasdsadasdasd".getBytes());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        FileInputStream in = new FileInputStream("F:\\新建文件夹\\a.txt");
        in.skip(9); // 跳过前面的9个字节
//        InputStreamReader in2=new InputStreamReader(in);
//        byte[] b=new byte[500];
//        int c = in2.read();
//        FileInputStream in4=new FileInputStream("F:\\新建文件夹\\a.txt"); in4.skip(5); int e=in4.read();
//        RandomAccessFile in3=new RandomAccessFile("F:\\新建文件夹\\a.txt","r"); in3.skipBytes(5); int d=in3.readByte();
//
//        char a=97;
//        System.out.println(a+"d"+d+"s"+e+"d");  // 输出为10
        in.close();
        Pattern p = Pattern.compile("(name:)([a-zA-Z]*)(,age:)([0-9]*)");
        
        Matcher m = p.matcher("name:vunv,age:20");
        while (m.find()) {
            System.out.println(m.group(1));
            System.out.println(m.group(4));
        }
//        System.out.println(new WiseGame().ReadMap(list));
    }
}
