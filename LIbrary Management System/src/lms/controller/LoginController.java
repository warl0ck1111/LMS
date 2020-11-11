package lms.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import lms.model.*;
import lms.Main;

public class LoginController implements Initializable {

    final private Image[] images = new Image[7];
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Pagination pagination;

    @FXML
    public void loginButtonHandler(ActionEvent e) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        User user = Main.usersMemory.get(username);

        if (user != null && user.getPassword().equals(password)) {
            if (user.getClass() == LibraryUser.class) {
                LibraryUser libryUser = (LibraryUser) user;
                if (libryUser.getStatus() == LibraryUser.Status.SUSPENDED) {
                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setContentText("Sorry you cant use the Application\nBecause you are Currently Suspended Contact the Librarian to Clear you !!!");
                    info.getDialogPane().getStylesheets().add(Main.class.getResource("style/theme.css").toExternalForm());
                    info.showAndWait();
                    return;
                }
            }
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Main.loggedUser = user;
            stage.setScene(changeScene(user));
        } else {
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setContentText("Invalid Username or Password!!!");
            info.getDialogPane().getStylesheets().add(Main.class.getResource("style/theme.css").toExternalForm());
            info.showAndWait();

        }
    }

    private Scene changeScene(User user) throws IOException {
        String activityNumber = String.valueOf(Main.journalMemory.size() + 1);
        Activity activity;

        Scene scene;
        if (user.getClass() == Librarian.class) {
            scene = new Scene(FXMLLoader.load(Main.class.getResource("view/librarianMenu.fxml")));
            activity = new Activity(activityNumber, user.getUid(), "None", "None", "Librarian Logged in");
        } else {
            scene = new Scene(FXMLLoader.load(Main.class.getResource("view/libraryUserMenu.fxml")));
            activity = new Activity(activityNumber, "None", user.getUid(), "None", "Library User Logged in");
        }
        Main.journalMemory.put(activityNumber, activity);
        return scene;
    }

    @FXML
    public void aboutHandler(ActionEvent e) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setContent(FXMLLoader.load(Main.class.getResource("view/about.fxml")));
        alert.setHeaderText("Group 3 Members");
        alert.setTitle("About");
        alert.setGraphic(null);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for (int i = 0; i < images.length; i++) {
            images[i] = new Image(Main.class.getResource("images/image" + (i + 1) + ".jpg").toExternalForm(), true);
        }
        pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer index) {
                ImageView image = new ImageView(images[index]);
                return image;
            }
        });
        Timeline timer = new Timeline(new KeyFrame(Duration.seconds(3), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pagination.setCurrentPageIndex((pagination.getCurrentPageIndex() + 1) % 7);
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

}
