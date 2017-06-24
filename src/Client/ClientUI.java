package Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * Created by parsahejabi on 6/22/17.
 */
public class ClientUI extends Application{
    static Stage primaryStage;
    static Scene mainScene;


    @Override
    public void start(Stage primaryStage) throws Exception {
        Client.connect(Server.SERVER_PORT);
        this.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("ClientUI.fxml"));
        mainScene = new Scene(root);
        mainScene.getStylesheets().add("Stylesheet/style.css");
        primaryStage.setTitle("Client");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void sceneChanger(Scene scene, String title){
        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
    }
}