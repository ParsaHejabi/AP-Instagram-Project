package Client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by parsahejabi on 6/23/17.
 */
public class ViewPeoplePageController implements Initializable {
    static Profile requestedProfile;
    @FXML
    private Circle profilePicture;
    @FXML
    private Label fullName;
    @FXML
    private Label biography;
    @FXML
    private Label followingNum;
    @FXML
    private Label followerNum;
    @FXML
    private Label postNum;
    @FXML
    private Label username;
    @FXML
    private Button followUnfollowButton;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Client.refreshOwner();
            requestedProfile = ((Profile) Client.clientInputStream.readObject());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        profilePicture.setFill(new ImagePattern(new Image(requestedProfile.profilePicture.toURI().toString())));
        fullName.setText(requestedProfile.fullName);
        biography.setText(requestedProfile.biography);
        followerNum.setText(Integer.toString(requestedProfile.followers.size()));
        followingNum.setText(Integer.toString(requestedProfile.following.size()));
        postNum.setText(Integer.toString(requestedProfile.posts.size()));
        username.setText(requestedProfile.username);

        if (Client.profileOwner.following.contains(requestedProfile)){
            followUnfollowButton.setText("Following");
            followUnfollowButton.setStyle("-fx-background-color:#f4f4f4;" +
                    "-fx-text-fill:black;" +
                    "-fx-border-color:black;");
        }
        else {
            followUnfollowButton.setText("Follow");
            followUnfollowButton.setStyle("-fx-background-color:#3897f0;" +
                    "-fx-text-fill:white;" +
                    "-fx-border-color:#3897f0");
        }

        followUnfollowButton.setOnAction(event -> {
            try {
                Client.clientOutputStream.writeUTF("#FollowUnFollow:"+requestedProfile.username);
                Client.clientOutputStream.flush();
                Client.refreshOwner();
                goToPeople();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }

        });

    }

    public void goToProfile1() throws IOException, ClassNotFoundException {
        Client.clientOutputStream.writeUTF("Profile1");
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("profilePage1.fxml")));
        scene.getStylesheets().add("Stylesheet/style.css");
        ClientUI.sceneChanger(scene, "Profile");
    }
    public void goToHome() throws IOException, ClassNotFoundException {
        Client.clientOutputStream.writeUTF("Home");
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("homePage.fxml")));
        scene.getStylesheets().add("Stylesheet/style.css");
        ClientUI.sceneChanger(scene, "Home");
    }

    public void goToSearch() throws IOException, ClassNotFoundException {
        Client.clientOutputStream.writeUTF("Search");
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("searchPage.fxml")));
        scene.getStylesheets().add("Stylesheet/style.css");
        ClientUI.sceneChanger(scene, "Search");
    }

    private void goToPeople() throws IOException, ClassNotFoundException {

        Client.clientOutputStream.writeUTF("#PeoplePage:"+requestedProfile.username);
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("viewPeoplePage.fxml")));
        scene.getStylesheets().add("Stylesheet/style.css");
        ClientUI.sceneChanger(scene, "People");
    }

    public void goToShare() throws IOException, ClassNotFoundException{
        Client.clientOutputStream.writeUTF("Share");
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("sharePage.fxml")));
        scene.getStylesheets().add("Stylesheet/style.css");
        ClientUI.sceneChanger(scene, "Share");
    }
}