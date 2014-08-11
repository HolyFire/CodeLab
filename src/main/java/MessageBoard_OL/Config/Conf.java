package MessageBoard_OL.Config;

/**
 * Created by DELL on 14-7-28.
 */
public class Conf {
    public static String getRealPath(String uri){
//        StringBuilder sb=new StringBuilder("F:/IdeaProjects/MavenProjectSample/src/main/java/MessageBoard_OL/Web");
        StringBuilder sb=new StringBuilder(System.getProperty("user.dir")+"/src/main/java/MessageBoard_OL/Web");
        if(uri.equals("/")){
            sb.append("/index.html");
            return sb.toString();
        }
        if(!uri.equals("/")){
            sb.append(uri);

        }
        if(!uri.endsWith("/")){
            sb.append("/");
        }


        return sb.toString();
    }

    public static void main(String[] args) {

    }
}
