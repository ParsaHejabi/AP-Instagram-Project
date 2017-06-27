package Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by parsahejabi on 6/22/17.
 */
public class ClientUI extends Application{
    static Stage primaryStage;
    static Scene mainScene;


    @Override
    public void start(Stage primaryStage)  {
        this.primaryStage = primaryStage;


        while (!Client.connect(Server.SERVER_PORT))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Server Outa reach");
            alert.setHeaderText("Oops!");
            alert.setContentText("It seems like our server is not available at the moment\n" +
                    "Try connecting again by clicking Ok or closing this window");
            alert.showAndWait();
        }
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("ClientUI.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mainScene = new Scene(root);
        mainScene.getStylesheets().add("Client/style.css");
        primaryStage.setTitle("Client");
        primaryStage.setScene(mainScene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {

            try {


                Client.clientOutputStream.writeUTF("Exit");
                Client.clientOutputStream.flush();
                Client.refreshOwner();
                Client.disconnect();


            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void sceneChanger(Scene scene, String title){
        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
    }
}