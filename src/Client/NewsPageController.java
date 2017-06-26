package Client;

import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by parsahejabi on 6/26/17.
 */
public class NewsPageController implements Initializable {

    ArrayList<News> news;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Client.refreshOwner();
            news = ((ArrayList<News>) Client.clientInputStream.readObject());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        for (News n : news)
        {

        }
    }
}
