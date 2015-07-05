package MessageBoard_OL.App;

/**
 * Created by DELL on 2014-11-08.
 */
public class DouBanUser implements GuestUser{

    private String Code;

    @Override
    public String getCode() {
        return Code;
    }

    @Override
    public void setCode(String code) {
        Code = code;
    }

    private String Access_Token;
    private String Client_Id;
    private String OpenId;
    private static String host="www.douban.com";
//    private static String host="www.douban.com/service/auth2/token";

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getRawPath(String code) {
//        return "/service/auth2/token?client_id=09d80c679aef1e721d882536d0cd5b56&client_secret=ebbc55953577d36f&redirect_uri=messageboard-fg1.herokuapp.com/accesstoken_douban&grant_type=authorization_code&code="+code;
        return "client_id=09506d93f402cc441bf148bbdc0864cc&client_secret=85bafd7d0adb0772&redirect_uri=holyfire.me/accesstoken_douban&grant_type=authorization_code&code="+code;
    }

    @Override
    public String getAccess_Token() {
        return Access_Token;
    }

    @Override
    public String getClient_Id() {
        return Client_Id;
    }

    @Override
    public String getOpenId() {
        return OpenId;
    }

    @Override
    public void setAccess_Token(String Access_Token) {
        this.Access_Token=Access_Token;

    }

    @Override
    public void setClient_Id(String Client_Id) {
        this.Client_Id=Client_Id;
    }

    @Override
    public void setOpenId(String OpenId) {
        this.OpenId=OpenId;
    }
}
