package MessageBoard_OL.Control;

import MessageBoard_OL.GetLocation;

/**
 * Created by DELL on 14-7-28.
 */
public class Conf {
    public static String getRealPath(String uri){
//        StringBuilder sb=new StringBuilder("F:/IdeaProjects/MavenProjectSample/src/main/java/MessageBoard_OL/Web");

        StringBuilder sb=new StringBuilder(new GetLocation().getLocation()+"/Web");
//        StringBuilder sb=new StringBuilder(System.getProperty("user.dir")+"/Web");
//        StringBuilder sb=new StringBuilder("/app/Web");

//        StringBuilder sb=new StringBuilder(Thread.currentThread().getContextClassLoader().getResource("")+"Web");

//        set index
        if(uri.equals("/")||uri.equals("/home")){
//            sb.append("/index.html");
            sb.append("/StationWel.html");
            return sb.toString();
        }
        if(uri.equals("/collection")){
            sb.append("/ViewModel/collection.html");
            return sb.toString();
        }
        if(uri.equals("/add")){
            sb.append("/ControlPage.html");
            return  sb.toString();
        }

        if(!uri.equals("/")){
            sb.append(uri);

        }
        if(!uri.endsWith("/")){
            sb.append("/");
        }


        return sb.toString();
    }



}
