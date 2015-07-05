package MessageBoard_OL;

/**
 * Created by DELL on 2014-10-26.
 */
public class GetLocation {

    String jarName="MavenProjectSample.jar";

    public GetLocation(){

    }
    public GetLocation(String jarName){

        this.jarName=jarName;
    }

    public String getLocation(){
        String location=new GetLocation().getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        String url;
        if(location.indexOf("target/classes/")!=-1){
//            System.err.println(location.indexOf("target/classes/")-1);
            url=location.substring(0,location.indexOf("target/classes/")-1);
        }else{
            url=location.substring(0,(location.length()-jarName.length()-1));
        }
//        String test="ab";
//        String url=test.substring(0,1);
        return url;
    }
//
    public static void main(String[] args) {
        System.err.println(new GetLocation().getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        System.err.println(new GetLocation().getLocation());

    }

}
