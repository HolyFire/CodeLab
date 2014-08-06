package MessageBoard_OL.NettyMsServer;

import MessageBoard_OL.Config.Routes;
import MessageBoard_OL.DB.DbHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.util.CharsetUtil;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.netty.buffer.Unpooled.copiedBuffer;
import static io.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by DELL on 14-7-28.
 */
public class BoardServerHandler extends SimpleChannelInboundHandler<Object>{
    private HttpRequest request;
    int count=1;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        DbHandler db=DbHandler.getDbHandler();
        db.init();
        count=db.readcount("messcontent");

        if(msg instanceof HttpRequest){
            HttpRequest request=this.request=(HttpRequest)msg;
            URI uri= new URI(request.getUri());
            System.err.println("request uri==" + uri.getPath());

            if(uri.getPath().equals("/Atest")){
                StringBuilder sb=new StringBuilder();



                for(int i=0;i<count;i++){
                sb.append("<div class=\"panel panel-default\">");
                sb.append("<div class=\"panel-body\"");
                sb.append("<p>");
                sb.append("java输出的面板"+db.read(i+1));
                sb.append("</p>");
                sb.append("</div>");
                sb.append("</div>");
                }


                ByteBuf buf=copiedBuffer(sb.toString(), CharsetUtil.UTF_8);
                HttpResponse response=new DefaultHttpResponse(HTTP_1_1,HttpResponseStatus.OK);
                response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");
                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, buf.readableBytes());
                System.out.println(response.headers().toString()+"~~~~~~~~~~~~");
                ctx.channel().write(response);
                ctx.channel().writeAndFlush(buf);

                return;
            }

//            处理POST请求
            if(request.getMethod().equals(HttpMethod.POST)){
                if(uri.getPath().equals("/index.html")){
                    System.err.println("~~~~~~~~~~~~~~~~!!~~~~~~~~~~~~~~");

                    HttpDataFactory factory=new DefaultHttpDataFactory(false);
                    HttpPostRequestDecoder decoder=new HttpPostRequestDecoder(factory,request);
                    List list=decoder.getBodyHttpDatas();
                    if(list.get(0).toString().equals("chatText=")){
                        System.out.println("内容为空"+list.get(0).toString());
                        return;
                    }

//                  分割等号前后数据 "a=b"
                    String[] array=new String[list.size()];
                    array=list.get(0).toString().split("=");
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
            }


            if(uri.getPath()!=null){
                new Routes(ctx,request);
                return;

            }

            if (is100ContinueExpected(request)) {
                send100Continue(ctx);
            }

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
