package lms.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.stage.Stage;
import lms.Main;
import lms.model.Activity;

public class LibraryUserMenuController implements Initializable {
    @FXML private Tab profileTab;
    @FXML private Tab viewResourcesTab;
    @FXML private Tab viewCollectedResourcesTab;
    @FXML private Button logoutButton;
    
    @FXML
    public void logoutButtonHandler(ActionEvent e){
        String activityNumber = String.valueOf(Main.journalMemory.size() + 1);
        Activity activity = new Activity(activityNumber, "None", Main.loggedUser.getUid(), "None", "Library User Logged out");
        Main.journalMemory.put(activityNumber, activity);
        
        Main.loggedUser = null;
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        try{
        stage.setScene(new Scene(FXMLLoader.load(Main.class.getResource("view/login.fxml"))));
        }catch(IOException ex){
            System.out.println("An error occured while trying to logout");
            ex.printStackTrace();
        }
    }
    
    @FXML
    public void aboutHandler(ActionEvent e) throws IOException{
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setContent(FXMLLoader.load(Main.class.getResource("view/about.fxml")));
        alert.setHeaderText("Group 3 Members");
        alert.setTitle("About");
        alert.setGraphic(null);
        alert.showAndWait();
    }
    
    @FXML
    public void refreshProfileTab(Event e) throws IOException{
        profileTab.setContent(FXMLLoader.load(Main.class.getResource("view/profileLibraryUser.fxml")));
    }
    
    @FXML
    public void refreshViewResourcesTab(Event e) throws IOException{
        viewResourcesTab.setContent(FXMLLoader.load(Main.class.getResource("view/viewResourcesLibraryUser.fxml")));
    }
    
    @FXML
    public void refreshViewCollectedResourcesTab(Event e) throws IOException{
        viewCollectedResourcesTab.setContent(FXMLLoader.load(Main.class.getResource("view/viewCollectedResources.fxml")));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try{
            refreshProfileTab(null);
            refreshViewResourcesTab(null);
            refreshViewCollectedResourcesTab(null);
                    
        }catch(IOException e){
            System.out.println("An error ocuured while trying to set tabs content");
            e.printStackTrace();
        }
    }    
    
}
