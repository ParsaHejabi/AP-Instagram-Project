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
import javafx.scene.input.MouseEvent;
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

    public void goToNews() throws IOException, ClassNotFoundException{

        Client.clientOutputStream.writeUTF("#News");
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("newsPage.fxml")));
        scene.getStylesheets().add("Stylesheet/style.css");
        ClientUI.sceneChanger(scene, "Activity");
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
        for (int i= size -1; i>=0; i--){
            Post p = postsToShow.get(i);
            posts.setPadding(new Insets(0));
            VBox post = new VBox(10);
            post.setPadding(new Insets(0));
            HBox postOwnerHBox = new HBox(10);
            postOwnerHBox.setAlignment(Pos.CENTER_LEFT);
            Circle postOwnerProfilePicture = new Circle(20,new ImagePattern(new Image(p.owner.profilePicture.toURI().toString())));
            Hyperlink postOwnerUsername = new Hyperlink(p.owner.username);
            postOwnerUsername.setOnAction(event -> {
                try {
                    goToProfile2();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
            postOwnerUsername.setStyle(("-fx-font-family: Helvetica;" +
                    "-fx-font-size: 17;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: black;" +
                    "-fx-text-decoration: none;"));
            postOwnerUsername.setPadding(new Insets(0));
            postOwnerHBox.getChildren().addAll(postOwnerProfilePicture,postOwnerUsername);
            ImageView postImageView = new ImageView(new Image(p.image.toURI().toString()));
            postImageView.setStyle("-fx-padding: 0;");
            postImageView.setFitWidth(500);
            postImageView.setFitHeight(500);
            HBox postButtonsHBox = new HBox(10);
            postButtonsHBox.setAlignment(Pos.CENTER_LEFT);
            ImageView likeButtonImageView = new ImageView("Client/Assets/likeButton.png");
            likeButtonImageView.setPickOnBounds(true);
            likeButtonImageView.setFitWidth(45);
            likeButtonImageView.setFitHeight(45);
            if (p.canComment){
                ImageView commentButtonImageView = new ImageView("Client/Assets/commentButton.png");
                commentButtonImageView.setPickOnBounds(true);
                commentButtonImageView.setFitWidth(45);
                commentButtonImageView.setFitHeight(45);
                commentButtonImageView.setOnMouseClicked((MouseEvent event) -> {
                    String command = "ViewComments:"+p.owner.username+":"+p.id;

                    try {
                        Client.clientOutputStream.writeUTF(command);
                        Client.clientOutputStream.flush();
                        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("commentsPage.fxml")));
                        scene.getStylesheets().add("Stylesheet/style.css");
                        ClientUI.sceneChanger(scene, "Comments");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    event.consume();

                });
                postButtonsHBox.getChildren().addAll(likeButtonImageView, commentButtonImageView);
            }
            else {
                postButtonsHBox.getChildren().add(likeButtonImageView);
            }
            Hyperlink likesHyperlink = new Hyperlink(p.liked.size() + " likes");
            likesHyperlink.setStyle(("-fx-font-family: Helvetica;" +
                    "-fx-font-size: 17;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: black;" +
                    "-fx-text-decoration: none;"));
            likesHyperlink.setOnAction(event -> {
                String command = "ViewLikes:"+p.owner.username+":"+p.id;

                try {
                    Client.clientOutputStream.writeUTF(command);
                    Client.clientOutputStream.flush();
                    Scene scene = new Scene(FXMLLoader.load(getClass().getResource("likesPage.fxml")));
                    scene.getStylesheets().add("Stylesheet/style.css");
                    ClientUI.sceneChanger(scene, "Likes");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            likesHyperlink.setPadding(new Insets(0));
            Hyperlink postOwnerUsernameHyperLink = new Hyperlink(p.owner.username);
            postOwnerUsernameHyperLink.setStyle(("-fx-font-family: Helvetica;" +
                    "-fx-font-size: 17;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: black;" +
                    "-fx-text-decoration: none;"));
            postOwnerUsernameHyperLink.setPadding(new Insets(0));
            Label postCaptionTextArea = new Label(p.caption);
            //TODO age khastim ye kari konim ke date alano begire menhaye oon moghe kone
            Label postDateLabel = new Label(p.uploadDate.toString());
            if (!postCaptionTextArea.getText().isEmpty()){
                if (p.canComment){
                    Hyperlink commentHyperlink = new Hyperlink("View all " + p.comments.size() + " comments");
                    commentHyperlink.setStyle(("-fx-font-family: Helvetica;" +
                            "-fx-font-size: 17;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: black;" +
                            "-fx-text-decoration: none;"));
                    commentHyperlink.setOnAction(event -> {
                        String command = "ViewComments:"+p.owner.username+":"+p.id;

                        try {
                            Client.clientOutputStream.writeUTF(command);
                            Client.clientOutputStream.flush();
                            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("commentsPage.fxml")));
                            scene.getStylesheets().add("Stylesheet/style.css");
                            ClientUI.sceneChanger(scene, "Comments");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    post.getChildren().addAll(postOwnerHBox, postImageView, postButtonsHBox, likesHyperlink, postOwnerUsernameHyperLink, postCaptionTextArea, commentHyperlink, postDateLabel);
                }
                else{
                    post.getChildren().addAll(postOwnerHBox, postImageView, postButtonsHBox, likesHyperlink, postOwnerUsernameHyperLink, postCaptionTextArea, postDateLabel);
                }
            }
            else{
                if (p.canComment){
                    Hyperlink commentHyperlink = new Hyperlink("View all " + p.comments.size() + " comments");
                    commentHyperlink.setStyle(("-fx-font-family: Helvetica;" +
                            "-fx-font-size: 17;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: black;" +
                            "-fx-text-decoration: none;"));
                    commentHyperlink.setOnAction(event -> {
                        String command = "ViewComments:"+p.owner.username+":"+p.id;

                        try {
                            Client.clientOutputStream.writeUTF(command);
                            Client.clientOutputStream.flush();
                            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("commentsPage.fxml")));
                            scene.getStylesheets().add("Stylesheet/style.css");
                            ClientUI.sceneChanger(scene, "Comments");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    post.getChildren().addAll(postOwnerHBox, postImageView, postButtonsHBox, likesHyperlink, postOwnerUsernameHyperLink, commentHyperlink, postDateLabel);
                }
                else{
                    post.getChildren().addAll(postOwnerHBox, postImageView, postButtonsHBox, likesHyperlink, postOwnerUsernameHyperLink, postDateLabel);
                }
            }
            likeButtonImageView.setOnMouseClicked((MouseEvent event) ->{
                String command = "Like:"+p.owner.username+":"+p.id;

                try {
                    Client.clientOutputStream.writeUTF(command);
                    Client.refreshOwner();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                }

                if(p.liked.contains(Client.profileOwner))
                {
                    p.liked.remove(Client.profileOwner);
                    likesHyperlink.setText(p.liked.size() + " likes");
                }
                else {
                    p.liked.add((Client.profileOwner));
                    likesHyperlink.setText(p.liked.size()+ " likes");
                }

                //todo: axesh avaz she vaghti like kard;
                event.consume();

            });
            posts.getItems().addAll(post);
        }
    }
}
