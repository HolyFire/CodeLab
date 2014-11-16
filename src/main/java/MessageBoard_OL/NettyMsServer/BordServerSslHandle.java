//package MessageBoard_OL.NettyMsServer;
//
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.SimpleChannelInboundHandler;
//
///**
// * Created by DELL on 2014-09-22.
// */
//public class BordServerSslHandle extends SimpleChannelInboundHandler {
//
//        private static final Logger logger = Logger.getLogger(
//                ServerSSLHandler.class.getName());
//        private final ThreadLocal<Boolean> COMMAND_FLAG = new ThreadLocal<Boolean>();
//        private final ServerChannelGroup serverChannelGroup = ServerChannelGroup.newInstance();
//
//        @Override
//        public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
//                throws Exception {
//            if (e instanceof ChannelStateEvent) {
//                logger.log(Level.INFO, "Channel state changed: {0}", e);
//            }
//            super.handleUpstream(ctx, e);
//        }
//
//        @Override
//        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
//                throws Exception {
//            //ssl握手
//            SslHandler sslHandler = ctx.getPipeline().get(SslHandler.class);
//            sslHandler.handshake();
//        }
//
//        @Override
//        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
//                throws Exception {
//            System.out.println(this);
//            String request = (String) e.getMessage();
//            //如果接受到客户端发送的bye指令,那么就给客户端回复一个bye指令,客户端接受到后,主动关闭连接
//            //服务器端通过ChannelFutureListener.CLOSE,当它认为客户端已经接受到服务器发送的bye后,也主动关闭连接
//            if (request.toLowerCase().equals("bye")) {
//                ChannelFuture future = e.getChannel().write("bye\r\n");
//                future.addListener(ChannelFutureListener.CLOSE);
//            } else {
//                //以下是我初步解析客户端发送过来的数据,然后决定处理方式
//                RecevieData receivedaData = MessageDecoder.decode(request);
//                if (null != receivedaData) {
//                    //服务器第5版
//                    if (VersionCode.V5.equals(receivedaData.getVersion())) {
//                        //然后判断命令是否存在
//                        for (String s : CommandCode.COMMANDS) {
//                            if (s.equals(receivedaData.getActionType())) {
//                                COMMAND_FLAG.set(true);
//                                if (s.equals(CommandCode.KEEP_ALIVE)) {
//                                    serverChannelGroup.addChannel(e.getChannel());
//                                }
//                                break;
//                            } else {
//                                COMMAND_FLAG.set(false);
//                            }
//                        }
//                        if (COMMAND_FLAG.get()) {
//                            COMMAND_FLAG.set(false);
//                            //将这个命令传递给下一个handler来处理.
//                            //这里的"下一个handler"即为用户自己定义的处理handler
//                            ctx.sendUpstream(e);
//                        } else {
//                            e.getChannel().write(MessageEncoder.encode(receivedaData, StatusCode.NOT_FOUND, StatusCode.NOT_FOUND_TEXT));
//                        }
//                    } else {
//                        //版本错误
//                        e.getChannel().write(MessageEncoder.encode(receivedaData, StatusCode.VERSION_NOT_SUPPORTED, StatusCode.VERSION_NOT_SUPPORTED_TXET));
//                    }
//                } else {
//                    //如果格式错误,那么直接返回
//                    e.getChannel().write(MessageEncoder.encode(receivedaData, null, null));
//                }
//            }
//
//        }
//
//        @Override
//        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
//                throws Exception {
//            logger.log(Level.WARNING, "Server side Unexpected exception from downstream.",
//                    e.getCause());
//            e.getChannel().close();
//            ListenerManager.getListener(ConnectClosedByPeerListener.class).connectClosedByPeer(e.getCause());
//        }
//
//
//    @Override
//    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
//
//    }
//}
