package Client;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by parsahejabi on 6/26/17.
 */
public class News implements Serializable {
    //like comment follow
    String state;
    Post relatedPost;
    String commentCaption;

    //who made this news
    Profile owner;
    Date newsDate;

    public News(Profile owner, String state, Post relatedPost, String commentCaption) {
        this.owner = owner;
        this.state = state;
        this.relatedPost = relatedPost;
        this.commentCaption = commentCaption;
        newsDate = new Date(System.currentTimeMillis());
    }

    @Override
    public int hashCode() {
        int hash = owner.hashCode() * 31;
        hash = hash * 31 + state.hashCode();
        hash = hash * 31 + (relatedPost != null ? relatedPost.hashCode() : 0);
        hash = hash * 31 + (commentCaption != null ? commentCaption.hashCode() : 0);
        return hash;
    }
}