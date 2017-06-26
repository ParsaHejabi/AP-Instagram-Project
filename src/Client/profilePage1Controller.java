package Client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Movahed on 6/23/2017.
 */
public class profilePage1Controller implements Initializable{




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
    private ScrollPane postsScrollPane;
    private GridPane postsGridPane;
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


        postsScrollPane.setPadding(new Insets(0));
        postsGridPane = new GridPane();
        postsGridPane.setPadding(new Insets(0));
        postsGridPane.setVgap(1);
        postsGridPane.setHgap(1);
        int size = postsToShow.size();
        int remainder = size % 3;
        remainder += 3;
        for (int i = size - 1; i>=0; i--){
            ImageView imageView = new ImageView(new Image(postsToShow.get(i).image.toURI().toString()));
            imageView.setFitWidth(174);
            imageView.setFitHeight(174);
            if (((i+1) % 3) == (remainder % 3)){
                GridPane.setConstraints(imageView, 0, (size - (i+1)) / 3);
            }
            else if(((i+1) % 3) == ((remainder - 1) % 3)){
                GridPane.setConstraints(imageView, 1, (size - (i+1)) / 3);
            }
            else if (((i+1) % 3) == ((remainder - 2) % 3)){
                GridPane.setConstraints(imageView, 2, (size - (i+1)) / 3);
            }
            postsGridPane.getChildren().add(imageView);
        }
        postsScrollPane.setContent(postsGridPane);
    }

    public void goToShare() throws IOException, ClassNotFoundException{

        Client.clientOutputStream.writeUTF("Share");
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("sharePage.fxml")));
        scene.getStylesheets().add("Stylesheet/style.css");
        ClientUI.sceneChanger(scene, "Share");
    }
}
