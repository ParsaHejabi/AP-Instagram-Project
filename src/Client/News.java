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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        News n = ((News) o);
        if(this.owner.equals(n.owner))
        {
            if(this.state.equals(n.state))
            {
                if(this.state.equals("LikeNews"))
                {
                    if(this.relatedPost.equals(n.relatedPost))
                        return true;
                }
                else if(this.state.equals("CommentNews"))
                {
                     if(this.relatedPost.equals(n.relatedPost))
                     {
                         if (this.commentCaption.equals(n.commentCaption))
                             return true;
                     }
                }
                else
                    return true;
            }
        }
        return false;

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