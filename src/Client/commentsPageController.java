package Client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
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
 * Created by parsahejabi on 6/25/17.
 */
public class commentsPageController implements Initializable {

    private ArrayList<Comment> postComments;

    @FXML
    ListView<HBox> commentsListView;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Client.refreshOwner();
            postComments = (ArrayList<Comment>) Client.clientInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        for (Comment c:postComments){
            commentsListView.setPadding(new Insets(0));
            HBox comments = new HBox(10);
            comments.setPadding(new Insets(0));
            comments.setAlignment(Pos.CENTER_LEFT);
            VBox commentDetailVBox = new VBox(10);
            Circle commentOwnerProfilePicture = new Circle(35,new ImagePattern(new Image(c.owner.profilePicture.toURI().toString())));
            Hyperlink commentOwnerUsername = new Hyperlink(c.owner.username);
            commentOwnerUsername.setStyle("-fx-font-family: Helvetica;" +
                    "-fx-font-size: 17;" +
                    "-fx-font-weight: bold;" +
                    "-fx-color:black");
            commentOwnerUsername.setPadding(new Insets(0));
            Label commentLabel = new Label(c.commentText);
            commentLabel.setWrapText(true);
            commentLabel.setStyle("-fx-font-family: Helvetica;" +
                    "-fx-font-size: 17;");
            Label commentDateLabel = new Label(c.uploadDate.toString());
            commentDateLabel.setTextFill(Color.web("#c7c7c7"));
            commentDateLabel.setStyle("-fx-font-family: Helvetica;" +
                    "-fx-font-size: 15;");
            commentDetailVBox.getChildren().addAll(commentOwnerUsername, commentLabel, commentDateLabel);
            commentDetailVBox.getChildren().addAll(commentOwnerProfilePicture, commentDetailVBox);
            commentsListView.getItems().addAll(comments);
        }
    }
}