import java.sql.Timestamp;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by DELL on 14-8-24.
 */
public class TestMain {
    public static void main(String[] args) {
        System.out.println("Welcome to CodeLab test");
        Date d=new Date();
        Timestamp now1 =new Timestamp(new java.util.Date().getTime());
        System.err.println(now1);
        Timestamp now=new Timestamp(d.getTime());
        System.out.println(now);
        Timestamp createtime=now;
        System.err.println(createtime);

        String a="aaaabbbbb&d&bbbbaaaa";
        Pattern pattern=Pattern.compile("&(.*)&");
        Matcher m=pattern.matcher(a);
        while (m.find()){
            System.out.println("in");

            a=a.replaceAll("&(.*)&","*****");
            System.out.println(m.replaceAll("*****"));
        }
        System.out.println(a);

    }
}
