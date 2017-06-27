package Client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by parsahejabi on 6/26/17.
 */
public class newsPageController implements Initializable {
    @FXML
    VBox newsVBox;


    ArrayList<News> news;

    public void goToProfile1() throws IOException, ClassNotFoundException {
        Client.clientOutputStream.writeUTF("Profile1");
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("profilePage1.fxml")));
        scene.getStylesheets().add("Client/style.css");
        ClientUI.sceneChanger(scene, "Profile");
    }
    public void goToHome() throws IOException, ClassNotFoundException {
        Client.clientOutputStream.writeUTF("Home");
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("homePage.fxml")));
        scene.getStylesheets().add("Client/style.css");
        ClientUI.sceneChanger(scene, "Home");
    }

    public void goToSearch() throws IOException, ClassNotFoundException {
        Client.clientOutputStream.writeUTF("Search");
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("searchPage.fxml")));
        scene.getStylesheets().add("Client/style.css");
        ClientUI.sceneChanger(scene, "Search");
    }

    public void goToShare() throws IOException, ClassNotFoundException{

        //this will nullify the readUTF in sharePage
        Client.clientOutputStream.writeUTF("Share");
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("sharePage.fxml")));
        scene.getStylesheets().add("Client/style.css");
        ClientUI.sceneChanger(scene, "Share");
    }

    public void goToNews() throws IOException, ClassNotFoundException{

        Client.clientOutputStream.writeUTF("#News");
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("newsPage.fxml")));
        scene.getStylesheets().add("Client/style.css");
        ClientUI.sceneChanger(scene, "Activity");
    }

    private void goToPeople(News n) {
        try {
            if (n.owner.username.equals(Client.profileOwner.username)){
                goToProfile1();
            }
            else{
                Client.clientOutputStream.writeUTF("#PeoplePage:"+n.owner.username);
                Client.clientOutputStream.flush();
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("viewPeoplePage.fxml")));
                scene.getStylesheets().add("Client/style.css");
                ClientUI.sceneChanger(scene, "People");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Client.refreshOwner();
            news = ((ArrayList<News>) Client.clientInputStream.readObject());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        int size = news.size();
        for (int i = size - 1; i>=0; i--){
            News n = news.get(i);
            System.out.println(n.owner.username + "  " + n.state);
            HBox newsHBox = new HBox(25);
            VBox newsDetailVBox = new VBox(10);
            newsDetailVBox.setAlignment(Pos.CENTER_LEFT);
            HBox newsDetailHBox = new HBox(2);
            newsDetailHBox.setAlignment(Pos.CENTER_LEFT);
            newsHBox.setPadding(new Insets(0));
            newsHBox.setAlignment(Pos.CENTER_LEFT);
            Circle newsOwnerProfilePicture = new Circle(30,new ImagePattern(new Image(n.owner.profilePicture.toURI().toString())));
            Hyperlink newsOwnerUsername = new Hyperlink(n.owner.username);
            newsOwnerUsername.setStyle("-fx-font-family: Helvetica;" +
                    "-fx-font-size: 17;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: black");
            newsOwnerUsername.setOnAction(event -> {
                goToPeople(n);
            });
            newsOwnerUsername.setPadding(new Insets(0));
            Label newsDateLabel = new Label(n.newsDate.toString());
            newsDateLabel.setTextFill(Color.web("#c7c7c7"));
            newsDateLabel.setStyle("-fx-font-family: Helvetica;" +
                    "-fx-font-size: 15;");
            //FollowNews, UnFollowNews, LikeNews, CommentNews
            if (n.state.equals("FollowNews")){
                Label newsLabel = new Label("started following you.");
                newsLabel.setWrapText(true);
                newsLabel.setStyle("-fx-font-family: Helvetica;" +
                        "-fx-font-size: 17;");
                Button followUnfollowButton = new Button();
                if (Client.profileOwner.following.contains(n.owner)){
                    followUnfollowButton.setText("Following");
                    followUnfollowButton.setStyle("-fx-background-color:#f4f4f4;" +
                            "-fx-text-fill:black;" +
                            "-fx-border-color:black;");
                    followUnfollowButton.setOnAction(event -> {
                        try {
                            Client.clientOutputStream.writeUTF("#FollowUnFollow:"+n.owner.username);
                            Client.clientOutputStream.flush();
                            Client.refreshOwner();
                            goToNews();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e)
                        {
                            e.printStackTrace();
                        }
                    });
                    newsDetailHBox.getChildren().addAll(newsOwnerUsername);
                    newsDetailVBox.getChildren().addAll(newsDetailHBox, newsLabel,newsDateLabel);
                    newsHBox.getChildren().addAll(newsOwnerProfilePicture, newsDetailVBox, new Label("          "),followUnfollowButton);
                }
                else {
                    followUnfollowButton.setText("Follow");
                    followUnfollowButton.setStyle("-fx-background-color:#3897f0;" +
                            "-fx-text-fill:white;" +
                            "-fx-border-color:#3897f0");
                    followUnfollowButton.setOnAction(event -> {
                        try {
                            Client.clientOutputStream.writeUTF("#FollowUnFollow:"+n.owner.username);
                            Client.clientOutputStream.flush();
                            Client.refreshOwner();
                            goToNews();

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e)
                        {
                            e.printStackTrace();
                        }
                    });
                    newsDetailHBox.getChildren().addAll(newsOwnerUsername);
                    newsDetailVBox.getChildren().addAll(newsDetailHBox, newsLabel,newsDateLabel);
                    newsHBox.getChildren().addAll(newsOwnerProfilePicture, newsDetailVBox, new Label("               "),followUnfollowButton);
                }
            }
            else if (n.state.equals("UnFollowNews")){
                Label newsLabel = new Label("unfollowed you.");
                newsLabel.setWrapText(true);
                newsLabel.setStyle("-fx-font-family: Helvetica;" +
                        "-fx-font-size: 17;");
                Button followUnfollowButton = new Button();
                if (Client.profileOwner.following.contains(n.owner)){
                    followUnfollowButton.setText("Following");
                    followUnfollowButton.setStyle("-fx-background-color:#f4f4f4;" +
                            "-fx-text-fill:black;" +
                            "-fx-border-color:black;");
                    followUnfollowButton.setOnAction(event -> {
                        try {
                            Client.clientOutputStream.writeUTF("#FollowUnFollow:"+n.owner.username);
                            Client.clientOutputStream.flush();
                            Client.refreshOwner();
                            goToNews();

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e)
                        {
                            e.printStackTrace();
                        }
                    });
                    newsDetailHBox.getChildren().addAll(newsOwnerUsername);
                    newsDetailVBox.getChildren().addAll(newsDetailHBox, newsLabel,newsDateLabel);
                    newsHBox.getChildren().addAll(newsOwnerProfilePicture, newsDetailVBox, new Label("          "),followUnfollowButton);
                }
                else {
                    followUnfollowButton.setText("Follow");
                    followUnfollowButton.setStyle("-fx-background-color:#3897f0;" +
                            "-fx-text-fill:white;" +
                            "-fx-border-color:#3897f0");
                    followUnfollowButton.setOnAction(event -> {
                        try {
                            Client.clientOutputStream.writeUTF("#FollowUnFollow:"+n.owner.username);
                            Client.clientOutputStream.flush();
                            Client.refreshOwner();
                            goToNews();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e)
                        {
                            e.printStackTrace();
                        }
                    });
                    newsDetailHBox.getChildren().addAll(newsOwnerUsername);
                    newsDetailVBox.getChildren().addAll(newsDetailHBox, newsLabel,newsDateLabel);
                    newsHBox.getChildren().addAll(newsOwnerProfilePicture, newsDetailVBox, new Label("               "),followUnfollowButton);
                }
            }
            else if (n.state.equals("LikeNews")){
                Label newsLabel = new Label("liked your post.");
                newsLabel.setWrapText(true);
                newsLabel.setStyle("-fx-font-family: Helvetica;" +
                        "-fx-font-size: 17;");
                ImageView postImageView = new ImageView(new Image(n.relatedPost.image.toURI().toString()));
                postImageView.setFitWidth(60);
                postImageView.setFitHeight(60);
                newsDetailHBox.getChildren().addAll(newsOwnerUsername, newsLabel);
                newsDetailVBox.getChildren().addAll(newsDetailHBox, newsDateLabel);
                newsHBox.getChildren().addAll(newsOwnerProfilePicture, newsDetailVBox, new Label("               "),postImageView);
            }
            else if (n.state.equals("CommentNews")){
                Label newsLabel = new Label("commented: ");
                newsLabel.setWrapText(true);
                newsLabel.setStyle("-fx-font-family: Helvetica;" +
                        "-fx-font-size: 17;");
                Label commentNewsLabel = new Label(n.commentCaption);
                commentNewsLabel.setWrapText(true);
                commentNewsLabel.setStyle("-fx-font-family: Helvetica;" +
                        "-fx-font-size: 17;");
                ImageView postImageView = new ImageView(new Image(n.relatedPost.image.toURI().toString()));
                postImageView.setFitWidth(60);
                postImageView.setFitHeight(60);
                newsDetailHBox.getChildren().addAll(newsOwnerUsername, newsLabel);
                newsDetailVBox.getChildren().addAll(newsDetailHBox, commentNewsLabel , newsDateLabel);
                newsHBox.getChildren().addAll(newsOwnerProfilePicture, newsDetailVBox, new Label("               "),postImageView);
            }
            newsVBox.getChildren().addAll(newsHBox);
        }
    }
}