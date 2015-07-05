package MessageBoard_OL.Control;

import MessageBoard_OL.GetLocation;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by DELL on 2014-12-22.
 */
public class ModelTrans {
    public static String keyWordsReplace(String path,HashMap<String,String> map) throws IOException {
        path=new GetLocation().getLocation()+"/Web"+path;
        StringBuilder sb = new StringBuilder();
        Pattern re=Pattern.compile("&begin:(.*?):end&");
        File file=new File(path);
        FileInputStream fileIn=new FileInputStream(file);

        BufferedReader reader=new BufferedReader(new InputStreamReader(fileIn));
        String content;
        while((content=reader.readLine())!=null){
            sb.append(content+"\n");
//            System.out.println(content);
        }
        String replace=sb.toString();
        Matcher m=re.matcher(sb);
        while (m.find()){
//            System.out.println("find");
//            System.out.println(m.group(1));
//            System.out.println(map.get("title"));
            if(map.get(m.group(1))!=null){
//                System.out.println("not null");
//                System.out.println(m.group(1));
//                replace= m.replaceFirst(map.get(m.group(1)));
                replace=replace.replaceAll("&begin:"+m.group(1)+":end&",map.get(m.group(1)));
            }
//                Iterator<String> iterator=map.keySet().iterator();
//                while (iterator.hasNext()){
//                }
        }

        return replace;
    }

    public static void main(String[] args) {
        try {
            String a=new ModelTrans().keyWordsReplace(new GetLocation().getLocation()+"/Web/ViewModel/ContentViewModel.html",new HashMap<String, String>());
            System.out.println(a);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
