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
import lms.model.*;

public class ViewRequestsController implements Initializable {

    @FXML private ListView<String> requestList;
    
    @FXML
    public void requestListHandler(MouseEvent e){
        String text = requestList.getSelectionModel().getSelectedItem();
        if(text != null && !text.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            String uid = text.substring(14, 28);
            String rid = text.substring(text.length()-4);
            LibraryUser user = (LibraryUser) Main.usersMemory.get(uid);
            Resource resource = Main.resourcesMemory.get(rid);
            String list = "Collected Resources by User:\n";

            alert.setTitle("Request From User");
            alert.setGraphic(null);
            alert.setHeaderText(null);
            for(String res: user.getCollectedResourcesID())
                list = list.concat(res + "\n");
            alert.setContentText(list + "Remaining Quantity: " + resource.getRemainingQuantity());

            ButtonType grantRequestButton = new ButtonType("Grant");    
            ButtonType denyRequestButton = new ButtonType("Deny");    
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(grantRequestButton, denyRequestButton, cancelButton);
            alert.getDialogPane().getStylesheets().add(Main.class.getResource("style/theme.css").toExternalForm());
            Optional<ButtonType> result = alert.showAndWait();

            Librarian librarian = (Librarian)Main.loggedUser;
            
            if(result.get() == grantRequestButton){
                boolean isAvailable = librarian.treatResourceRequest(uid, rid, true);
                if(isAvailable){
                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setContentText("Request Granted Succesfully !!!");
                    info.getDialogPane().getStylesheets().add(Main.class.getResource("style/theme.css").toExternalForm());
                    info.showAndWait();
                }
                else{
                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setContentText("Unable to Grant Request besacuse the Resource is currently not Available !!!");
                    info.getDialogPane().getStylesheets().add(Main.class.getResource("style/theme.css").toExternalForm());
                    info.showAndWait();
                }
                initialize(null, null);
            }
            else if(result.get() == denyRequestButton){
                librarian.treatResourceRequest(uid, rid, false);
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setContentText("Request Rejected Succefully !!!");
                info.getDialogPane().getStylesheets().add(Main.class.getResource("style/theme.css").toExternalForm());
                info.showAndWait();
                initialize(null, null);
            }
        }    
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ArrayList<String> list = new ArrayList<>();
        Librarian librarian = (Librarian)Main.loggedUser;
        for(LibraryUser user: librarian.viewUsers()){
            for(String rid: user.getRequestedResourcesID()){
                Resource resource = Main.resourcesMemory.get(rid);
                String request = "Request From: " + user.getUid()+ " Name: " + user.getFirstname() + " " + user.getLastname() +
                        " on " + resource.getName() + " with ID " + rid;
                list.add(request);
            }
        }
                
        requestList.setItems(FXCollections.observableArrayList(list));
        requestList.setPlaceholder(new Label("No Request from any User"));
        requestList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }        
}
