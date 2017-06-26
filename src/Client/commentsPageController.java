package Client;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by parsahejabi on 6/25/17.
 */
public class commentsPageController implements Initializable {

    private ArrayList<Comment> postComments;
    private String postOwnerUsername;
    private String postId;

    @FXML
    ListView<HBox> commentsListView;

    @FXML
    Pane sendCommentPane;

    @FXML
    TextField commentTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Client.refreshOwner();
            postComments = (ArrayList<Comment>) Client.clientInputStream.readObject();
            postOwnerUsername = Client.clientInputStream.readUTF();
            postId = Client.clientInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        commentsListView.setPadding(new Insets(0));
        int size = postComments.size();
        for (int i=size - 1; i>=0; i--){
            HBox comments = new HBox(10);
            comments.setPadding(new Insets(0));
            comments.setAlignment(Pos.CENTER_LEFT);
            VBox commentDetailVBox = new VBox(10);
            Circle commentOwnerProfilePicture = new Circle(35,new ImagePattern(new Image(postComments.get(i).owner.profilePicture.toURI().toString())));
            Hyperlink commentOwnerUsername = new Hyperlink(postComments.get(i).owner.username);
            commentOwnerUsername.setStyle("-fx-font-family: Helvetica;" +
                    "-fx-font-size: 17;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: black;" +
                    "-fx-text-decoration: none;");
            commentOwnerUsername.setPadding(new Insets(0));
            Label commentLabel = new Label(postComments.get(i).commentText);
            commentLabel.setWrapText(true);
            commentLabel.setStyle("-fx-font-family: Helvetica;" +
                    "-fx-font-size: 17;");
            Label commentDateLabel = new Label(postComments.get(i).uploadDate.toString());
            commentDateLabel.setTextFill(Color.web("#c7c7c7"));
            commentDateLabel.setStyle("-fx-font-family: Helvetica;" +
                    "-fx-font-size: 15;");
            commentDetailVBox.getChildren().addAll(commentOwnerUsername, commentLabel, commentDateLabel);
            comments.getChildren().addAll(commentOwnerProfilePicture, commentDetailVBox);
            commentsListView.getItems().addAll(comments);
        }
        sendCommentPane.setOnMouseClicked((MouseEvent event) -> {
            try {
                if (commentTextField.getText().isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Hmmm...");
                    alert.setContentText("There is no comment to post!");
                }
                else{
                    sendComment();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private void sendComment() throws IOException, ClassNotFoundException {
        Client.clientOutputStream.writeUTF("SendComment:" + postOwnerUsername + ":" + postId);
        Client.clientOutputStream.flush();
        Client.clientOutputStream.writeUTF(commentTextField.getText());
        Client.clientOutputStream.flush();
        Client.refreshOwner();
        Client.clientOutputStream.writeUTF("ViewComments:" + postOwnerUsername + ":" + postId);
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("commentsPage.fxml")));
        scene.getStylesheets().add("Stylesheet/style.css");
        ClientUI.sceneChanger(scene, "Comments");
    }

    public void goToHome() throws IOException, ClassNotFoundException {
        Client.clientOutputStream.writeUTF("Home");
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("homePage.fxml")));
        scene.getStylesheets().add("Stylesheet/style.css");
        ClientUI.sceneChanger(scene, "Home");
    }
}