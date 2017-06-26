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
            Circle likedByOwnerProfilePicture = new Circle(35,new ImagePattern(new Image(p.profilePicture.toURI().toString())));
            Hyperlink likedByUsername = new Hyperlink(p.username);
            likedByUsername.setPadding(new Insets(0));
            likedByUsername.setStyle("-fx-font-family: Helvetica;" +
                    "-fx-font-size: 17;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: black;" +
                    "-fx-text-decoration: none;");
            Label likedByFullname = new Label(p.fullName);
            likedByFullname.setTextFill(Color.web("#c7c7c7"));
            likedByFullname.setStyle("-fx-font-family: Helvetica;" +
                    "-fx-font-size: 17;");
            likesHBox.getChildren().addAll(likedByOwnerProfilePicture, likedByUsername, likedByFullname);
            likesListView.getItems().addAll(likesHBox);
        }
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

    public void goToShare() throws IOException, ClassNotFoundException{

        //this will nullify the readUTF in sharePage
        Client.clientOutputStream.writeUTF("Share");
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("sharePage.fxml")));
        scene.getStylesheets().add("Stylesheet/style.css");
        ClientUI.sceneChanger(scene, "Share");
    }
}
