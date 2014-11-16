package MessageBoard_OL.App;

/**
 * Created by DELL on 14-8-2.
 */
public class MessageBoard {
    private String username;
    private String content;
    private String location;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
            this.location = location;
    }

    public MessageBoard(){

    }

    public  MessageBoard(String username,String content,String location){
        this.username=username;
        this.content=content;
        this.location=location;
    }

    @Override
    public String toString() {

        return "MessageBoard{" +
                "username='" + username +'\''+
                ", content='" + content +'\''+
                ", location='"+location+'\''+
                '}';


    }
}
