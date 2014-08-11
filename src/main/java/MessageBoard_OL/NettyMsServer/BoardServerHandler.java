package MessageBoard_OL.NettyMsServer;

import MessageBoard_OL.Config.RequestConf;
import MessageBoard_OL.Config.Routes;
import MessageBoard_OL.DB.DbHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

import static io.netty.buffer.Unpooled.copiedBuffer;
import static io.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by DELL on 14-7-28.
 */
public class BoardServerHandler extends SimpleChannelInboundHandler<Object>{
    private HttpRequest request;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        DbHandler db=DbHandler.getDbHandler();
        db.init();


        if(msg instanceof HttpRequest){
            HttpRequest request=this.request=(HttpRequest)msg;
            URI uri= new URI(request.getUri());
            System.err.println("request uri==" + uri.getPath());


            if(uri.getPath().equals("/favicon.ico")){
                System.err.println("/favicon aaaaaaaaaaa a a a a a a aa a ");
                return;
            }

            RequestConf requestConf =new RequestConf(ctx,request,db);


            if(!requestConf.needContinue()){
               return;
            }


            if(request.getUri()!=null){
                new Routes(ctx,request);
                return;
            }





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
        ctx.close();
    }

}
