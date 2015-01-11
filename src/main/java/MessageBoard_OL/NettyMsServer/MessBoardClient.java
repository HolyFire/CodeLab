package MessageBoard_OL.NettyMsServer;

import MessageBoard_OL.App.DouBanUser;
import MessageBoard_OL.App.GuestUser;
import MessageBoard_OL.App.QQUser;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.util.CharsetUtil;

import java.io.*;
import java.net.URLEncoder;
import java.util.zip.GZIPInputStream;

import static io.netty.buffer.Unpooled.copiedBuffer;

/**
 * Created by DELL on 2014-10-31.
 */
public class MessBoardClient {

    ChannelHandlerContext serverCtx;
    GuestUser user;


    public MessBoardClient(String host, String uri, int port, ChannelHandlerContext serverCtx, GuestUser user){
        this.serverCtx=serverCtx;
        this.user=user;


        try {
            EventLoopGroup group=new NioEventLoopGroup();
            Bootstrap b=new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).handler(new ClientInitializer());
            Channel ch=b.connect(host,port).sync().channel();
            System.err.println("HOST HOST HOST HOST HOST HOST"+host);

            if(host.equals(new QQUser().getHost())){
                HttpRequest request=new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1, HttpMethod.GET,uri
            );

            request.headers().set("host", host);
            request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
//            request.headers().set(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP);
                System.out.println(request);
                ch.writeAndFlush(request);

            }



            if(host.equals("api.douban.com")){
                System.err.println("In API.DOUBAN.COM");
//                System.err.println(user.getAccess_Token()+user.getClient_Id());
                HttpRequest request=new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1, HttpMethod.GET,uri
            );
                request.headers().set("host", host);
                request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
//                String [] s=new String[2];
//                s[0]=user.getClient_Id();
//                s[2]=user.getAccess_Token();
//                request.headers().set("Authorization",user.getClient_Id());
//                request.headers().add("Authorization:",user.getAccess_Token());
//                request.headers().set("Bearer",user.getAccess_Token());
                request.headers().set("Authorization","Bearer "+user.getAccess_Token());
//                System.err.println("SEE　Authorization"+user.getClient_Id()+" "+user.getAccess_Token());
                System.out.println(request);

                ch.writeAndFlush(request);

            }

            if(host.equals(new DouBanUser().getHost())){
                String params=uri;
                ByteBuf buf=copiedBuffer(params,CharsetUtil.UTF_8);

                HttpRequest request=new DefaultHttpRequest(HttpVersion.HTTP_1_1,HttpMethod.POST,"/service/auth2/token");
//            request.headers().set(HttpHeaders.Names.CONTENT_TYPE, "application/json");

                request.headers().add(HttpHeaders.Names.CONTENT_TYPE,"application/x-www-form-urlencoded");
                request.headers().set("host", host);
                request.headers().set(HttpHeaders.Names.CONTENT_LENGTH,buf.readableBytes());
                System.out.println(request);
                ch.write(request);
                ch.writeAndFlush(buf);
//                ch.writeAndFlush(buf).addListener(ChannelFutureListener.CLOSE);
            }

//            request.headers().set(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP);





//            ChannelBuffer cb=ChannelBuffers.copiedBuffer(params,Charset.defaultCharset());
//            httpReq.setHeader(HttpHeaders.Names.CONTENT_LENGTH,cb.readableBytesH());
//            httpReq.setContent(cb);


//            ch.closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    class ClientInitializer extends ChannelInitializer<SocketChannel>{

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            ChannelPipeline p=socketChannel.pipeline();
            SslContext sslCtx= SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE);
//        Enable HTTPS if nessary.
            if(sslCtx!=null){
                p.addLast(sslCtx.newHandler(socketChannel.alloc()));
            }
            p.addLast(new HttpClientCodec());

//        Remove the following line if you don't want automatic content decompression.
//            p.addLast(new HttpContentDecompressor());

            // Uncomment the following line if you don't want to handle HttpContents.
            p.addLast(new HttpObjectAggregator(1048576));

            p.addLast(new GetResponseHandler());
        }
    }

    private class GetResponseHandler extends SimpleChannelInboundHandler {
        private HttpRequest request;
        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

//            System.err.println("o.getName@@@@@@@@@@@@@@\t"+o.getClass().getName());
            System.err.println("oooooooo"+"\n"+o);

//            先把数据读出来
            String contentStr=null;
                if(o instanceof DefaultFullHttpResponse) {
                    DefaultFullHttpResponse defaultFullHttpResponse = (DefaultFullHttpResponse) o;
                    if (defaultFullHttpResponse.headers().get("Content-Encoding") != null) {
                        StringBuilder sb = new StringBuilder();
                        byte[] b = new byte[defaultFullHttpResponse.content().capacity()];
                        ByteBuf buf = defaultFullHttpResponse.content();
                        defaultFullHttpResponse.content().readBytes(b);

                        GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(b));
                        byte[] b2 = new byte[8192];
                        while (gzipInputStream.read(b2) != -1) {
                            sb.append(new String(b2, "utf-8"));
                        }
                        gzipInputStream.close();
//                        System.err.println(sb.toString());
                        contentStr = sb.toString();
                    } else {
                        contentStr = defaultFullHttpResponse.content().toString(CharsetUtil.UTF_8);
                    }


//                    System.err.println(o);
                    System.err.println("CONTENTstr" + contentStr);
//            判断用户授权来自什么哪里
                    if (contentStr != null && contentStr.indexOf("access_token") != -1) {
                        String access_token = null;
                        String user_name = null;
                        if (user instanceof QQUser) {
                            access_token = contentStr.substring(contentStr.indexOf("access_token=") + "access_token=".length(), contentStr.indexOf("&expires_in"));
                            user.setAccess_Token(access_token);
                            new MessBoardClient("graph.qq.com", "/oauth2.0/me?access_token=" + access_token, 443, serverCtx, user);

                        }
                        if (user instanceof DouBanUser) {
                            System.err.println(contentStr);
                            JSONObject jsonObject = JSONObject.parseObject(contentStr.trim());
                            access_token = jsonObject.getString("access_token");
                            user_name = jsonObject.getString("douban_user_name");
                            user.setAccess_Token(access_token);
                            user.setClient_Id(user_name);
                            System.err.println("DDDDDDDDDDDDDDDDDDDDDDDDDD" + access_token + "\t" + user_name);
                            System.err.println("BBBBBBBBBBBBBBBBBBBBBBBBBB"+user.getAccess_Token()+"\t"+user.getClient_Id());
                            new MessBoardClient("api.douban.com", "/v2/user/~me", 443, serverCtx, user);
                        }


//                new SendRequest("graph.qq.com","/oauth2.0/me?access_token="+access_token,443,serverCtx,user);
                        if (defaultFullHttpResponse.content() instanceof LastHttpContent) {
//                    System.err.println("} END OF CONTENT");
                            channelHandlerContext.close();
                        }
                        return;
                    }

                    if(contentStr.indexOf("callback")!=-1){
                        contentStr=contentStr.trim();
//                        System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~"+contentStr);
                        String jsonStr=contentStr.substring("callback(".length(), contentStr.length()-2);
                        System.err.println(jsonStr+"~~~~~~~~~~~~~~~~~~~~~~~");

//         类似这种内容           callback( {"client_id":"101165433","openid":"7DB7F5567CF9F79AF65537620B4C5020"} );

//                        System.err.println(jsonStr);
                        JSONObject js = JSON.parseObject(jsonStr);

                        if(js.getString("client_id")!=null&&js.getString("openid")!=null){
                            user.setClient_Id(js.getString("client_id"));
                            user.setOpenId(js.getString("openid"));

                            System.err.println("graph.qq.com/user/get_user_info?access_token="+user.getAccess_Token()+"&oauth_consumer_key="+user.getClient_Id()+"&openid="+user.getOpenId());
//                           TODO 第二个请求
                            new MessBoardClient("graph.qq.com","/user/get_user_info?access_token="+user.getAccess_Token()+"&oauth_consumer_key="+user.getClient_Id()+"&openid="+user.getOpenId(),443,serverCtx,user);

                        }
//                        System.err.println(js.getString("client_id"));
                        if (defaultFullHttpResponse.content() instanceof LastHttpContent) {
//                    System.err.println("} END OF CONTENT");
                            channelHandlerContext.close();
                        }
                        return;
                    }

                    System.err.println(contentStr+"contentStr~~~~~~~~~~~~~~");
                    if (user.getClient_Id()!= null) {
                        System.err.println(contentStr+"contentStr~~~~~~~~~~~~~~");
                        JSONObject userInfo = JSONObject.parseObject(contentStr);
                        String nickname = null;
                        if (user instanceof QQUser) {
                            nickname = userInfo.getString("nickname");
//                            System.err.println(nickname);
                        }
                        if (user instanceof DouBanUser) {
                            nickname = userInfo.getString("name");
                        }

                        System.err.println("NICKNAME="+nickname);

                        if (nickname != null) {

                            System.err.println("NICKNAME="+nickname);
                            HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                            response.headers().add(HttpHeaders.Names.SET_COOKIE, "guestUser=" + URLEncoder.encode(nickname, "utf-8") + ";");

                            StringBuilder responseSb=new StringBuilder();
//                    responseSb.append("window.location=\"http://messageboard-fg1.herokuapp.com/index.html\";");
                            responseSb.append("<script>");
                            responseSb.append("window.location=\"http://holyfire.me/index.html\";");
                            responseSb.append("</script>");

                            ByteBuf responseContent = copiedBuffer(responseSb.toString(), CharsetUtil.UTF_8);
                            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");
                    response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, responseContent.readableBytes());

//                            StringBuilder sb = new StringBuilder();

                            serverCtx.channel().write(response);
//                            new Routes(serverCtx,"/index.html");
                    serverCtx.channel().writeAndFlush(responseContent).addListener(ChannelFutureListener.CLOSE);
                        }


                    }
                    if (defaultFullHttpResponse.content() instanceof LastHttpContent) {
//                    System.err.println("} END OF CONTENT");
                        channelHandlerContext.close();
                    }
                }


//            if(o instanceof DefaultFullHttpResponse){
//                DefaultFullHttpResponse defaultFullHttpResponse = (DefaultFullHttpResponse) o;
//                System.err.println(defaultFullHttpResponse);
//                if(defaultFullHttpResponse.headers().get("Content-Encoding")!=null){
//
////                String s1 = new String(defaultFullHttpResponse.content().array().toSt,"UTF-8");
//                    byte[] b = new byte[defaultFullHttpResponse.content().capacity()];
////                ZlibDecoder zlibDecoder=new JdkZlibDecoder(ZlibWrapper.GZIP);
////                zlibDecoder.handlerAdded(channelHandlerContext);
////                System.err.println(defaultFullHttpResponse.content().capacity()+"\tlenth");
////                System.err.println(b.length);
////                FileOutputStream fileOutputStream=new FileOutputStream("F:\\新建文件夹\\a.txt");
////                OutputStreamWriter outputStreamWriter=new OutputStreamWriter()
//
//                    ByteBuf buf = defaultFullHttpResponse.content();
//                    defaultFullHttpResponse.content().readBytes(b);
//
//                    GZIPInputStream gzipInputStream=new GZIPInputStream(new ByteArrayInputStream(b));
//                    byte[] b2=new byte[8192];
//                    StringBuilder sb=new StringBuilder();
//                    while(gzipInputStream.read(b2)!=-1){
//                        sb.append(new String(b2,"utf-8"));
//                    }
//                    gzipInputStream.close();
//                    System.err.println(sb.toString());
//                    JSONObject object=JSONObject.parseObject(sb.toString().trim());
//                    String nickname=object.getString("nickname");
//                    System.err.println(nickname);
//
//                    HttpResponse response=new DefaultHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK);
//                    response.headers().add(HttpHeaders.Names.SET_COOKIE, "guestUser=" + URLEncoder.encode(nickname,"utf-8") + ";");
////                    StringBuilder responseSb=new StringBuilder();
////                    responseSb.append("window.location=\"http://messageboard-fg1.herokuapp.com/index.html\";");
////                    ByteBuf responseContent = copiedBuffer(responseSb.toString(), CharsetUtil.UTF_8);
//                    response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");
////                    response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, buf.readableBytes());
//
//                    serverCtx.channel().writeAndFlush(response);
//                    new Routes(serverCtx,"/index.html");
////                    serverCtx.channel().writeAndFlush(responseContent);
//                    serverCtx.channel().closeFuture();
//
//
//
////                System.out.println("c1:" +uncompress( buf.toString()) );
////                System.out.println("c2:" +buf.toString(CharsetUtil.US_ASCII) );
////                System.out.println("c3:" +buf.toString(CharsetUtil.ISO_8859_1) );
//
//
////                defaultFullHttpResponse.content().readBytes(b);
////                String s2 = new String(b,"utf-8");
////                String s3 = defaultFullHttpResponse.content().toString(CharsetUtil.UTF_8);
//////                System.out.println("s1:"+s1);
////                System.out.println("s2:"+s2);
////                System.out.println("s3:"+s3);
//                }else{
//                    contentStr=defaultFullHttpResponse.content().toString(CharsetUtil.UTF_8);
//                    System.out.println(contentStr);
//
//
//                    if(contentStr.indexOf("access_token")!=-1) {
//                        String access_token = "null";
////                        if (contentStr.indexOf("access_token=") == 0) {
//                            access_token = contentStr.substring(contentStr.indexOf("access_token=")+"access_token=".length(), contentStr.indexOf("&expires_in"));
//                            user.setAccess_Token(access_token);
////                        }
////                    System.err.println("DDDDDDDDDDDDDDDDDDDDDDDDDD" + access_token);
//
//                        new MessBoardClient("graph.qq.com","/oauth2.0/me?access_token="+access_token,443,serverCtx,user);
//
////                    return;
//                    }





//            }

//            if (o instanceof HttpContent) {
//                    HttpContent content = (HttpContent) o;
//                    System.err.println(content.toString()+"a1");
//                    System.err.println(content.content().toString());
//                    System.out.println(content.content().toString(CharsetUtil.UTF_8)+"ffff");
//                    contentStr=content.content().toString(CharsetUtil.UTF_8);
////                byte[] b=new byte[content.content().readableBytes()];
////                content.content().readBytes(b);
////                for(int i=0;i<b.length;i++){
////                    System.out.println(b[i]);
////                }
////                String str1 = new String(b, Charset.defaultCharset());
////                String str2 = new String(b, "UTF-8");
////                String str3 = new String(b, "GBK");
////                System.err.println("~@^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
////                System.err.println(str1);
////                System.err.println(str2);
////                System.err.println(str3);
////                System.err.println("VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV$$~~#");
////                System.err.println("readBytes\t");
////                System.err.println("SEE WHAT TO SAY"+contentStr);
//            }
        }
    }

//    public static String uncompress(String str) throws IOException {
//        if (str == null || str.length() == 0) {
//            return str;
//        }
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        ByteArrayInputStream in = new ByteArrayInputStream(str
//                .getBytes("ISO-8859-1"));
//        GZIPInputStream gunzip = new GZIPInputStream(in);
//        byte[] buffer = new byte[8192];
//        int n;
//        while ((n = gunzip.read(buffer))>= 0) {
//            out.write(buffer, 0, n);
//        }
//        // toString()使用平台默认编码，也可以显式的指定如toString(&quot;GBK&quot;)
//        return out.toString();
//    }


//    final class SslContextFactory{
//        private static final String PROTOCOL = "SSL";
//        private  final SSLContext CLIENT_CONTEXT;
//
//        private  String CLIENT_KEY_STORE = System.getProperty("user.dir")+"/Web/sslclientkeys";
//        private  String CLIENT_TRUST_KEY_STORE =System.getProperty("user.dir")+"/Web/sslclienttrust";
//        private  String CLIENT_KEY_STORE_PASSWORD = "a12345678";
//        private  String CLIENT_TRUST_KEY_STORE_PASSWORD = "a12345678";
//         {
//            String algorithm = SystemPropertyUtil.get("ssl.KeyManagerFactory.algorithm");
//            if (algorithm == null) {
//                algorithm = "SunX509";
//            }
//
//            SSLContext serverContext;
//            SSLContext clientContext;
//
//            try {
//                KeyStore ks2 = KeyStore.getInstance("JKS");
//                ks2.load(new FileInputStream(CLIENT_KEY_STORE), CLIENT_KEY_STORE_PASSWORD.toCharArray());
//
//                KeyStore tks2 = KeyStore.getInstance("JKS");
//                tks2.load(new FileInputStream(CLIENT_TRUST_KEY_STORE), CLIENT_TRUST_KEY_STORE_PASSWORD.toCharArray());
//                // Set up key manager factory to use our key store
//                KeyManagerFactory kmf2 = KeyManagerFactory.getInstance(algorithm);
//                TrustManagerFactory tmf2 = TrustManagerFactory.getInstance("SunX509");
//                kmf2.init(ks2, CLIENT_KEY_STORE_PASSWORD.toCharArray());
//                tmf2.init(tks2);
//                clientContext = SSLContext.getInstance(PROTOCOL);
//                clientContext.init(kmf2.getKeyManagers(), tmf2.getTrustManagers(), null);
//            } catch (Exception e) {
//                throw new Error("Failed to initialize the client-side SSLContext", e);
//            }
//
//            CLIENT_CONTEXT = clientContext;
//        }
//
//
//    }
}
