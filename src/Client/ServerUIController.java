package Client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by parsahejabi on 6/27/17.
 */
public class ServerUIController implements Initializable {

    @FXML
    ListView<HBox> onlineUsersListView;

    @FXML
    ListView<HBox> logListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
