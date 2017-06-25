package Client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import javax.swing.text.html.ListView;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by parsahejabi on 6/21/17.
 */
public class ClientHandler implements Runnable{
    Socket clientSocket;
    ObjectOutputStream clientOutputStream;
    ObjectInputStream clientInputStream;
    String clientMessage;
    String username;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static boolean isEmailValid(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            clientOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            clientInputStream = new ObjectInputStream(clientSocket.getInputStream());

            do {
                clientMessage = clientInputStream.readUTF();
                System.out.println(Thread.currentThread().getName() + " said: " + clientMessage);
                if (clientMessage.equals("Login")){
                    System.out.println("Logging in...");
                    String usernameOrEmail = clientInputStream.readUTF();
                    String password = clientInputStream.readUTF();
                    if (isEmailValid(usernameOrEmail)){
                        if (loginChecker(usernameOrEmail,password,true)){
                            clientOutputStream.writeBoolean(true);
                            clientOutputStream.flush();
                            login(usernameOrEmail);
                        }
                        else{
                            clientOutputStream.writeBoolean(false);
                            clientOutputStream.flush();
                        }
                    }
                    else {
                        if (loginChecker(usernameOrEmail,password)){
                            clientOutputStream.writeBoolean(true);
                            clientOutputStream.flush();
                            login(usernameOrEmail);
                            break;
                        }
                        else{
                            clientOutputStream.writeBoolean(false);
                            clientOutputStream.flush();
                        }
                    }
                }
                else if (clientMessage.equals("Signup")){
                    System.out.println("Singing up...");
                    String email = clientInputStream.readUTF();
                    String username = clientInputStream.readUTF();
                    String password = clientInputStream.readUTF();
                    String registerStatus = registerChecker(email, username);
                    if (registerStatus.equals("Correct")){
                        clientOutputStream.writeUTF("Correct");
                        clientOutputStream.flush();
                        String fullName = clientInputStream.readUTF();
                        String biography = clientInputStream.readUTF();
                        String pictureStatus = clientInputStream.readUTF();

                        Profile profile = null;
                        if (pictureStatus.equals("Pic"))
                        {
                            File dir = new File(Server.profilesDir, username+"/");
                            dir.mkdirs();
                            File profilePic = new File(dir,"profilePic");
                            profilePic.createNewFile();
                            byte[] bytes = ((byte[]) clientInputStream.readObject());
                            Files.write(profilePic.toPath(),bytes );
                            profile = new Profile(email,password,username,fullName,biography, profilePic);
                        }
                        else if (pictureStatus.equals("Skip"))
                        {
                            File file = new File(Server.profilesDir, username+"/");
                            file.mkdirs();
                            File profilePic = new File(file,"profilePic");
                            profilePic.createNewFile();
                            File defaultProfilePic = new File("src/Client/Assets/defaultProfilePicture.png");
                            Files.write(profilePic.toPath(), Files.readAllBytes(defaultProfilePic.toPath()));
                            profile = new Profile(email,password,username,fullName,biography, profilePic);
                        }

                        Server.profiles.add(profile);
                        Server.serialize(profile);
                        login(username);
                        break;
                    }
                    else if (registerStatus.equals("Email")){
                        clientOutputStream.writeUTF("Email");
                        clientOutputStream.flush();
                    }
                    else if (registerStatus.equals("Username")){
                        clientOutputStream.writeUTF("Username");
                        clientOutputStream.flush();
                    }
                }

            }while (!clientMessage.equals("Exit"));
            do {
                clientMessage = clientInputStream.readUTF();
                System.out.println(clientMessage);
                refreshClientOwner(profileFinder(username));
                if (clientMessage.equals("Profile1"))
                {
                }
                else if ( clientMessage.equals("Profile2")) {
                    Profile currentClient = profileFinder(username);
                    ArrayList<Post> postsToShow = new ArrayList<>();
                    for (Post post: currentClient.posts){
                        postsToShow.add(post);
                    }
                    clientOutputStream.reset();
                    clientOutputStream.writeObject(postsToShow);
                    clientOutputStream.flush();
                }
                else if (clientMessage.equals("Home"))
                {
                    Profile currentClient = profileFinder(username);
                    ArrayList<Post> postsToShow = new ArrayList<>();
                    for (Profile profile: currentClient.following){
                        for (Post post: profile.posts){
                            postsToShow.add(post);
                        }
                    }
                    for (Post post: currentClient.posts){
                        postsToShow.add(post);
                    }
                    postsToShow.sort(null);
                    clientOutputStream.reset();
                    clientOutputStream.writeObject(postsToShow);
                    clientOutputStream.flush();
                }
                else if (clientMessage.equals("Search"))
                {
                    String userCommand;
                    System.out.println("in search mode");
                    do{
                        userCommand = clientInputStream.readUTF();

                        if (userCommand.contains("SearchProfile")) {
                            String searchedToken = userCommand.split(":", 2)[1];
                            if (searchedToken.length() > 2)
                            {
                                Profile searchingClient = profileFinder(username);
                                ArrayList<Profile> searchedProfile = Server.search(searchedToken, searchingClient);
                                clientOutputStream.reset();
                                clientOutputStream.writeObject(searchedProfile);
                                clientOutputStream.flush();
                            }
                        }
                        if(userCommand.contains("People"))
                        {
                            Profile currentClient = profileFinder(username);
                            refreshClientOwner(currentClient);
                            String peopleUsername = userCommand.split(":", 2)[1];
                            Profile requestedProfile = profileFinder(peopleUsername);
                            clientOutputStream.reset();
                            clientOutputStream.writeObject(requestedProfile);
                            clientOutputStream.flush();


                            userCommand = clientInputStream.readUTF();
                            if (userCommand.contains("FollowUnfollow"))
                            {
                                if(currentClient.following.contains(requestedProfile))
                                {
                                    currentClient.following.remove(requestedProfile);
                                    requestedProfile.followers.remove(currentClient);
                                    Server.serialize(requestedProfile);
                                    Server.serialize(currentClient);
                                }
                                else {
                                    currentClient.following.add(requestedProfile);
                                    requestedProfile.followers.add(currentClient);
                                    Server.serialize(requestedProfile);
                                    Server.serialize(currentClient);
                                }
                            }



                        }

                    }while (!userCommand.equals("Exit"));
                }
                else if(clientMessage.equals("Share"))
                {
                    clientMessage = clientInputStream.readUTF();
                    if (clientMessage.equals("SharePost"))
                    {
                        Profile curentClient = profileFinder(username);
                        File f = ((File) clientInputStream.readObject());
                        String caption = clientInputStream.readUTF();
                        boolean canComment = clientInputStream.readBoolean();
                        Server.createPost(curentClient,f, canComment, caption);
                    }


                }
            }while (!clientMessage.equals("Exit"));

            System.out.println(Thread.currentThread().getName() + " is closed!");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void login(String usernameOrEmail) throws IOException {
        Profile p = profileFinder(usernameOrEmail);
        username = p.username;
        clientOutputStream.writeObject(p);
        clientOutputStream.flush();
    }

    private String registerChecker(String email, String username) throws IOException {
        for (Profile p:Server.profiles){
            if (email.equals(p.email)){
                return "Email";
            }
            else if (username.equals(p.username)){
                return "Username";
            }
        }
        return "Correct";
    }

    private boolean loginChecker(String username, String password) {
        for (Profile p:Server.profiles){
            if (username.equals(p.username) && password.equals(p.password)){
                return true;
            }
        }
        return false;
    }

    private boolean loginChecker(String email, String password, boolean nothing) {
        for (Profile p:Server.profiles){
            if (email.equals(p.email) && password.equals(p.password)){
                return true;
            }
        }
        return false;
    }

    public static Profile profileFinder(String usernameOrEmail){
        for (Profile p:Server.profiles){
            if (isEmailValid(usernameOrEmail)){
                if (usernameOrEmail.equals(p.email)){
                    return p;
                }
            }
            else{
                if (usernameOrEmail.equals(p.username)){
                    return p;
                }
            }
        }
        return null;
    }

    private void refreshClientOwner(Profile profile) throws IOException
    {
        Server.serialize(profile);
        clientOutputStream.reset();
        clientOutputStream.writeObject(profile);
        clientOutputStream.flush();
    }
}
