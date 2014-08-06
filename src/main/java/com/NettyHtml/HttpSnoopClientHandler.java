package com.NettyHtml;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * Created by DELL on 14-7-18.
 */
public class HttpSnoopClientHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        if(httpObject instanceof HttpResponse){
            HttpResponse response = (HttpResponse) httpObject;
            System.err.println("STATUS: " + response.getStatus());
            System.err.println("VERSION: " + response.getProtocolVersion());
            System.err.println();
            if (!response.headers().isEmpty()) {
                for (String name: response.headers().names()) {
                    for (String value: response.headers().getAll(name)) {
                        System.err.println("HEADER: " + name + " = " + value);
                    }
                }
                System.err.println();
            }

            if (HttpHeaders.isTransferEncodingChunked(response)) {
                System.err.println("CHUNKED CONTENT {");
            }else{
                System.err.println("CONTENT {");
            }

        }
        if (httpObject instanceof HttpContent) {
            HttpContent content = (HttpContent) httpObject;
            System.err.print(content.content().toString(CharsetUtil.UTF_8));
            System.err.flush();
            if(content instanceof LastHttpContent) {
                System.err.println("} END OF CONTENT");
                channelHandlerContext.close();
            }
        }
    }

@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
