package com.NettyHtml;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.ssl.SslContext;

/**
 * Created by DELL on 14-7-18.
 */
public class HttpSnoopClientInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;

    public HttpSnoopClientInitializer(SslContext sslCtx){
        this.sslCtx=sslCtx;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        ChannelPipeline p=socketChannel.pipeline();
//        Enable HTTPS if nessary.
        if(sslCtx!=null){
            p.addLast(sslCtx.newHandler(socketChannel.alloc()));
        }
        p.addLast(new HttpClientCodec());

//        Remove the following line if you dont't want automatic content decompression.
        p.addLast(new HttpContentDecompressor());

        // Uncomment the following line if you don't want to handle HttpContents.
        //p.addLast(new HttpObjectAggregator(1048576));

        p.addLast(new HttpSnoopClientHandler());
    }
}
