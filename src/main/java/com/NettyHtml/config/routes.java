package com.NettyHtml.config;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Set;

import static io.netty.buffer.Unpooled.copiedBuffer;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaders.Names.TRANSFER_ENCODING;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.setContentLength;
import static io.netty.handler.codec.http.HttpHeaders.setDate;

/**
 * Created by DELL on 14-7-26.
 */
public class routes {

    boolean keepAlive;
//FullHttpResponse response = null;


    String uri=null;
    RandomAccessFile raf = null;




    public routes(ChannelHandlerContext ctx,HttpRequest request){

        keepAlive = isKeepAlive(request);
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK);


        uri=request.getUri();
        System.err.println(uri);

        final String path = Config.getRealPath(uri);
        keepAlive = isKeepAlive(request);
        File localFile = new File(path);
        System.err.println(localFile);
        // 如果文件隐藏或者不存在
        if (localFile.isHidden() || !localFile.exists()) {
            // 逻辑处理
            System.out.println("文件隐藏或者不存在");
            return;
        }
        // 如果请求路径为目录
        if (localFile.isDirectory()) {
            // 逻辑处理
            System.out.println("是目录");
            return;
        }


        try {
            System.out.println("~~~~~~~~~~~~~~~~");
            raf = new RandomAccessFile(localFile, "r");
            long fileLength = raf.length();
            System.err.println(String.valueOf(fileLength));



            // 这里又要重新温习下http的方法，head方法与get方法类似，但是服务器在响应中只返回首部，不会返回实体的主体部分
            System.out.println("进入没？");
            if (!request.getMethod().equals(HttpMethod.HEAD)) {
                System.out.println("进入了！");

//                System.err.println(raf.readLine());
//                byte[] b=new byte[8192];
//                raf.seek(c);
//                int rdcount = raf.read(b,0,b.length);
//                ByteBuf buf =
//                        copiedBuffer(raf.toString(),CharsetUtil.UTF_8);


//                response = new DefaultFullHttpResponse(
//                        HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);

//                response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
//                response.headers().set(CONTENT_LENGTH, buf.readableBytes());
                // Write the response.
//                ch.writeAndFlush(response);


                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");


                response.headers().set(CONTENT_LENGTH, String.valueOf(fileLength));;
                response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
//                response.headers().set(TRANSFER_ENCODING,"CHUNKED");
                ctx.channel().write(response);

//                ChunkedFile ch = new ChunkedFile(raf, 0, fileLength, 8192);
//                ByteBuf buf=ch.readChunk(ctx);
//                System.out.println(ch.toString()+"~~~~~~~~~~~~");
//                ctx.channel().write(buf);


//TODO
                byte[] b=new byte[8192];
                raf.seek(0);
                int rdcount = raf.read(b,0,b.length);
                ByteBuf buf=copiedBuffer(b);
//                FullHttpResponse rs = new DefaultFullHttpResponse(
//                        HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
                ctx.channel().writeAndFlush(buf);

//                ctx.channel().writeAndFlush(new HttpChunkedInput(new ChunkedFile(raf, 0, fileLength, 8192)));
//                ctx.channel().write(new ChunkedFile(raf, 0, fileLength, 8192));//8kb
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        finally {
            if (keepAlive) {
//                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH,response.headers());
//                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH,response.getDecoderResult());
                response.headers().set(CONTENT_LENGTH,response.getDecoderResult());
//                response.headers().set(CONTENT_LENGTH,response.getContent().readableBytes());
            }
            if (!keepAlive) {
                ctx.channel().closeFuture();
//                e.getFuture().addListener(ChannelFutureListener.CLOSE);
            }
        }
//            0----------------------------------------------------0
//            return;
//        }
    }


}
