package MessageBoard_OL.Config;

import MessageBoard_OL.App.User;
import MessageBoard_OL.DB.DbHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.util.CharsetUtil;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static io.netty.buffer.Unpooled.copiedBuffer;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by DELL on 14-8-6.
 */

public class RequestConf {
    private boolean flag=true;




    public RequestConf(ChannelHandlerContext ctx,HttpRequest request, DbHandler db) throws URISyntaxException {


        URI uri= new URI(request.getUri());
        String method=uri.getPath();
        int count=db.readcount("messcontent");
        System.out.println("count+++++++++++++++++++"+count);


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
                array=list.get(1).toString().split("=");
                String username=array[1];
                count++;
                db.write(count,content,username);

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


                db.write(map.get("username").toString(),map.get("password").toString());
                System.out.println(map.toString());


                flag=false;
            }

            if(uri.getPath().equals("/login")){
                HttpDataFactory factory=new DefaultHttpDataFactory(false);
                HttpPostRequestDecoder decoder=new HttpPostRequestDecoder(factory,request);
                List list=decoder.getBodyHttpDatas();
                if(list.get(0).toString().equals("username=")){
                    System.out.println("内容为空"+list.get(0).toString());
                    flag=false;
                    return;
                }if(list.get(1).toString().equals("pw=")){
                    System.out.println("内容为空"+list.get(1).toString());
                    flag=false;
                    return;
                }
                System.err.println(list.toString());
                String[] array=list.get(0).toString().split("=");
                String username=array[1];
                array=list.get(1).toString().split("=");
                String passward=array[1];

//array 0 username 1 unct 2 password 3pwct

                StringBuilder sb=new StringBuilder();
                User user=new User();
                HttpResponse response=new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.OK);
                if(db.readcount("clientuser","username",username)!=1){
                    sb.append("<script>");
                    sb.append("alert(\"用户名不存在\")");
                    sb.append("</script>");
                    sb.append("<script>");
                    sb.append("window.location=\"/login.html\";");
                    sb.append("</script>");
                    flag=false;
                }else{
                if(db.read(username).equals(passward)){
                    //                保存登陆用户的用户名
//                    clientusername=username;

                    Set<Cookie> cookies;
                    String value = request.headers().get(HttpHeaders.Names.COOKIE);
                    if (value == null) {
                        cookies = Collections.emptySet();
                    } else {
                        cookies = CookieDecoder.decode(value);
                    }

                    for (Cookie str : cookies) {
                        System.out.println(str+"Cooooooooooooooooooooooooooooooooookies");
                    }

                    BASE64Encoder encoder=new BASE64Encoder();
                    String strPw=encoder.encode(TripleDes.encryptMode(db.read(username).getBytes()));

                    response.headers().add(HttpHeaders.Names.SET_COOKIE,"realname="+username+";");

                    response.headers().add(HttpHeaders.Names.SET_COOKIE,"realpw="+strPw);



                    sb.append("<script>");
                    sb.append("window.location=\"/index.html\";");

                    sb.append("usename="+"\""+username+"\";");
                    sb.append("</script>");

                    flag=false;
                }else{
                    sb.append("<script>");
                    sb.append("alert(\"密码不正确\")");
                    sb.append("</script>");
                    sb.append("<script>");
                    sb.append("window.location=\"/login.html\";");
                    sb.append("</script>");
                    flag=false;
                }
                }


                    ByteBuf buf=copiedBuffer(sb.toString(), CharsetUtil.UTF_8);
                    response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");
                    response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, buf.readableBytes());
                    ctx.channel().write(response);
                    ctx.channel().writeAndFlush(buf);

            }


//            TODO
            if(method.equals("/checksign")){
                HttpDataFactory factory=new DefaultHttpDataFactory(false);
                HttpPostRequestDecoder decoder=new HttpPostRequestDecoder(factory,request);
                List list=decoder.getBodyHttpDatas();
                if(list.get(0).equals("username=")||list.get(1).equals("password")){

                }else{
                    String[] array=list.get(0).toString().split("=");
                    String username=array[1];
                    array=list.get(1).toString().split("=");
                    String password=array[1];
                    BASE64Decoder base64Decoder=new BASE64Decoder();
                    String realpassword=new String();
                    System.out.println(username+password+"CHECKSIGN");
                    try {
                        realpassword=TripleDes.decryptMode(base64Decoder.decodeBuffer(password)).toString();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(db.read(username).equals(realpassword)){
                        return;
                    }
                }



            }

        }

//        get请求
        if(uri.getPath().equals("/Atest")){
            StringBuilder sb=new StringBuilder();
            System.err.println(count+"~~~~~count");
            for(int i=0;i<count;i++){
                sb.append("<div class=\"panel panel-default\">");
                sb.append("<div class=\"panel-body\"");
                sb.append("<p style=\"word-break:break-all;\">");
                sb.append(db.read(i+1));
                sb.append("</p>");
                sb.append("</div>");
                sb.append("<div class=\"panel-footer\">");
                sb.append("<p style=\"word-wrap:break-word;\" class=\"text-right\">");
                sb.append(db.readusername(i+1));
                sb.append("</p>");
                sb.append("</div>");
                sb.append("</div>");
            }



            ByteBuf buf=copiedBuffer(sb.toString(), CharsetUtil.UTF_8);
            HttpResponse response=new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.OK);
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, buf.readableBytes());
            System.out.println(response.headers().toString() + "~~~~~~~~~~~~");

            ctx.channel().write(response);
            ctx.channel().writeAndFlush(buf);

            flag=false;
        }
    }


    public boolean needContinue(){
        return flag;
    }
}
