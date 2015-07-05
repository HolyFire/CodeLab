package MessageBoard_OL;

/**
 * Created by DELL on 2015-06-07.
 */
public class TestInMessageBoard_OL {
    public static void main(String[] args) {
        String a="第三(个Tag";
        System.out.println(a.matches("[^\\*\"!\\(\\)]+"));
    }
}
