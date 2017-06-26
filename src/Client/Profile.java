package Client;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by parsahejabi on 6/21/17.
 */
public class Profile implements Serializable{
    String email;
    String password;
    String username;
    String fullName;
    String biography;
    File profilePicture;


    ArrayList<Profile> following;
    ArrayList<Profile> followers;
    ArrayList<Post> posts;
    ArrayList<News> news;

    public Profile(String email, String password, String username, String fullName, String biography, File profilePicture) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.fullName = fullName;
        this.biography = biography;
        this.profilePicture = profilePicture;
        following = new ArrayList<>();
        followers = new ArrayList<>();
        posts = new ArrayList<>();
        news = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Profile profile = (Profile) o;
        if (this.email.equals(profile.email)) return true;
        return false;
    }

    @Override
    public int hashCode() {
        int result = (email!= null ? email.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);

        return result;
    }

}


