package com.NettyHtml.config;

/**
 * Created by DELL on 14-7-19.
 */
public class Config {
    public static String getRealPath(String uri) {
        StringBuilder sb=new StringBuilder("F:/IdeaProjects/MavenProjectSample/src/main/java/com/NettyHtml/web");
//        if(uri.equals("/")){
//            return "F:/IdeaProjects/MavenProjectSample/src/main/java/com/NettyHtml/web/index.html";
//        }
        sb.append(uri);
        if (!uri.endsWith("/")) {
            sb.append('/');
        }
        return sb.toString();
    }
}
