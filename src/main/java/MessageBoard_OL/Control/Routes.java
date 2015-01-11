package MessageBoard_OL.Control;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.io.*;

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
        System.err.println("uri~~~\t"+uri);
        String path=Conf.getRealPath(uri);
//        System.err.println("RealPath"+path);
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

//            if (!request.getMethod().equals(HttpMethod.HEAD)){
System.err.println(path);

                response.headers().set(CONTENT_LENGTH,fileLength);
//                response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
//                response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
                ctx.channel().write(response);

                int bytelenth=0;
//                byte[] b=new byte[8192];
//                raf.seek(0);
//                int readCount=raf.read(b, 0, b.length);
//                ByteBuf buf=copiedBuffer(b);
                raf.seek(0);

//                BufferedInputStream in=new BufferedInputStream(new FileInputStream(file));
//                byte[] b = new byte[8192];
//                int len=0;
//                while ((len=in.read(b))>0){
//                    ByteBuf buf = copiedBuffer(b,0,len);
//                    System.err.println("buf.lenth"+buf.capacity());
//                    ctx.channel().write(buf);
//
//                }
//                ctx.channel().flush();
//                in.close();
//                ctx.channel().closeFuture();


                while(raf.getFilePointer()<raf.length()){
                    byte[] b=new byte[8192];
//                    System.out.println(b.length);
                    int readCount=raf.read(b);
//                    System.err.println(readCount+"b.length"+b.length);
                    ByteBuf buf=copiedBuffer(b,0,readCount);
                    ctx.channel().write(buf);
//                    System.err.println(bytelenth);
                }
                raf.close();

                ctx.channel().writeAndFlush("").addListener(ChannelFutureListener.CLOSE);

//                ctx.channel().closeFuture().addListener(ChannelFutureListener.CLOSE);


//            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally{


//            if (keepAlive) {
////                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH,response.headers());
////                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH,response.getDecoderResult());
//                System.err.println(response.getDecoderResult()+"~~~~~~getDecoderResult");
////            response.headers().set(CONTENT_LENGTH,response.getDecoderResult());
////                response.headers().set(CONTENT_LENGTH,response.getContent().readableBytes());
//        }
//            if (!keepAlive) {
//                System.err.println("!KEEPALVE!!!!!!!!!!!!!!!!!!!!");
//                ctx.channel().closeFuture();
////                e.getFuture().addListener(ChannelFutureListener.CLOSE);
//            }

        }

    }


    public Routes(ChannelHandlerContext ctx,String uri) {


//        System.err.println("RealPath"+path);
        String path=Conf.getRealPath(uri);
        File file = new File(path);
        if (file.isHidden() || !file.exists()) {
            System.err.println("file is hidden or not exist ");
            return;
        }
        if (file.isDirectory()) {
            System.err.println("is Directory");
            return;
        }

        try {

            raf = new RandomAccessFile(file, "r");
            long fileLength = raf.length();
            System.err.println("~~~~~~~~~~~" + String.valueOf(fileLength));

                System.err.println(path);

                response.headers().set(CONTENT_LENGTH, fileLength);
                response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
//                response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
                ctx.channel().write(response);

                int bytelenth = 0;
//                byte[] b=new byte[8192];
//                raf.seek(0);
//                int readCount=raf.read(b, 0, b.length);
//                ByteBuf buf=copiedBuffer(b);
                raf.seek(0);

            while(raf.getFilePointer()<raf.length()){
                byte[] b=new byte[8192];
//                    System.out.println(b.length);
                int readCount=raf.read(b);
//                    System.err.println(readCount+"b.length"+b.length);
                ByteBuf buf=copiedBuffer(b,0,readCount);
                ctx.channel().write(buf);
//                    System.err.println(bytelenth);
            }
            raf.close();

//                while (fileLength != bytelenth) {
//                    byte[] b = new byte[8192];
////                    System.out.println(b.length);
//                    int readCount = raf.read(b, 0, b.length);
////                    System.err.println(readCount+"b.length"+b.length);
//                    ByteBuf buf = copiedBuffer(b);
//
//                    ctx.channel().write(buf);
//
//                    bytelenth += readCount;
////                    System.err.println(bytelenth);
//                }
//            ctx.channel().closeFuture().addListener(ChannelFutureListener.CLOSE);
            ctx.channel().writeAndFlush("").addListener(ChannelFutureListener.CLOSE);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            ctx.channel().flush();

//            if (keepAlive) {
////                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH,response.headers());
////                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH,response.getDecoderResult());
//                System.err.println(response.getDecoderResult() + "~~~~~~getDecoderResult");
////            response.headers().set(CONTENT_LENGTH,response.getDecoderResult());
////                response.headers().set(CONTENT_LENGTH,response.getContent().readableBytes());
//            }
//            if (!keepAlive) {
//                System.err.println("!KEEPALVE!!!!!!!!!!!!!!!!!!!!");
//                ctx.channel().closeFuture();
////                e.getFuture().addListener(ChannelFutureListener.CLOSE);
//            }

        }
    }


    }
