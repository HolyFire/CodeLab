package MessageBoard_OL.NettyMsServer;

import MessageBoard_OL.DB.DbHandler;
import MessageBoard_OL.GetLocation;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import java.io.File;
import java.util.Properties;

/**
 * Created by DELL on 14-7-28.
 */
public class BoardServer {
    static final boolean SSL=System.getProperty("ssl")!=null;
//    static final int PORT =Integer.parseInt(System.getProperty("port",SSL?"8443":System.getenv("PORT")));
    static final int PORT =Integer.parseInt(System.getProperty("port",SSL?"8443":"80"));

    public static void main(String[] args) throws Exception {
        // Configure SSL.


        final SslContext sslCtx;
        if(SSL){
            SelfSignedCertificate ssc=new SelfSignedCertificate();
            sslCtx = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
            System.err.println();
        }else{
            sslCtx=null;
        }
        // Configure the server.




        DbHandler db=DbHandler.getDbHandler();
        db.init();

        EventLoopGroup bossGroup=new NioEventLoopGroup(1);
        EventLoopGroup workerGroup=new NioEventLoopGroup();
        try{
            ServerBootstrap b=new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new BoardServerInit(sslCtx));
            Channel ch=b.bind(PORT).sync().channel();
            System.err.println("Open your web browser and navigate to " +
                    (SSL? "https" : "http") + "://127.0.0.1:" + PORT + '/');
            ch.closeFuture().sync();



//            File directory = new File("");//设定为当前文件夹
//            System.out.println(directory.getCanonicalPath());//获取标准的路径
//            System.out.println("~~~~~~~~up~~~down~~~~~~~~path");
//            Properties props=System.getProperties(); //系统属性
//            System.out.println("Java jar path:"+props.getProperty("java.class.path"));
//            System.out.println("================================================");
//            System.getProperty("user.dir");

//
//            SelfSignedCertificate ssc=new SelfSignedCertificate();
//
//            ServerBootstrap sslB=new ServerBootstrap();
//            sslB.group(bossGroup,workerGroup)
//                    .channel(NioServerSocketChannel.class)
//                    .handler(new LoggingHandler(LogLevel.INFO))
//                    .childHandler(new BoardServerInit(SslContext.newServerContext(ssc.certificate(), ssc.privateKey())));
//            Channel sslCh=sslB.bind(8443).sync().channel();
//            sslCh.closeFuture().sync();


        }finally {
            {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        }

    }
}
