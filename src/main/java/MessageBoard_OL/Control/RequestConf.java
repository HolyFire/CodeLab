package MessageBoard_OL.Control;

import MessageBoard_OL.App.*;
import MessageBoard_OL.DB.DbHandler;
import MessageBoard_OL.NettyMsServer.MessBoardClient;
import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.util.CharsetUtil;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.netty.buffer.Unpooled.copiedBuffer;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by DELL on 14-8-6.
 */

public class RequestConf {
    private boolean flag=true;




    public RequestConf(ChannelHandlerContext ctx,HttpRequest request, DbHandler db) throws URISyntaxException {


        URI uri = new URI(request.getUri());
        String method = uri.getPath();
        int count = db.readcount("messcontent");
//        System.out.println("count+++++++++++++++++++"+count);


        //            处理POST请求
        if (request.getMethod().equals(HttpMethod.POST)) {

            System.err.println("In read POST");

            if (method.equals("/index.html")) {
//                System.err.println("~~~~~~~~~~~~~~~~!!~~~~~~~~~~~~~~");

                StringBuilder sb = new StringBuilder();

                HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);
                HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(factory, request);
                List list = decoder.getBodyHttpDatas();
                System.err.println(list);
                if (list.get(0).toString().equals("chatText=") || list.get(0).toString().equals("Mixed: chatText=")) {
                    System.out.println("内容为空" + list.get(0).toString());
                    sb.append("<script>");
                    sb.append("alert(\"系统维护，暂时关闭留言功能\");");
                    sb.append("window.location=\"/index.html\";");
                    sb.append("</script>");

                }else
                if (list.get(1).toString().equals("username=") || list.get(1).toString().equals("Mixed: username=")) {
                    System.out.println("内容为空" + list.get(1).toString());
                    sb.append("<script>");
                    sb.append("alert(\"系统维护，暂时关闭留言功能\");");
                    sb.append("window.location=\"/index.html\";");
                    sb.append("</script>");

                }else
                if (list.get(2).toString().equals("location=") || list.get(1).toString().equals("Mixed: username=")) {
                    System.out.println("内容为空" + list.get(1).toString());
                    sb.append("<script>");
                    sb.append("alert(\"系统维护，暂时关闭留言功能\");");
                    sb.append("window.location=\"/index.html\";");
                    sb.append("</script>");

                }else {
//                    分割等号前后数据 "a=b"
                    String content;
                    String a = list.get(0).toString();
                    while (a.charAt(0) == ' ') {
                        a = a.substring(1, a.length());
                    }
                    if (a.indexOf("Mixed: chatText=") == 0) {
                        content = a.substring("Mixed: chatText=".length(), a.length());
                        String[] array;
//                array=list.get(0).toString().split("=");
//                String content=array[1];
                        array = list.get(1).toString().split("=");
                        String username = array[1];
                        array = list.get(2).toString().split("=");
                        String location = array[1];
                        System.err.println(location + "~~~~~~~~~location~~~~");
                        count++;
                        db.write(count, content, username, location);
                        sb.append("<script>");
                        sb.append("window.location=\"/index.html\";");
                        sb.append("</script>");
                    }
                }

//



                ByteBuf buf = copiedBuffer(sb.toString(), CharsetUtil.UTF_8);
                HttpResponse response = new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.OK);
                response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");
                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, buf.readableBytes());
                ctx.channel().write(response);
                ctx.channel().writeAndFlush(buf).addListener(ChannelFutureListener.CLOSE);

                return;


//                    for(String a:array){
//                        System.out.println("@@@@@@"+a);
//                    }
//                System.err.println( list.get(0));
//                System.err.println(decoder.getBodyHttpDatas());

//                    ~~GET~~
//                    QueryStringDecoder decoder=new QueryStringDecoder(request.getUri());
//                    Map map=decoder.parameters();
//                    System.out.println(map.get("chatText") + "~!!~!~!~!~!~!~!~!~!~");
//                    System.err.println("~~~~~~~~~~~~"+decoder.parameters());

            }

            if (method.equals("/signup")) {
                HttpDataFactory factory = new DefaultHttpDataFactory(false);
                HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(factory, request);
                List list = decoder.getBodyHttpDatas();

//                分割数据放入map中
//                TODO
                HashMap<String, String> map = new HashMap();
                StringBuilder sb = new StringBuilder();
                if (list.get(0).toString().equals("username=") || list.get(1).toString().equals("password=")) {
                    System.out.println("账号或密码为空");

                    sb.append("<script>");
                    sb.append("alert(\"账号或密码为空\");");
                    sb.append("window.location=\"/signup.html\";");
                    sb.append("</script>");
                }else{
                    for (int i = 0; i < list.size(); i++) {

                        String[] array = list.get(i).toString().split("=");
                        map.put(array[0], array[1]);
                    }


                    if (db.readcount("clientuser", "username", map.get("username").toString()) == 1) {
                        sb.append("<script>");
                        sb.append("alert(\"用户名已存在\");");
                        sb.append("window.location=\"/signup.html\";");
                        sb.append("</script>");
                    } else {
                        db.write(map.get("username").toString(), map.get("password").toString());
//                System.out.println(map.toString());
                        sb.append("<script>");
                        sb.append("window.location=\"/welcomepage.html\"");
                        sb.append("</script>");
                    }
                }



                ByteBuf buf = copiedBuffer(sb.toString(), CharsetUtil.UTF_8);
                HttpResponse response = new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.OK);
                response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");
                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, buf.readableBytes());
                ctx.channel().write(response);
                ctx.channel().writeAndFlush(buf).addListener(ChannelFutureListener.CLOSE);

                flag = false;
                return;

            }

            if (uri.getPath().equals("/login")) {
                HttpDataFactory factory = new DefaultHttpDataFactory(false);
                HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(factory, request);
                List list = decoder.getBodyHttpDatas();
//                System.out.println(request+"@@@@wwwwwwwwwwwwwwwwwwwwww");
                if (list.get(0).toString().equals("username=")) {
                    System.out.println("内容为空" + list.get(0).toString());

                    flag = false;
                    ctx.channel().writeAndFlush("").addListener(ChannelFutureListener.CLOSE);
                    return;
                }
                if (list.get(1).toString().equals("pw=")) {
                    System.out.println("内容为空" + list.get(1).toString());
                    flag = false;
                    ctx.channel().writeAndFlush("").addListener(ChannelFutureListener.CLOSE);
                    return;
                }
                System.err.println(list.toString());
                String[] array = list.get(0).toString().split("=");
                String username = array[1];
                array = list.get(1).toString().split("=");
                String password = array[1];


//array 0 username 1 unct 2 password 3pwct

                StringBuilder sb = new StringBuilder();
                User user = new User();
                HttpResponse response = new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.OK);
                if (db.readcount("clientuser", "username", username) != 1) {
//                    sb.append("<script>");
//                    sb.append("alert(\"Username is unknown \")");
//                    sb.append("</script>");
//                    sb.append("<script>");
//                    sb.append("window.location=\"/login.html\";");
//                    sb.append("</script>");
                    sb.append("用户名不存在");
                    flag = false;
                } else {
                    if (db.read(username).equals(password)) {
                        //                保存登陆用户的用户名
//                    clientusername=username;

                        Set<Cookie> cookies;
                        String value = request.headers().get(HttpHeaders.Names.COOKIE);

//                    System.out.println(value+"VVVVVVVVVVVVVVVVVVVVV");

                        if (value == null) {
                            cookies = Collections.emptySet();
                        } else {
                            cookies = CookieDecoder.decode(value);
                        }


                        for (Cookie str : cookies) {
//                        System.out.println(str+"Cooooooooooooooooooooooooooooooooookies");
                        }

                        BASE64Encoder encoder = new BASE64Encoder();
                        String strPw = encoder.encode(TripleDes.encryptMode(db.read(username).getBytes()));
//                    System.err.println(strPw+"输出到cookie之前");

//含中文cookie需要编码
                        try {
                            response.headers().add(HttpHeaders.Names.SET_COOKIE, "realname=" + URLEncoder.encode(username, "utf-8") + ";");
//                        System.out.println("@@@@@@已输出"+URLEncoder.encode(username,"GBK"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        response.headers().add(HttpHeaders.Names.SET_COOKIE, "realpw=" + strPw + ";");

                        sb.append("index");
//                    sb.append("<script>");
//                    sb.append("window.location=\"/index.html\";");
//                    sb.append("alert(\"index\")");
//                    sb.append("</script>");

                        flag = false;
                    } else {
//                    sb.append("<script>");
//                    sb.append("alert(\"Invalid password\")");
//                    sb.append("</script>");
//                    sb.append("<script>");
//                    sb.append("window.location=\"/login.html\";");
//                    sb.append("</script>");
                        sb.append("密码不正确");
                        flag = false;
                    }
                }


                ByteBuf buf = copiedBuffer(sb.toString(), CharsetUtil.UTF_8);
                response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");
//                    response.headers().set(HttpHeaders.Names.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, buf.readableBytes());
//                System.out.println(response+"@@@@@MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
//                System.out.print(buf.toString());
                ctx.channel().write(response);
                ctx.channel().writeAndFlush(buf).addListener(ChannelFutureListener.CLOSE);
            }


//            TODO
            if (method.equals("/checksign")) {
                HttpDataFactory factory = new DefaultHttpDataFactory(false);
                HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(factory, request);
                List list = decoder.getBodyHttpDatas();

                System.err.println(list + "##############");
                if (list.get(0).toString().equals("name=") || list.get(1).toString().equals("password=")) {

                } else {
                    String[] array = list.get(0).toString().split("=");
                    String username = array[1];
// 因为密码经过加密处理 可能出现等号 所以需要单独读取
                    String password = new String();
                    String a = list.get(1).toString();
//                    System.out.println(a);
                    while (a.charAt(0) == ' ') {
                        a = a.substring(1, a.length());
                    }
                    if (a.indexOf("password=") == 0) {
                        password = a.substring("password=".length(), a.length());
                    }
//                    System.out.println(password+"读取解码前的password");

//                    解码password
                    BASE64Decoder base64Decoder = new BASE64Decoder();
                    String realpassword = null;
//                    System.out.println(username+password+"CHECKSIGN");
                    try {
//                        base64Decoder解码后要是8的倍数
//                        System.out.println("****************"+base64Decoder.decodeBuffer(password));
                        if (base64Decoder.decodeBuffer(password).length % 8 != 0) {
                        } else {
                            realpassword = new String(TripleDes.decryptMode(base64Decoder.decodeBuffer(password)));

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (realpassword != null) {
                        if (db.read(username).equals(realpassword)) {
//                            System.out.println("又进来了");
                            ctx.channel().writeAndFlush("").addListener(ChannelFutureListener.CLOSE);
                            return;
                        }
                    }
                }

                StringBuilder sb = new StringBuilder();
//                sb.append("<script>");
                sb.append("login.html");
//                sb.append("</script>");
                ByteBuf buf = copiedBuffer(sb.toString(), CharsetUtil.UTF_8);

                HttpResponse response = new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.OK);
                response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");
                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, buf.readableBytes());
                ctx.channel().write(response);
                ctx.channel().writeAndFlush(buf);
                ctx.channel().flush();

            }

        }

//        get请求
        if (uri.getPath().equals("/Atest")) {
            System.err.println("In Atest");
            StringBuilder sb = new StringBuilder();
//            System.err.println(count+"~~~~~count");x
            for (int i = 0; i < count; i++) {
                MessageBoard board = db.showInMes(i + 1);
                sb.append("<div class=\"panel panel-default\">");
                sb.append("<div class=\"panel-body\"");
                sb.append("<p style=\"word-break:break-all;\">");
//                sb.append(db.read(i+1));
                sb.append(board.getContent());
                sb.append("</p>");
                sb.append("</div>");
                sb.append("<div class=\"panel-footer\">");
                sb.append("<p style=\"word-wrap:break-word;font-size: small;\" class=\"text-right\">");
//                sb.append(db.readusername(i+1));
                sb.append(board.getUsername());
                sb.append("<br/>");
                sb.append("来自&nbsp" + board.getLocation());
                sb.append("</p>");
                sb.append("</div>");
                sb.append("</div>");
            }


            ByteBuf buf = copiedBuffer(sb.toString(), CharsetUtil.UTF_8);
            HttpResponse response = new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.OK);
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, buf.readableBytes());
//            System.out.println(response.headers().toString() + "~~~~~~~~~~~~");

            ctx.channel().write(response);
            System.err.println("Before Atest flush");
            ctx.channel().writeAndFlush(buf).addListener(ChannelFutureListener.CLOSE);
//            ctx.channel().closeFuture();

            flag = false;
        }

        if (uri.getPath().equals("/Atest2")) {
            System.err.println("In Atest2");
            HashMap report = new HashMap();


            QueryStringDecoder decoder = new QueryStringDecoder(request.getUri());
            Map<String, List<String>> map = decoder.parameters();
            System.out.println(map.get("count").get(0) + "~!!~!~!~!~!~!~!~!~!~");
            System.err.println("~~~~~~~~~~~~" + decoder.parameters());
            Integer num = Integer.parseInt(map.get("count").get(0).toString());
            StringBuilder sb = new StringBuilder();

            if (num < count) {
//            System.err.println(count+"~~~~~count");
                ArrayList list = new ArrayList();
                if (num + 6 < count) {
                    for (int i = num; i < num + 6; i++) {
//                        MessageBoard mes=new MessageBoard(db.readusername(i+1),db.read(i + 1));
                        MessageBoard mes = db.showInMes(i + 1);
                        list.add(mes);
//                    System.err.println(mes);
//                    System.err.println(JSONObject.toJSONString(mes));
                    }
                } else {
                    for (int i = num; i < count; i++) {
//                        MessageBoard mes=new MessageBoard(db.readusername(i+1),db.read(i + 1));
                        MessageBoard mes = db.showInMes(i + 1);
                        list.add(mes);
                        System.err.println(mes);
//                    System.err.println(JSONObject.toJSONString(mes));
                    }
                }

                sb.append(JSON.toJSONString(list));
            }

            ByteBuf buf = copiedBuffer(sb.toString(), CharsetUtil.UTF_8);
            HttpResponse response = new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.OK);
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, buf.readableBytes());
//            System.out.println(response.headers().toString() + "~~~~~~~~~~~~");

            ctx.channel().write(response);
            ctx.channel().writeAndFlush(buf).addListener(ChannelFutureListener.CLOSE);

            flag = false;
        }


        if (uri.getPath().startsWith("/accesstoken")) {
            System.err.println("SEE FATHER REQUEST"+request.getUri());

//            豆瓣用户拒绝后页面跳转
            if(request.getUri().contains("error=access_denied")){
                HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);

                StringBuilder responseSb=new StringBuilder();
//                    responseSb.append("window.location=\"http://messageboard-fg1.herokuapp.com/index.html\";");
                responseSb.append("<script>");
                responseSb.append("window.location=\"http://holyfire.me/login.html\";");
                responseSb.append("</script>");

                ByteBuf responseContent = copiedBuffer(responseSb.toString(), CharsetUtil.UTF_8);
                response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");
                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, responseContent.readableBytes());

//                            StringBuilder sb = new StringBuilder();

                ctx.channel().write(response);
//                            new Routes(serverCtx,"/index.html");
                ctx.channel().writeAndFlush(responseContent).addListener(ChannelFutureListener.CLOSE);
            }

//            QueryStringDecoder decoder=new QueryStringDecoder(request.getUri());
////                    Map map=decoder.parameters();
////                    System.out.println(map.get("chatText") + "~!!~!~!~!~!~!~!~!~!~");
            String userFrom=uri.getPath().substring("/accesstoken_".length(),uri.getPath().length());
            System.err.println("userFrom="+userFrom);

            GuestUser user=null;
            if(userFrom.equals("douban")){
               user=new DouBanUser();
            }
            if(userFrom.equals("QQ")){
               user=new QQUser();
            }


            if(user==null){
                System.err.println("未知网站的授权");
                ctx.channel().writeAndFlush("").addListener(ChannelFutureListener.CLOSE);
                return;
            }
            String code;
            System.err.println("See the Request"+request);
            QueryStringDecoder decoder=new QueryStringDecoder(request.getUri());
            Map<String,List<String>> map=decoder.parameters();
            System.err.println(map);
            if(map.get("code")!=null){
                code=map.get("code").get(0);
                System.err.println("response code="+code);
                assert user != null;
                user.setCode(code);
                String host=user.getHost();
                System.err.println("HOST OUT OUT OUT"+host);

                String rawPath=user.getRawPath(code);
//                rawPath /request?....
                int port=443;

//                URI uri1=new URI(rawPath);
//                System.err.println(uri1.getRawPath());

//                new Routes(ctx,"/index.html");
                new MessBoardClient(host,rawPath,port,ctx,user);

//                new Routes(ctx,"/index.html");

            }
            flag = false;

        }

//        Myblog Index
        if(uri.getPath().equals("/blogIndex")||uri.getPath().equals("/blogIndex/1")){


            StringBuilder sb=new StringBuilder();
            int lastId=0;
            try {
                lastId=db.readBlogLastId();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            for (int i = lastId; i > 0; i--) {
                try {

                    MyBlog myBlog=db.readBlogIndex(i);
                    if(myBlog==null){
                        continue;
                    }

                    Date date=myBlog.getCreatetime();
                    System.out.println(date);
                    String year=new SimpleDateFormat("yyyy").format(date);
                    int month=Integer.parseInt(new SimpleDateFormat("mm").format(date))+1;
                    String day=new SimpleDateFormat("dd").format(date);
                    String title=myBlog.getTitle();
                    String category=myBlog.getCategory();
                    String blogContent=myBlog.getBlogText();
                    String tag=myBlog.getTag();
                    int id=myBlog.getId();
//                    String id=myBlog.setId();



                    sb.append(" <div class=\"panel-body excerpt\">");
                    sb.append("<div class=\"datetime\">"+year+"<br>"+month+"-"+day+"</div>\n" +
                            "                        <div class=\"tit\">\n" +
                            "                            <h4 class=\"titTitle\"><a href=\"/blue/"+id+"\" title=\""+title+"\">---"+title+"</a>\n" +
                            "                            </h4>\n" +
                            "                            <p class=\"iititle\"><span class=\"i2\"><a href=\"/aboutme\" title=\"Clocy\" rel=\"author\">clocy</a></span><span class=\"i1\"><a href=\"/category/"
                            +category+"\">"+category+"</a></span></p>\n" +
                            "                        </div>\n" +
                            "                        <div class=\"c-con\">\n" +
                            "                            <p>"+blogContent+"</p>\n" +
                            "                        </div>\n" +
                            "                        <div class=\"c-bot\">\n" +
                            "                            <span class=\"cb_bq\"><a href=\"/tag/"+tag+"\" rel=\"tag\">"+tag+"</a> <a href=\"/blue/"+id+"\" style=\"position: absolute;right: 10%;background: #ccc;\">Read More></a>  </span>\n" +
                            "                        </div>\n" +
                            "                    </div>\n");


                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
            ByteBuf buf = copiedBuffer(sb.toString(), CharsetUtil.UTF_8);
            HttpResponse response = new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.OK);
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, buf.readableBytes());
            ctx.channel().write(response);
            ctx.channel().writeAndFlush(buf).addListener(ChannelFutureListener.CLOSE);



        }
//

//      正文内容请求
        if(uri.getPath().startsWith("/blue/")){
            MyBlog blog;
            int id=-1;
            if(uri.getPath().matches("/blue/\\d+")){
                System.out.println("match");
                Pattern pattern=Pattern.compile("/blue/(\\d+)");
                Matcher m=pattern.matcher(uri.getPath());
                if(m.find()){
//                    System.out.println(m.group(1)+"group(1)");
                    id=Integer.parseInt(m.group(1));
//                    System.out.println("id"+id);

                }
            }
            if(id>0){
                try {
                    blog = db.readBlogIndex(id);

                    HashMap<String,String> map=new HashMap<String, String>();
                    map.put("title",blog.getTitle());
                    map.put("date", Date_Reader.getRealDate(blog.getCreatetime()));
                    map.put("tag",blog.getTag());
                    map.put("content",blog.getBlogText());
                    map.put(blog.getCategory(),"active");
                    String replaceContent=new ModelTrans().keyWordsReplace("/ViewModel/ContentViewModel.html",map);
                    ByteBuf buf=copiedBuffer(replaceContent,CharsetUtil.UTF_8);
                    HttpResponse response=new DefaultHttpResponse(HTTP_1_1,HttpResponseStatus.OK);
                    response.headers().set(HttpHeaders.Names.CONTENT_TYPE,"text/html; charset=UTF-8");
                    response.headers().set(HttpHeaders.Names.CONTENT_LENGTH,buf.readableBytes());
                    ctx.write(response);
                    ctx.writeAndFlush(buf).addListener(ChannelFutureListener.CLOSE);

                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


//        通过category分类 页面

        if(uri.getPath().startsWith("/category/")){

        }

//      通过tag分类 页面
        if(uri.getPath().startsWith("/tag/")){

        }

//      最近5篇文章
        if(uri.getPath().equals("/recent_blog")){
            StringBuilder sb=new StringBuilder();
            MyBlog blog;
            int id= 0;
            try {
                id = db.readBlogLastId();
                int blog_count=0;
                HashMap<String,String> map=new HashMap<String, String>();
                for(int i=id;i>0;i--){
                   blog= db.readBlogIndex(i);
                   if(blog==null){
                       continue;
                   }
                    map.put("title",blog.getTitle());
                    map.put("date", Date_Reader.getRealDate(blog.getCreatetime()));
                    sb.append(new ModelTrans().keyWordsReplace("/ViewModel/recent_blog.html",map));
                    blog_count=blog_count+1;
                    if(blog_count>=5){
                        break;
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteBuf buf=copiedBuffer(sb,CharsetUtil.UTF_8);
            HttpResponse response=new DefaultHttpResponse(HTTP_1_1,HttpResponseStatus.OK);
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE,"text/html; charset=UTF-8");
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH,buf.readableBytes());
            ctx.write(response);
            ctx.writeAndFlush(buf).addListener(ChannelFutureListener.CLOSE);



        }

//      tag池
        if(uri.getPath().equals("tag_pool")){
            StringBuilder sb =new StringBuilder();
            HashSet<String> tagPool= db.readTagPool();
            Iterator iterator=tagPool.iterator();
            while (iterator.hasNext()){

            }

        }




    }








    public boolean needContinue(){
        return flag;
    }
}
