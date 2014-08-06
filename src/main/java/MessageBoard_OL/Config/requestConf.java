package MessageBoard_OL.Config;

import MessageBoard_OL.DB.DbHandler;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by DELL on 14-8-6.
 */
public class requestConf {
    public requestConf(HttpRequest request,DbHandler db) throws URISyntaxException {

        URI uri= new URI(request.getUri());
        String method=uri.getPath();
        int count=db.readcount("messcontent");

        //            处理POST请求
        if(request.getMethod().equals(HttpMethod.POST)){

            if(method.equals("/index.html")){
                System.err.println("~~~~~~~~~~~~~~~~!!~~~~~~~~~~~~~~");

                HttpDataFactory factory=new DefaultHttpDataFactory(false);
                HttpPostRequestDecoder decoder=new HttpPostRequestDecoder(factory,request);
                List list=decoder.getBodyHttpDatas();
                if(list.get(0).toString().equals("chatText=")){
                    System.out.println("内容为空"+list.get(0).toString());
                    return;
                }

//                  分割等号前后数据 "a=b"
                String[] array=list.get(0).toString().split("=");
                String content=array[1];
                count++;
                db.write(count,content);

//                    for(String a:array){
//                        System.out.println("@@@@@@"+a);
//                    }
                System.err.println( list.get(0));
                System.err.println(decoder.getBodyHttpDatas());

//                    ~~GET~~
//                    QueryStringDecoder decoder=new QueryStringDecoder(request.getUri());
//                    Map map=decoder.parameters();
//                    System.out.println(map.get("chatText") + "~!!~!~!~!~!~!~!~!~!~");
//                    System.err.println("~~~~~~~~~~~~"+decoder.parameters());

            }

            if(method.equals("/signup")){
                HttpDataFactory factory=new DefaultHttpDataFactory(false);
                HttpPostRequestDecoder decoder=new HttpPostRequestDecoder(factory,request);
                List list=decoder.getBodyHttpDatas();

//                分割数据放入map中
//                TODO
                HashMap<String,String> map=new HashMap();
                for(int i=0;i<list.size();i++){

                    String[] array=list.get(i).toString().split("=");
                    map.put(array[0],array[1]);
                }
                System.out.println(map.toString());



            }

        }
    }
}
