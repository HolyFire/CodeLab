package com.NettyServer;

import com.NettyHtml.HttpSnoopServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * Created by DELL on 14-7-19.
 */
public class HttpServer {
    private static int port;
    public void HttpServer(int port){
        this.port=port;
    }



    public static void run() throws InterruptedException {


        EventLoopGroup bossGroup=new NioEventLoopGroup();
        //这个是用于serversocketchannel的eventloop
        EventLoopGroup workGroup=new NioEventLoopGroup();
        //这个是用于处理accept到的channel

        ServerBootstrap bootstrap=new ServerBootstrap();  //构建serverbootstrap对象
        bootstrap.group(bossGroup,workGroup);
        //设置时间循环对象，前者用来处理accept事件，后者用于处理已经建立的连接的io

        try{
        bootstrap.channel(NioServerSocketChannel.class);
        //用它来建立新accept的连接，用于构造serversocketchannel的工厂类

//        bootstrap.localAddress(port);
        bootstrap.childHandler(new ChannelInitializer() {
            //为accept channel的pipeline预添加的inboundhandler
            @Override
            protected void initChannel(Channel channel) throws Exception {

                ChannelPipeline p=channel.pipeline();

                p.addLast(new HttpRequestDecoder());//用于解析http报文的handler
                // Uncomment the following line if you don't want to handle HttpChunks.
//                p.addLast(new HttpObjectAggregator(1048576));
                // 用于将解析出来的数据封装成http对象，httprequest什么的
                p.addLast(new HttpResponseEncoder());
                // Remove the following line if you don't want automatic content compression.
                //p.addLast(new HttpContentCompressor());
                //用于将response编码成httpresponse报文发送
                p.addLast("handshake",new WebSocketServerProtocolHandler("","",true));
                //websocket的handler部分定义的，它会自己处理握手等操作

                p.addLast(new HttpServerHandler());

            }
        });

        //bind方法会创建一个serverchannel，并且会将当前的channel注册到eventloop上面，
        //会为其绑定本地端口，并对其进行初始化，为其的pipeline加一些默认的handler
        ChannelFuture f = bootstrap.bind(8000).sync();
        f.channel().closeFuture().sync();  //相当于在这里阻塞，直到serverchannel关闭
    } finally {
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }





    }
    public static void main(String args[]) throws Exception {
        new HttpServer().run();
    }

}
