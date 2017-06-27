package Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by parsahejabi on 6/27/17.
 */
public class ServerUI extends Application {
    static Stage primaryStage;
    static Scene mainScene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("ServerUI.fxml"));
        mainScene = new Scene(root);
        mainScene.getStylesheets().add("Client/style.css");
        primaryStage.setTitle("Server");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
