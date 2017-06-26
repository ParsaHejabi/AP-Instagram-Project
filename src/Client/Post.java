package Client;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by parsahejabi on 6/21/17.
 */
public class Post implements Comparable,Serializable {
    static public final String POST_NAME = "Post";
    Profile owner;
    Date uploadDate;
    File image;
    ArrayList<Profile> liked;
    ArrayList<Comment> comments;
    String caption;
    boolean canComment;
    String id;

    @Override
    public int hashCode() {
        return image.getAbsolutePath().hashCode() + (owner.hashCode() * 31);
    }

    @Override
    public int compareTo(Object o) {
        return ((Post) o).uploadDate.compareTo(this.uploadDate);
    }

    Post(Profile owner, File image, String  caption, boolean canComment)
    {
        this.uploadDate = new Date(System.currentTimeMillis());
        this.owner = owner;
        this.id = Integer.toString(owner.posts.size());
        this.canComment = canComment;
        if(canComment)
            comments = new ArrayList<>();
        else
            comments = new ArrayList<>(0);
        this.image = image;
        this.caption = caption;
        liked = new ArrayList<>();
    }

}