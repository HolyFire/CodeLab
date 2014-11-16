package MessageBoard_OL.App;

/**
 * Created by DELL on 2014-11-05.
 */
public interface GuestUser {
    public abstract String getHost();
    public abstract String getRawPath(String code);
    public abstract String getAccess_Token();
    public abstract String getClient_Id();
    public abstract String getOpenId();
    public abstract void setAccess_Token(String Access_Token);
    public abstract void setClient_Id(String Client_Id);
    public abstract void setOpenId(String OpenId);
    public abstract String getCode();
    public abstract void setCode(String code);

}
