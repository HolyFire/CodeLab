package MessageBoard_OL.App;

/**
 * Created by DELL on 2014-11-05.
 */
public class QQUser implements GuestUser {
    private String Access_Token;
    private String Client_Id;
    private String OpenId;
    private String Code;

    @Override
    public String getCode() {
        return Code;
    }

    @Override
    public void setCode(String code) {
        Code = code;
    }
    private static String host="graph.qq.com";

//    单例
//    private QQUser(){
//
//    }
//    private static QQUser qqUser=new QQUser();
//
//    public static QQUser getInstance(){
//        return qqUser;
//    }

    @Override
    public void setOpenId(String openId) {
        OpenId = openId;
    }

    @Override
    public void setAccess_Token(String access_Token) {
        Access_Token = access_Token;
    }

    @Override
    public void setClient_Id(String client_Id) {
        Client_Id = client_Id;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getRawPath(String code) {
        return "/oauth2.0/token?grant_type=authorization_code&client_id=101165433&client_secret=ee3aa95133cc7bb1b9e6d1875e8fdbb8&code="+code+"&redirect_uri=holyfire.me/accesstoken_QQ";
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
}
