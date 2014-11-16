package MessageBoard_OL.NettyMsServer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * Created by DELL on 14-7-28.
 */
public class BoardServerInit extends ChannelInitializer<SocketChannel> {
    private final SslContext sslCtx;
    public BoardServerInit(SslContext sslCtx) {
        this.sslCtx=sslCtx;
    }


    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline p=socketChannel.pipeline();
        if(sslCtx !=null){
//            p.addLast(sslCtx.newHandler(socketChannel.alloc()));
            SSLEngine engine = HttpSslContextFactory.getServerContext().createSSLEngine();
            engine.setUseClientMode(false);
//        engine.setNeedClientAuth(true);
            p.addLast("ssl", new SslHandler(engine));
        }

//        SSLEngine engine = HttpSslContextFactory.getServerContext().createSSLEngine();
//        engine.setUseClientMode(false);
////        engine.setNeedClientAuth(true);
//        p.addLast("ssl", new SslHandler(engine));

        p.addLast(new HttpRequestDecoder());
        // Uncomment the following line if you don't want to handle HttpChunks.
        p.addLast(new HttpObjectAggregator(1048576));
        p.addLast(new HttpResponseEncoder());
        // Remove the following line if you don't want automatic content compression.
        //p.addLast(new HttpContentCompressor());
        p.addLast(new BoardServerHandler());


        //以下是HTTPS的设置
//        SSLEngine engine = HttpSslContextFactory.getServerContext().createSSLEngine();
//        SSLEngine sslEngine = SSLContextProvider.get().createSSLEngine();

//        SSLEngine engine = HttpSslContextFactory.get().createSSLEngine();
//        engine.setUseClientMode(false);    //非客户端模式
//        p.addLast("ssl", new SslHandler(engine));



    }
}
