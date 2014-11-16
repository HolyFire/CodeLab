package MessageBoard_OL.NettyMsServer;

import MessageBoard_OL.App.MessageBoard;
import MessageBoard_OL.DB.DbHandler;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

/**
 * Created by DELL on 14-8-24.
 */
public class TestDirectory {
    public static void main(String[] args) {
        ArrayList list=new ArrayList();
        System.out.println("I'm in NettyMsServer");
        DbHandler db=DbHandler.getDbHandler();
        db.init();
        int count=db.readcount("messcontent");
        for(int i=0;i<count;i++){
            MessageBoard board=new MessageBoard();
            board=db.showInMes(i);
            list.add(board);

        }
        System.out.println("JSJSJSJSJSJSJS~~~~~~~~~~~~~~"+ JSON.toJSONString(list));



    }
}
