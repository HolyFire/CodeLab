package MessageBoard_OL.NettyMsServer;

import MessageBoard_OL.Control.RequestConf;
import MessageBoard_OL.Control.Routes;
import MessageBoard_OL.DB.DbHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.*;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.URI;
import java.util.logging.Logger;

import static io.netty.buffer.Unpooled.copiedBuffer;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by DELL on 14-7-28.
 */
public class BoardServerHandler extends SimpleChannelInboundHandler<Object>{
    private HttpRequest request;

    private static final Logger logger = Logger.getLogger(HttpSslContextFactory.class.getName());
    static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
//    @Override
//    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
//        // Once session is secured, send a greeting and register the channel to the global channel
//        // list so the channel received the messages from others.
//        ctx.pipeline().get(SslHandler.class).handshakeFuture().addListener(
//                new GenericFutureListener<Future<Channel>>() {
//                    @Override
//                    public void operationComplete(Future<Channel> future) throws Exception {
//                        ctx.writeAndFlush(
//                                "Welcome to " + InetAddress.getLocalHost().getHostName() +
//                                        " secure chat service!\n");
//                        ctx.writeAndFlush(
//                                "Your session is protected by " +
//                                        ctx.pipeline().get(SslHandler.class).engine().getSession().getCipherSuite() +
//                                        " cipher suite.\n");
//
//                        channels.add(ctx.channel());
//                    }
//                });
//    }



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        DbHandler db=DbHandler.getDbHandler();
//        db.init();

//System.err.println("weizhuanhuade rquest()()()()"+msg);
//        System.err.println("@@@@@@@@@@@@@@@@\n"+msg+"##################\n");

        if(msg instanceof HttpRequest){
            HttpRequest request=this.request=(HttpRequest)msg;
            URI uri= new URI(request.getUri());
            System.err.println("request uri==" + uri.toString());

//            if(uri.getPath().equals("/")){
//                HttpResponse response=new DefaultHttpResponse(HTTP_1_1,HttpResponseStatus.OK);
//                String sb="welcome to CyHeroku";
//                response.headers().set(CONTENT_LENGTH,sb.length());
//                response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
//                ByteBuf buf=copiedBuffer(sb,CharsetUtil.UTF_8);
//                ctx.channel().write(response);
//                ctx.channel().writeAndFlush(buf);
//            }

//            if(uri.getPath().equals("/favicon.ico")){
//                new Routes(ctx,request);
//                System.err.println("/favicon aaaaaaaaaaa a a a a a a aa a ");
//                ctx.channel().closeFuture();
//                return;
//            }

            RequestConf requestConf =new RequestConf(ctx,request,db);


            if(!requestConf.needContinue()){
                System.out.println("返回");
                ctx.channel().closeFuture();
                return;
            }


            if(request.getUri()!=null){
//                System.out.println("进入到导航");
                new Routes(ctx,request);
//                Thread.sleep(15000);
//                ctx.channel().closeFuture();

//                TODO
                return;
            }

//            ctx.channel().closeFuture();
//
//


////            处理POST请求
//            if(request.getMethod().equals(HttpMethod.POST)){
//                if(uri.getPath().equals("/index.html")){
//                    System.err.println("~~~~~~~~~~~~~~~~!!~~~~~~~~~~~~~~");
//
//                    HttpDataFactory factory=new DefaultHttpDataFactory(false);
//                    HttpPostRequestDecoder decoder=new HttpPostRequestDecoder(factory,request);
//                    List list=decoder.getBodyHttpDatas();
//                    if(list.get(0).toString().equals("chatText=")){
//                        System.out.println("内容为空"+list.get(0).toString());
//                        return;
//                    }
//
////                  分割等号前后数据 "a=b"
////只有 name=content 所以array[1] 就是content
//                    String[] array=list.get(0).toString().split("=");
//                    String content=array[1];
//                    count++;
//                    db.write(count,content);
//
////                    for(String a:array){
////                        System.out.println("@@@@@@"+a);
////                    }
//                    System.err.println( list.get(0));
//                    System.err.println(decoder.getBodyHttpDatas());
//
////                    ~~GET~~
////                    QueryStringDecoder decoder=new QueryStringDecoder(request.getUri());
////                    Map map=decoder.parameters();
////                    System.out.println(map.get("chatText") + "~!!~!~!~!~!~!~!~!~!~");
////                    System.err.println("~~~~~~~~~~~~"+decoder.parameters());
//
//                }
//            }


//
//            if (is100ContinueExpected(request)) {
//                send100Continue(ctx);
//            }

        }
    }






    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, CONTINUE);
        System.out.println("~~~~~~~~~~~~~send100Continue~~~~~~~~~~~~~~~~");
        ctx.write(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.channel().closeFuture();
    }

}
