package com.NettyHtml;

import com.NettyHtml.config.Config;
import com.NettyHtml.config.routes;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.netty.buffer.Unpooled.copiedBuffer;
import static io.netty.handler.codec.http.HttpHeaders.getHost;
import static io.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpVersion.*;
import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
/**
 * Created by DELL on 14-7-18.
 */
public class HttpSnoopServerHandler extends SimpleChannelInboundHandler<Object> {

    private final StringBuilder responseContent = new StringBuilder();

    private HttpRequest request;
    /** Buffer that stores the response content */
    private final StringBuilder buf = new StringBuilder();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
              HttpRequest request = this.request = (HttpRequest) msg;
//            TODO
            URI uri = new URI(request.getUri());
            System.err.println("request uri==" + uri.getPath());

//            if (uri.getPath().equals("/favicon.ico")) {
//
////                ByteBuf buf = copiedBuffer("    <script language=\"javascript\">\n" +
////                        "\n" +
////                        "           window.location.href=\"index.html\"; \n" +
////                        "\n" +
////                        "            //比较常用的方法，没什么可解释的，后面直接跟指定要跳转的地方。\n" +
////                        "\n" +
////                        "    </script>", CharsetUtil.UTF_8);
////                final String path = Config.getRealPath(uri.getPath());
//
//                ByteBuf buf = copiedBuffer("Hello,favicon.ico",CharsetUtil.UTF_8);
//                // Build the response object.
//                FullHttpResponse response = new DefaultFullHttpResponse(
//                        HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
//                response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
//                response.headers().set(CONTENT_LENGTH, buf.readableBytes());
//                // Write the response.
//                ctx.channel().writeAndFlush(response);

//            if (uri.getPath().equals("/")) {
//
//                writeMenu(ctx);
//                return;
//            }

            if(uri.getPath()!=null){
                new routes(ctx,request);
                return;
            }
//
//                return;
//            }

//TODO




            if (is100ContinueExpected(request)) {
                   send100Continue(ctx);
              }

              buf.setLength(0);
              buf.append("WELCOME TO THE WILD WILD WEB SERVER\r\n");
              buf.append("===================================\r\n");

              buf.append("VERSION: ").append(request.getProtocolVersion()).append("\r\n");
              buf.append("HOSTNAME: ").append(getHost(request, "unknown")).append("\r\n");
              buf.append("REQUEST_URI: ").append(request.getUri()).append("\r\n\r\n");

              HttpHeaders headers = request.headers();
              if (!headers.isEmpty()) {
                  for (Map.Entry<String, String> h: headers) {
                      String key = h.getKey();
                      String value = h.getValue();
                      buf.append("HEADER: ").append(key).append(" = ").append(value).append("\r\n");
                  }
                  buf.append("\r\n");
              }

              QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.getUri());
              Map<String, List<String>> params = queryStringDecoder.parameters();
              if (!params.isEmpty()) {
                  for (Map.Entry<String, List<String>> p: params.entrySet()) {
                      String key = p.getKey();
                      List<String> vals = p.getValue();
                      for (String val : vals) {
                         buf.append("PARAM: ").append(key).append(" = ").append(val).append("\r\n");
                      }
                  }
                 buf.append("\r\n");
              }

              appendDecoderResult(buf, request);
          }

//         if (msg instanceof HttpContent) {
//             HttpContent httpContent = (HttpContent) msg;
//
//             ByteBuf content = httpContent.content();
//             if (content.isReadable()) {
//                  buf.append("CONTENT: ");
//                  buf.append(content.toString(CharsetUtil.UTF_8));
//                  buf.append("\r\n");
//                  appendDecoderResult(buf, request);
//             }
//
//             if (msg instanceof LastHttpContent) {
//                  buf.append("END OF CONTENT\r\n");
//
//                   LastHttpContent trailer = (LastHttpContent) msg;
//                   if (!trailer.trailingHeaders().isEmpty()) {
//                       buf.append("\r\n");
//                        for (String name: trailer.trailingHeaders().names()) {
//                            for (String value: trailer.trailingHeaders().getAll(name)) {
//                               buf.append("TRAILING HEADER: ");
//                                buf.append(name).append(" = ").append(value).append("\r\n");
//                            }
//                        }
//                       buf.append("\r\n");
//                    }
//
//                   if (!writeResponse(trailer, ctx)) {
//                        // If keep-alive is off, close the connection once the content is fully written.
//                        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
//                    }
//                }
//            }
        }
    private static void appendDecoderResult(StringBuilder buf, HttpObject o) {
        DecoderResult result = o.getDecoderResult();
          if (result.isSuccess()) {
             return;
         }

         buf.append(".. WITH DECODER FAILURE: ");
         buf.append(result.cause());
         buf.append("\r\n");
      }

     private boolean writeResponse(HttpObject currentObj, ChannelHandlerContext ctx) {
         // Decide whether to close the connection or not.
         boolean keepAlive = isKeepAlive(request);
         // Build the response object.
         FullHttpResponse response = new DefaultFullHttpResponse(
                 HTTP_1_1, currentObj.getDecoderResult().isSuccess()? OK : BAD_REQUEST,
                 copiedBuffer(buf.toString(), CharsetUtil.UTF_8));

         response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

         if (keepAlive) {
            // Add 'Content-Length' header only for a keep-alive connection.
             response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
             // Add keep alive header as per:
             // - http://www.w3.org/Protocols/HTTP/1.1/draft-ietf-http-v11-spec-01.html#Connection
            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
         }

        // Encode the cookie.
        String cookieString = request.headers().get(COOKIE);
        if (cookieString != null) {
             Set<Cookie> cookies = CookieDecoder.decode(cookieString);
            if (!cookies.isEmpty()) {
                // Reset the cookies if necessary.
                for (Cookie cookie: cookies) {
                    response.headers().add(SET_COOKIE, ServerCookieEncoder.encode(cookie));
                }
             }
         } else {
             // Browser sent no cookie.  Add some.
             response.headers().add(SET_COOKIE, ServerCookieEncoder.encode("key1", "value1"));
             response.headers().add(SET_COOKIE, ServerCookieEncoder.encode("key2", "value2"));
         }

        // Write the response.
         ctx.write(response);


         return keepAlive;
     }

     private static void send100Continue(ChannelHandlerContext ctx) {
         FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, CONTINUE);
         System.out.println("~~~~~~~~~~~~~send100Continue~~~~~~~~~~~~~~~~");
         ctx.write(response);
     }

     @Override
     public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
         cause.printStackTrace();
         ctx.close();
     }

//    private void writeMenu(ChannelHandlerContext ctx) {
//
//        // print several HTML forms
//
//        // Convert the response content to a ChannelBuffer.
//
//        responseContent.setLength(0);
//
//
//
//        // create Pseudo Menu
//
//        responseContent.append("<html>");
//
//        responseContent.append("<head>");
//
//        responseContent.append("<title>Netty Test Form</title>\r\n");
//
//        responseContent.append("</head>\r\n");
//
//        responseContent.append("<body bgcolor=white><style>td{font-size: 12pt;}</style>");
//
//
//
//        responseContent.append("<table border=\"0\">");
//
//        responseContent.append("<tr>");
//
//        responseContent.append("<td>");
//
//        responseContent.append("<h1>Netty Test Form</h1>");
//
//        responseContent.append("Choose one FORM");
//
//        responseContent.append("</td>");
//
//        responseContent.append("</tr>");
//
//        responseContent.append("</table>\r\n");
//
//
//
//        // GET
//
//        responseContent.append("<CENTER>GET FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
//
//        responseContent.append("<FORM ACTION=\"/from-get\" METHOD=\"GET\">");
//
//        responseContent.append("<input type=hidden name=getform value=\"GET\">");
//
//        responseContent.append("<table border=\"0\">");
//
//        responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"info\" size=10></td></tr>");
//
//        responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"info\" size=10></td></tr>");
//
//        responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"secondinfo\" size=20>");
//
//        responseContent
//
//                .append("<tr><td>Fill with value: <br> <textarea name=\"thirdinfo\" cols=40 rows=10></textarea>");
//
//        responseContent.append("</td></tr>");
//
//        responseContent.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
//
//        responseContent.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
//
//        responseContent.append("</table></FORM>\r\n");
//
//        responseContent.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
//
//
//
//        // POST
//
//        responseContent.append("<CENTER>POST FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
//
//        responseContent.append("<FORM ACTION=\"/from-post\" METHOD=\"POST\">");
//
//        responseContent.append("<input type=hidden name=getform value=\"POST\">");
//
//        responseContent.append("<table border=\"0\">");
//
//        responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"info\" size=10></td></tr>");
//
//        responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"secondinfo\" size=20>");
//
//        responseContent
//
//                .append("<tr><td>Fill with value: <br> <textarea name=\"thirdinfo\" cols=40 rows=10></textarea>");
//
//        responseContent.append("<tr><td>Fill with file (only file name will be transmitted): <br> "
//
//                        + "<input type=file name=\"myfile\">");
//
//        responseContent.append("</td></tr>");
//
//        responseContent.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
//
//        responseContent.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
//
//        responseContent.append("</table></FORM>\r\n");
//
//        responseContent.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
//
//        responseContent.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
//
//        responseContent.append("</body>");
//
//        responseContent.append("</html>");
//
//
//
//        ByteBuf buf = copiedBuffer(responseContent.toString(), CharsetUtil.UTF_8);
//
//        // Build the response object.
//
//        FullHttpResponse response = new DefaultFullHttpResponse(
//
//                HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
//
//
//
//        response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
//
//        response.headers().set(CONTENT_LENGTH, buf.readableBytes());
//
//
//
//        // Write the response.
//
//        ctx.channel().writeAndFlush(response);
//
//    }



}
