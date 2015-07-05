package com.NettyServer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.nio.charset.Charset;

/**
 * Created by DELL on 14-7-19.
 */
public class HttpServerHandler extends SimpleChannelInboundHandler{
    @Override
    protected void channelRead0(final ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        WebSocketFrame frame = (WebSocketFrame)o;
        ByteBuf buf = frame.content();  //真正的数据是放在buf里面的


        String aa = buf.toString(Charset.forName("utf-8"));  //将数据按照utf-8的方式转化为字符串
        System.out.println(aa);
        WebSocketFrame out = new TextWebSocketFrame(aa);  //创建一个websocket帧，将其发送给客户端
        channelHandlerContext.pipeline().writeAndFlush(out).addListener(new ChannelFutureListener(){
            @Override
            public void operationComplete(ChannelFuture future)
                    throws Exception {
                // TODO Auto-generated method stub
                channelHandlerContext.pipeline().close();  //从pipeline上面关闭的时候，会关闭底层的chanel，而且会从eventloop上面取消注册
            }
        });



    }
}
