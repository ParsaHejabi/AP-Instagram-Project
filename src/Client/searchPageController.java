package Client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Movahed on 6/23/2017.
 */
public class searchPageController implements Initializable{
    @FXML
    TextField searchField;
    @FXML
    ListView<HBox> searchResult;

    static ArrayList<Profile> searchedProfile;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        try {
            Client.refreshOwner();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            Client.sendMessage("SearchProfile:"+newValue);
            if(newValue.length()>2) {
                try {

                    searchedProfile = ((ArrayList<Profile>) Client.clientInputStream.readObject());
                    showSearchRes();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else {
                searchResult.getItems().clear();
            }
        });

    }
    public void showSearchRes()
    {
        searchResult.getItems().clear();

        for (Profile p : searchedProfile) {
            HBox hBox = new HBox(35);
            hBox.setAlignment(Pos.CENTER_LEFT);
            Circle profilePicture = new Circle(50, new ImagePattern(new Image(p.profilePicture.toURI().toString())));
            Hyperlink usernameHyperLink = new Hyperlink(p.username);
            usernameHyperLink.setStyle("-fx-font-family: Helvetica;" +
                    "-fx-font-size: 20;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: black;" +
                    "-fx-text-decoration: none;");
            usernameHyperLink.setAlignment(Pos.CENTER_LEFT);
            usernameHyperLink.setOnAction(event -> {
                try {

                    goToPeople(p);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
            Label fullnameLabel = new Label(p.fullName);
            fullnameLabel.setAlignment(Pos.CENTER_LEFT);
            fullnameLabel.setStyle("-fx-font-family: Helvetica;" +
                    "-fx-font-size: 15;");
            hBox.getChildren().addAll(profilePicture, usernameHyperLink, fullnameLabel);
            searchResult.getItems().add(hBox);

        }
    }

    private void goToPeople(Profile p) throws IOException, ClassNotFoundException {
        Client.clientOutputStream.writeUTF("Exit");
        Client.clientOutputStream.writeUTF("#PeoplePage:" + p.username);
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("viewPeoplePage.fxml")));
        scene.getStylesheets().add("Client/style.css");
        ClientUI.sceneChanger(scene, "People");
    }

    public void goToHome() throws IOException, ClassNotFoundException {
        Client.clientOutputStream.writeUTF("Exit");
        Client.clientOutputStream.writeUTF("Home");
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("homePage.fxml")));
        scene.getStylesheets().add("Client/style.css");
        ClientUI.sceneChanger(scene, "Home");
    }

    public void goToProfile1() throws IOException, ClassNotFoundException {
        Client.clientOutputStream.writeUTF("Exit");
        Client.clientOutputStream.writeUTF("Profile1");
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("profilePage1.fxml")));
        scene.getStylesheets().add("Client/style.css");
        ClientUI.sceneChanger(scene, "Profile");
    }

    public void goToShare() throws IOException, ClassNotFoundException{
        Client.clientOutputStream.writeUTF("Exit");
        Client.clientOutputStream.writeUTF("Share");
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("sharePage.fxml")));
        scene.getStylesheets().add("Client/style.css");
        ClientUI.sceneChanger(scene, "Share");
    }

    public void goToNews() throws IOException, ClassNotFoundException{
        Client.clientOutputStream.writeUTF("Exit");
        Client.clientOutputStream.writeUTF("#News");
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("newsPage.fxml")));
        scene.getStylesheets().add("Client/style.css");
        ClientUI.sceneChanger(scene, "Activity");
    }
}
