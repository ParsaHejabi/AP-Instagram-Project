package Client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

/**
 * Created by parsahejabi on 6/26/17.
 */
public class editPageController implements Initializable {

    @FXML
    private Circle previewProfilePictureCircle;
    @FXML
    private Hyperlink addAPhotoHyperLink;
    @FXML
    private TextArea biographyTextArea;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private TextField fullnameTextField;

    File ax = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Client.refreshOwner();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        previewProfilePictureCircle.setFill(new ImagePattern(new Image(Client.profileOwner.profilePicture.toURI().toString())));

        addAPhotoHyperLink.setOnAction(event -> {

            FileChooser chooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("image files (*.jpg)",
                    "*.jpg", "*.png", "*.jpeg" ,"*.gif");
            chooser.getExtensionFilters().add(extFilter);

            ax = chooser.showOpenDialog(null);

            if(ax == null)
            {
                previewProfilePictureCircle.setFill(new ImagePattern(new Image(Client.profileOwner.profilePicture.toURI().toString())));
            }
            else
            {
                previewProfilePictureCircle.setFill(new ImagePattern(new Image(Client.profileOwner.profilePicture.toURI().toString())));
            }
        });

        biographyTextArea.setText(Client.profileOwner.biography);
        biographyTextArea.setText(Client.profileOwner.fullName);

        passwordPasswordField.setPromptText("(Unchanged)");



    }

    public void edit() throws IOException {

        Client.clientOutputStream.writeUTF("#DoEdit");
        Client.clientOutputStream.flush();

        if(ax == null)
        {
            Client.clientOutputStream.writeUTF("#No");
            Client.clientOutputStream.flush();
        }
        else {
            Client.clientOutputStream.writeUTF("#Yes");
            Client.clientOutputStream.flush();
            Client.clientOutputStream.writeObject(Files.readAllBytes(ax.toPath()));
            Client.clientOutputStream.flush();
        }


        if(fullnameTextField.getText().equals(Client.profileOwner.fullName))
        {
            Client.clientOutputStream.writeUTF("#No");
            Client.clientOutputStream.flush();

        }
        else {
            Client.clientOutputStream.writeUTF("#Yes");
            Client.clientOutputStream.flush();
            Client.clientOutputStream.writeUTF(fullnameTextField.getText());
            Client.clientOutputStream.flush();
        }


        if(biographyTextArea.getText().equals(Client.profileOwner.biography))
        {
            Client.clientOutputStream.writeUTF("#No");
            Client.clientOutputStream.flush();
        }
        else {
            Client.clientOutputStream.writeUTF("#Yes");
            Client.clientOutputStream.flush();
            Client.clientOutputStream.writeUTF(biographyTextArea.getText());
            Client.clientOutputStream.flush();
        }


        if(passwordPasswordField.getText().isEmpty())
        {
            Client.clientOutputStream.writeUTF("#No");
            Client.clientOutputStream.flush();
        }
        else {
            Client.clientOutputStream.writeUTF("#Yes");
            Client.clientOutputStream.flush();
            Client.clientOutputStream.writeUTF(passwordPasswordField.getText());
            Client.clientOutputStream.flush();
        }

        Client.clientOutputStream.writeUTF("Profile1");
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("profilePage1.fxml")));
        scene.getStylesheets().add("Stylesheet/style.css");
        ClientUI.sceneChanger(scene, "Profile");
    }

    public void goToProfile1() throws IOException, ClassNotFoundException {
        Client.clientOutputStream.writeUTF("fake command");
        Client.clientOutputStream.flush();
        //this will nullify the readUTF in sharePage
        Client.clientOutputStream.writeUTF("Profile1");
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("profilePage1.fxml")));
        scene.getStylesheets().add("Stylesheet/style.css");
        ClientUI.sceneChanger(scene, "Profile");
    }


    public void goToHome() throws IOException, ClassNotFoundException {
        Client.clientOutputStream.writeUTF("fake command");
        Client.clientOutputStream.flush();
        //this will nullify the readUTF in sharePage
        Client.clientOutputStream.writeUTF("Home");
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("homePage.fxml")));
        scene.getStylesheets().add("Stylesheet/style.css");
        ClientUI.sceneChanger(scene, "Home");
    }

    public void goToSearch() throws IOException, ClassNotFoundException {
        Client.clientOutputStream.writeUTF("fake command");
        Client.clientOutputStream.flush();
        //this will nullify the readUTF in sharePage
        Client.clientOutputStream.writeUTF("Search");
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("searchPage.fxml")));
        scene.getStylesheets().add("Stylesheet/style.css");
        ClientUI.sceneChanger(scene, "Search");
    }

    public void goToShare() throws IOException, ClassNotFoundException{
        Client.clientOutputStream.writeUTF("fake command");
        Client.clientOutputStream.flush();
        //this will nullify the readUTF in sharePage
        Client.clientOutputStream.writeUTF("Share");
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("sharePage.fxml")));
        scene.getStylesheets().add("Stylesheet/style.css");
        ClientUI.sceneChanger(scene, "Share");
    }

    public void goToNews() throws IOException, ClassNotFoundException{
        Client.clientOutputStream.writeUTF("fake command");
        Client.clientOutputStream.flush();
        Client.clientOutputStream.writeUTF("#News");
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("newsPage.fxml")));
        scene.getStylesheets().add("Stylesheet/style.css");
        ClientUI.sceneChanger(scene, "Activity");
    }

}