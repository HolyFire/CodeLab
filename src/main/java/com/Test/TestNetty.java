package com.Test;


import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.EmptyByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.logging.Logger;


/**
 * Created by DELL on 14-7-16.
 */
public class TestNetty {
    public void startNettyClient(){
        try {
            new EchoClient("192.168.1.105",9000).start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void startNettyServer(){
        try {
            new EchoServer(9000).start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

/*
netty 应答服务器
 */
class EchoServer{
    private final int port;
    EchoServer(int port){
        this.port=port;
    }

    void start() throws InterruptedException {
        EventLoopGroup group=new NioEventLoopGroup();
        try{
            //create ServerBootstrap instance
            ServerBootstrap bootstrap=new ServerBootstrap();
            //Specifies NIO transport,local socket address
            //Adds handler to channel pipeline
// 通过nio方式来接收连接和处理连接
// 设置nio类型的channel
// 设置监听端口

            bootstrap.group(group).channel(NioServerSocketChannel.class).localAddress(port).childHandler(new ChannelInitializer<Channel>() {
//有连接到达时会创建一个channel
                @Override
                protected void initChannel(Channel channel) throws Exception {
// pipeline管理channel中的Handler，在channel队列中添加一个handler来处理业务
                    channel.pipeline().addLast(new EchoServerHandler());
                }
            });
            //Binds Server,waits for server to close, and releases resources
// 配置完成，开始绑定server，通过调用sync同步方法阻塞直到绑定成功
            ChannelFuture future=bootstrap.bind().sync();
            System.out.println(EchoServer.class.getName()+"Started and listen on"+future.channel().localAddress());
// 应用程序会一直等待，直到channel关闭

            future.channel().closeFuture().sync();
        }finally{
            //关闭EventLoopGroup，释放掉所有资源包括创建的线程
            group.shutdownGracefully().sync();
        }
    }
}


/*
业务逻辑ServerHandler
要想处理接收到的数据,
你的channel handler必须继承ChannelInboundHandlerAdapter
并且重写channelRead方法，
这个方法在任何时候都会被调用来接收数据（一般是Byte类型数组）
 */

@ChannelHandler.Sharable
//注解@Sharable可以让它在channels间共享

class EchoServerHandler extends ChannelInboundHandlerAdapter {
    static final Logger logger = Logger.getLogger(EchoServerHandler.class.getName());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("!~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~!");
        System.out.println("Server received: "+msg.toString()+"~~~~~"+msg);

        ctx.write(msg);//写回数据
        ctx.flush();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)throws Exception{
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        //flush掉所有写回的数据
        //当flush完成后关闭channel
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) throws Exception{
        cause.printStackTrace();
        ctx.close();
//出现异常时关闭channel
    }

}


/*
Netty Client
连接服务器
写数据到服务器
等待接受服务器返回相同的数据
关闭连接
 */
class EchoClient{
    private final String host;
    private final int port;
    public EchoClient(String host,int port){
        this.host=host;
        this.port=port;
    }
    public void start() throws InterruptedException {
        EventLoopGroup group=new NioEventLoopGroup();

        try{
        Bootstrap bootstrap=new Bootstrap();
        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.remoteAddress(new InetSocketAddress(host,port));
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new EchoClientHandler());
            }
        });

        ChannelFuture future=bootstrap.connect().sync();
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()){
                    System.out.println("client connected");
                }else{
                    System.out.println("server attempt failed");
                    channelFuture.cause().printStackTrace();
                }
            }
        });
        future.channel().closeFuture().sync();
        }finally{
            group.shutdownGracefully();
        }
    }
}
/*
创建启动一个客户端包含下面几步：
创建Bootstrap对象用来引导启动客户端
创建EventLoopGroup对象并设置到Bootstrap中，EventLoopGroup可以理解为是一个线程池，这个线程池用来处理连接、接受数据、发送数据
创建InetSocketAddress并设置到Bootstrap中，InetSocketAddress是指定连接的服务器地址
添加一个ChannelHandler，客户端成功连接服务器后就会被执行
调用Bootstrap.connect()来连接服务器
最后关闭EventLoopGroup来释放资源
 */
/*
实现客户端业务逻辑
在这里将自定义一个继承SimpleChannelInboundHandler的ChannelHandler来处理业务；
通过重写父类的三个方法来处理感兴趣的事件：
channelActive()：客户端连接服务器后被调用
channelRead0()：从服务器接收到数据后调用
exceptionCaught()：发生异常时被调用
 */

class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf>{
    /**
     *此方法会在连接到服务器后被调用
     * */
    public void channelActive(ChannelHandlerContext ctx) {
//        ctx.write(Unpooled.copiedBuffer("Netty rocks", CharsetUtil.UTF_8));
        String str= null;
        try {
            str = new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ctx.write(Unpooled.copiedBuffer(str,CharsetUtil.UTF_8));
        ctx.flush();
    }

    /**
     *此方法会在接收到服务器数据后调用
     * */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {

        System.out.println("Client received: " + ByteBufUtil.hexDump(byteBuf.readBytes(byteBuf.readableBytes())));

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

