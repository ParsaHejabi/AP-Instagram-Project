package Client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Client.Client.clientInputStream;
import static Client.Client.clientOutputStream;

public class ClientUIController {
    @FXML
    Button goToSignUpButton;
    @FXML
    Hyperlink goToLoginHyperLink;
    @FXML
    TextField emailTextField;
    @FXML
    TextField usernameTextField;
    @FXML
    PasswordField passwordPasswordField;
    @FXML
    TextField fullnameTextField;
    @FXML
    TextArea biographyTextArea;
    @FXML
    Button finishSignUpButton;
    @FXML
    TextField emailOrUsernameLoginTextField;
    @FXML
    PasswordField passwordLoginPasswordField;
    @FXML
    Button loginButton;

    File picture;

    public void goToLoginPage() {
        Scene scene = null;
        try {
            scene = new Scene(FXMLLoader.load(getClass().getResource("loginPage.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        scene.getStylesheets().add("Stylesheet/style.css");
        ClientUI.sceneChanger(scene, "Login");
    }

    public void goToSignUpPage() {
        Scene scene = null;
        try {
            scene = new Scene(FXMLLoader.load(getClass().getResource("signupPage.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        scene.getStylesheets().add("Stylesheet/style.css");
        ClientUI.sceneChanger(scene, "Sign Up");
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static boolean isEmailValid(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }
    public void signUp() throws IOException {
        String email = emailTextField.getText();
        String username = usernameTextField.getText();
        String password = passwordPasswordField.getText();
        if (isEmailValid(email)){
            Client.clientOutputStream.writeUTF("Signup");
            Client.clientOutputStream.flush();
            Client.clientOutputStream.writeUTF(email);
            Client.clientOutputStream.flush();
            Client.clientOutputStream.writeUTF(username);
            Client.clientOutputStream.flush();
            Client.clientOutputStream.writeUTF(password);
            Client.clientOutputStream.flush();
            String serverResponse = clientInputStream.readUTF();
            if (serverResponse.equals("Correct")){
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("signupPage2.fxml")));
                scene.getStylesheets().add("Stylesheet/style.css");
                ClientUI.sceneChanger(scene, "Sign Up");
            }
            else if (serverResponse.equals("Username")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Oops!");
                alert.setContentText("this username exists!");
                alert.showAndWait();
            }
            else if (serverResponse.equals("Email")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Oops!");
                alert.setContentText("this email exists!");
                alert.showAndWait();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Hmmm...");
            alert.setContentText("Email is invalid!");
            alert.showAndWait();
        }
    }
    public void signUp2() throws IOException, ClassNotFoundException {
        String fullname = fullnameTextField.getText();
        String biography = biographyTextArea.getText();
        Client.clientOutputStream.writeUTF(fullname);
        Client.clientOutputStream.flush();
        Client.clientOutputStream.writeUTF(biography);
        Client.clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("signupPage3.fxml")));
        scene.getStylesheets().add("Stylesheet/style.css");
        ClientUI.sceneChanger(scene, "Set ProfilePicture");

    }

    public void selectPicture()
    {
        FileChooser fileChooser = new FileChooser();
        picture = fileChooser.showOpenDialog(null);

    }

    public void signUp3() throws IOException, ClassNotFoundException {
        if(picture != null)
        {
            Client.clientOutputStream.writeUTF("Pic");
            Client.clientOutputStream.flush();
            Client.clientOutputStream.writeObject(Files.readAllBytes(picture.toPath()));
            Client.clientOutputStream.flush();
            Client.profileOwner = ((Profile) Client.clientInputStream.readObject());
        }
        else {
            Client.clientOutputStream.writeUTF("Skip");
            Client.clientOutputStream.flush();
            Client.profileOwner = ((Profile) Client.clientInputStream.readObject());

        }
        clientOutputStream.writeUTF("Home");
        clientOutputStream.flush();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("homePage.fxml")));
        scene.getStylesheets().add("Stylesheet/style.css");
        ClientUI.sceneChanger(scene, "Home");
    }
    public void logIn() throws IOException, ClassNotFoundException {
        String usernameOrEmail = emailOrUsernameLoginTextField.getText();
        String password = passwordLoginPasswordField.getText();
        Client.clientOutputStream.writeUTF("Login");
        Client.clientOutputStream.flush();
        Client.clientOutputStream.writeUTF(usernameOrEmail);
        Client.clientOutputStream.flush();
        Client.clientOutputStream.writeUTF(password);
        Client.clientOutputStream.flush();
        boolean serverResponse = Client.clientInputStream.readBoolean();
        if (serverResponse){
            Client.profileOwner = (Profile) clientInputStream.readObject();
            clientOutputStream.writeUTF("Home");
            clientOutputStream.flush();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("homePage.fxml")));
            scene.getStylesheets().add("Stylesheet/style.css");
            ClientUI.sceneChanger(scene, "Home");
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Oops!");
            alert.setContentText("email or username or password is incorrect!");
            alert.showAndWait();
        }
    }
}
