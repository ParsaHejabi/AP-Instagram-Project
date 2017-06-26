package Client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by parsahejabi on 6/25/17.
 */
public class sharePageController implements Initializable {

    @FXML
    private Label photoAddressLabel;
    @FXML
    private Button addAPhotoButton;
    @FXML
    private TextArea captionTextArea;
    @FXML
    private ImageView previewImageView;
    @FXML
    private Pane toggleSwitchPane;

    ToggleSwitch canCommentSwitch;


    File ax;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Client.refreshOwner();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        canCommentSwitch = new ToggleSwitch();
        toggleSwitchPane.getChildren().add(canCommentSwitch);


        addAPhotoButton.setOnAction(event -> {
            FileChooser chooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("image files (*.jpg)",
                    "*.jpg", "*.png", "*.jpeg" ,"*.gif");
            chooser.getExtensionFilters().add(extFilter);
            ax = chooser.showOpenDialog(null);
            if(ax != null)
            {
                previewImageView.setImage(new Image(ax.toURI().toString()));
                photoAddressLabel.setText(ax.getAbsolutePath());
            }

        });


    }

    public void share()throws IOException, ClassNotFoundException{
        if(ax == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setHeaderText("Hmmm...");
            alert.setContentText("It seems like you haven't choose a picture for upload");
            alert.showAndWait();
        }
        else {

            String caption = captionTextArea.getText();

            Client.clientOutputStream.writeUTF("SharePost");
            Client.clientOutputStream.flush();



            Client.clientOutputStream.reset();
            Client.clientOutputStream.writeObject(ax);
            Client.clientOutputStream.flush();

            Client.clientOutputStream.reset();
            Client.clientOutputStream.writeUTF(caption);
            Client.clientOutputStream.flush();

            Client.clientOutputStream.reset();
            Client.clientOutputStream.writeBoolean(!canCommentSwitch.switchedOnProperty().getValue());
            //Todo: boolean isOn ToggleSwitch ro befres
            Client.clientOutputStream.flush();


            //this will send to Client Handler that i want to share a post
            Client.clientOutputStream.writeUTF("Home");
            Client.clientOutputStream.flush();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("homePage.fxml")));
            scene.getStylesheets().add("Client/style.css");
            ClientUI.sceneChanger(scene, "Home");
        }
    }

    public void goToProfile1() throws IOException, ClassNotFoundException {
        Client.clientOutputStream.writeUTF("fake command");
        Client.clientOutputStream.flush();
        //this will nullify the readUTF in sharePage
        Client.clientOutputStream.writeUTF("Profile1");
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("profilePage1.fxml")));
        scene.getStylesheets().add("Client/style.css");
        ClientUI.sceneChanger(scene, "Profile");
    }


    public void goToHome() throws IOException, ClassNotFoundException {
        Client.clientOutputStream.writeUTF("fake command");
        Client.clientOutputStream.flush();
        //this will nullify the readUTF in sharePage
        Client.clientOutputStream.writeUTF("Home");
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("homePage.fxml")));
        scene.getStylesheets().add("Client/style.css");
        ClientUI.sceneChanger(scene, "Home");
    }

    public void goToSearch() throws IOException, ClassNotFoundException {
        Client.clientOutputStream.writeUTF("fake command");
        Client.clientOutputStream.flush();
        //this will nullify the readUTF in sharePage
        Client.clientOutputStream.writeUTF("Search");
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("searchPage.fxml")));
        scene.getStylesheets().add("Client/style.css");
        ClientUI.sceneChanger(scene, "Search");
    }

    public void goToShare() throws IOException, ClassNotFoundException{
        Client.clientOutputStream.writeUTF("fake command");
        Client.clientOutputStream.flush();
        //this will nullify the readUTF in sharePage
        Client.clientOutputStream.writeUTF("Share");
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("sharePage.fxml")));
        scene.getStylesheets().add("Client/style.css");
        ClientUI.sceneChanger(scene, "Share");
    }

    public void goToNews() throws IOException, ClassNotFoundException{
        Client.clientOutputStream.writeUTF("fake command");
        Client.clientOutputStream.flush();
        Client.clientOutputStream.writeUTF("#News");
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("newsPage.fxml")));
        scene.getStylesheets().add("Client/style.css");
        ClientUI.sceneChanger(scene, "Activity");
    }
}
