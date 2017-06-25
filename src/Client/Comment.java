package Client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Movahed on 6/24/2017.
 */
public class Comment implements Serializable{
    Date uploadDate;
    String commentText;
    Profile owner;
    ArrayList<Profile> likesOfComment;

    public Comment(Profile owner,String commentText) {
        this.commentText = commentText;
        this.owner = owner;
        uploadDate = new Date(System.currentTimeMillis());
        likesOfComment = new ArrayList<Profile>();
    }
}