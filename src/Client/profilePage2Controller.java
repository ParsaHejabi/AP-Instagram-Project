package Client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by parsahejabi on 6/24/17.
 */
public class profilePage2Controller implements Initializable {
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
    ListView<VBox> posts;
    private ArrayList<Post> postsToShow;

    public void goToProfile1() throws IOException, ClassNotFoundException {
        Client.clientOutputStream.writeUTF("Profile1");
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("profilePage1.fxml")));
        scene.getStylesheets().add("Stylesheet/style.css");
        ClientUI.sceneChanger(scene, "Profile");
    }
    public void goToProfile2() throws IOException, ClassNotFoundException
    {
        Client.clientOutputStream.writeUTF("Profile2");
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("profilePage2.fxml")));
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

    public void goToShare() throws IOException, ClassNotFoundException{

        Client.clientOutputStream.writeUTF("Share");
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("sharePage.fxml")));
        scene.getStylesheets().add("Stylesheet/style.css");
        ClientUI.sceneChanger(scene, "Share");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Client.refreshOwner();
            postsToShow = (ArrayList<Post>) Client.clientInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        profilePicture.setFill(new ImagePattern(new Image(Client.profileOwner.profilePicture.toURI().toString())));
        fullName.setText(Client.profileOwner.fullName);
        biography.setText(Client.profileOwner.biography);
        followerNum.setText(Integer.toString(Client.profileOwner.followers.size()));
        followingNum.setText(Integer.toString(Client.profileOwner.following.size()));
        postNum.setText(Integer.toString(Client.profileOwner.posts.size()));
        username.setText(Client.profileOwner.username);
        int size = postsToShow.size();
        for (int i=size-1; i>=0; i--){
            posts.setPadding(new Insets(0));
            VBox post = new VBox(10);
            post.setPadding(new Insets(0));
            HBox postOwnerHBox = new HBox(10);
            postOwnerHBox.setAlignment(Pos.CENTER_LEFT);
            Circle postOwnerProfilePicture = new Circle(20,new ImagePattern(new Image(postsToShow.get(i).owner.profilePicture.toURI().toString())));
            Hyperlink postOwnerUsername = new Hyperlink(postsToShow.get(i).owner.username);
            postOwnerUsername.setPadding(new Insets(0));
            postOwnerHBox.getChildren().addAll(postOwnerProfilePicture,postOwnerUsername);
            ImageView postImageView = new ImageView(new Image(postsToShow.get(i).image.toURI().toString()));
            postImageView.setStyle("-fx-padding:0;");
            postImageView.setFitWidth(500);
            postImageView.setFitHeight(500);
            HBox postButtonsHBox = new HBox(10);
            postButtonsHBox.setAlignment(Pos.CENTER_LEFT);
            ImageView likeButtonImageView = new ImageView("Client/Assets/likeButton.png");
            likeButtonImageView.setFitWidth(45);
            likeButtonImageView.setFitHeight(45);
            if (postsToShow.get(i).canComment){
                ImageView commentButtonImageView = new ImageView("Client/Assets/commentButton.png");
                commentButtonImageView.setFitWidth(45);
                commentButtonImageView.setFitHeight(45);
                postButtonsHBox.getChildren().addAll(likeButtonImageView, commentButtonImageView);
            }
            else {
                postButtonsHBox.getChildren().add(likeButtonImageView);
            }
            Hyperlink likesHyperlink = new Hyperlink(postsToShow.get(i).liked.size() + " likes");
            likesHyperlink.setPadding(new Insets(0));
            Hyperlink postOwnerUsernameHyperLink = new Hyperlink(postsToShow.get(i).owner.username);
            postOwnerUsernameHyperLink.setPadding(new Insets(0));
            Label postCaptionTextArea = new Label(postsToShow.get(i).caption);
            //TODO age khastim ye kari konim ke date alano begire menhaye oon moghe kone
            Label postDateLabel = new Label(postsToShow.get(i).uploadDate.toString());
            if (!postCaptionTextArea.getText().isEmpty()){
                if (postsToShow.get(i).canComment){
                    Hyperlink commentHyperlink = new Hyperlink("View all " + postsToShow.get(i).comments.size() + " commentsListView");
                    post.getChildren().addAll(postOwnerHBox, postImageView, postButtonsHBox, likesHyperlink, postOwnerUsernameHyperLink, postCaptionTextArea, commentHyperlink, postDateLabel);
                }
                else{
                    post.getChildren().addAll(postOwnerHBox, postImageView, postButtonsHBox, likesHyperlink, postOwnerUsernameHyperLink, postCaptionTextArea, postDateLabel);
                }
            }
            else{
                if (postsToShow.get(i).canComment){
                    Hyperlink commentHyperlink = new Hyperlink("View all " + postsToShow.get(i).comments.size() + " commentsListView");
                    post.getChildren().addAll(postOwnerHBox, postImageView, postButtonsHBox, likesHyperlink, postOwnerUsernameHyperLink, commentHyperlink, postDateLabel);
                }
                else{
                    post.getChildren().addAll(postOwnerHBox, postImageView, postButtonsHBox, likesHyperlink, postOwnerUsernameHyperLink, postDateLabel);
                }
            }
            posts.getItems().addAll(post);
        }
    }
}
