package Client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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
public class likesPageController implements Initializable {

    private ArrayList<Profile> postLikes;

    @FXML
    ListView<HBox> likesListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Client.refreshOwner();
            postLikes = (ArrayList<Profile>) Client.clientInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        for (Profile p: postLikes){
            likesListView.setPadding(new Insets(0));
            HBox likesHBox = new HBox(10);
            likesHBox.setPadding(new Insets(0));
            likesHBox.setAlignment(Pos.CENTER_LEFT);
            Circle commentOwnerProfilePicture = new Circle(35,new ImagePattern(new Image(p.profilePicture.toURI().toString())));
            VBox likesMiddlePartVBox = new VBox(5);
            Hyperlink likedByUsername = new Hyperlink(p.username);
            likedByUsername.setPadding(new Insets(0));
            likedByUsername.setStyle("-fx-font-family: Helvetica;" +
                    "-fx-font-size: 17;" +
                    "-fx-font-weight: bold;" +
                    "-fx-color:black");
            Label likedByFullname = new Label(p.fullName);
            likedByFullname.setTextFill(Color.web("#c7c7c7"));
            likedByFullname.setStyle("-fx-font-family: Helvetica;" +
                    "-fx-font-size: 17;");
            likesMiddlePartVBox.getChildren().addAll(likedByUsername, likedByFullname);
        }
    }
}
