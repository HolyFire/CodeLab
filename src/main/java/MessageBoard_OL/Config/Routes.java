package MessageBoard_OL.Config;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import static io.netty.buffer.Unpooled.copiedBuffer;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;

/**
 * Created by DELL on 14-7-28.
 */
public class Routes {

    boolean keepAlive;
    String uri=null;
    RandomAccessFile raf = null;
    HttpResponse response=new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);

    public Routes(ChannelHandlerContext ctx,HttpRequest request){


        keepAlive=isKeepAlive(request);
        uri=request.getUri();
        String path=Conf.getRealPath(uri);
        File file=new File(path);
        if(file.isHidden()||!file.exists()){
            System.err.println("file is hidden or not exist ");
            return;
        }
        if(file.isDirectory()){
            System.err.println("is Directory");
            return;
        }

        try {

            raf=new RandomAccessFile(file,"r");
            long fileLength=raf.length();
            System.err.println("~~~~~~~~~~~"+String.valueOf(fileLength));

            if (!request.getMethod().equals(HttpMethod.HEAD)){
System.err.println(path);
                response.headers().set(CONTENT_LENGTH, String.valueOf(fileLength));
                response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
//                response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
                ctx.channel().write(response);



                byte[] b=new byte[8192];
                raf.seek(0);
                int readCount=raf.read(b, 0, b.length);

                ByteBuf buf=copiedBuffer(b);
                ctx.channel().writeAndFlush(buf);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }finally{
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

    }
}
