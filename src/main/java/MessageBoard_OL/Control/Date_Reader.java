package MessageBoard_OL.Control;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by DELL on 2015-01-11.
 */
public class Date_Reader {
    public static String getRealDate(Date date){
        String realDate = new String();
        String year=new SimpleDateFormat("yyyy").format(date);
        int month=Integer.parseInt(new SimpleDateFormat("mm").format(date))+1;
        String strmonth=new String();
        if(month<10){
            strmonth="0"+month;
        }else{
            strmonth=String.valueOf(month);
        }
        String day=new SimpleDateFormat("dd").format(date);
        realDate=year+"-"+strmonth+"-"+day;
        return realDate;
    }
}
