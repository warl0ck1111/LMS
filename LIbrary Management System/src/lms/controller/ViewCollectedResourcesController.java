package lms.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import lms.Main;
import lms.model.LibraryUser;
import lms.model.Resource;

public class ViewCollectedResourcesController implements Initializable {

    @FXML private ListView<String> colectedResourcesList;
    
    @FXML
    public void colectedResourcesListHandler(MouseEvent e){
        String text = colectedResourcesList.getSelectionModel().getSelectedItem();
        if(text != null && !text.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            String rid = text.substring(13, text.indexOf('C')-1);
            Resource resource = Main.resourcesMemory.get(rid);                         

            alert.setTitle("Request From User");
            alert.setGraphic(null);
            alert.setHeaderText(null);
            alert.setContentText("Do you want to Return the Resource ?");

            ButtonType returnButton = new ButtonType("Return");                            
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getDialogPane().getStylesheets().add(Main.class.getResource("style/theme.css").toExternalForm());
            alert.getButtonTypes().setAll(returnButton, cancelButton);
            Optional<ButtonType> result = alert.showAndWait();

            if(result.get() == returnButton){
                LibraryUser user = (LibraryUser) Main.loggedUser;
                user.returnResource(resource.getRid());
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setContentText("Resource has been Returned Successfully !!!");
                info.getDialogPane().getStylesheets().add(Main.class.getResource("style/theme.css").toExternalForm());
                info.showAndWait();
                initialize(null, null);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ArrayList<String> list = new ArrayList<>();
        LibraryUser user = (LibraryUser)Main.loggedUser;
        
        for(String rid: user.getCollectedResourcesID()){
            Resource resource = Main.resourcesMemory.get(rid);
            String item = "Resource ID: " + rid + " Category: " + 
                    resource.getCategory() + " Name: " + resource.getName();
            list.add(item);
        }
                
        colectedResourcesList.setItems(FXCollections.observableArrayList(list));
        colectedResourcesList.setPlaceholder(new Label("No Resource Currently Collected from Library"));
        colectedResourcesList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }    
    
}
