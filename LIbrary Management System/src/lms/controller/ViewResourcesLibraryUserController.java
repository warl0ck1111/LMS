package lms.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import lms.Main;
import lms.model.*;

public class ViewResourcesLibraryUserController implements Initializable {

    @FXML private TableView<Resource> resourcesTable;
    @FXML private TableColumn<Resource, Date> dateCol;
    @FXML private ChoiceBox<String> searchByChoiceBox;
    @FXML private TextField searchField;
    @FXML private Label titleLabel;

    @FXML
    public void searchButtonHandler(ActionEvent e){
        String searchKey = searchField.getText();
        LibraryUser user = (LibraryUser)Main.loggedUser;
        
        resourcesTable.setItems(user.searchResource(searchKey, searchByChoiceBox.getValue()));
        resourcesTable.setPlaceholder(new Label("No Result Found"));
        resourcesTable.getSortOrder().add(dateCol);
        titleLabel.setText("Search Result: "+ searchByChoiceBox.getValue());
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ArrayList<String> listSearchBy = new ArrayList<>();
        listSearchBy.add("By Name");
        listSearchBy.add("By Resource ID");
        listSearchBy.add("By Type");
        listSearchBy.add("By Category");
        searchByChoiceBox.setItems(FXCollections.observableArrayList(listSearchBy));
        searchByChoiceBox.setValue(listSearchBy.get(0));
        
        titleLabel.setText("All Resources");
        LibraryUser user = (LibraryUser) Main.loggedUser;
        resourcesTable.setPlaceholder(new Label("No Resource Added Yet"));
        resourcesTable.setItems(user.viewResources());
        resourcesTable.setRowFactory(new Callback<TableView<Resource>, TableRow<Resource>>() {
            @Override
            public TableRow<Resource> call(TableView<Resource> tableView) {
                TableRow<Resource> row = new TableRow<>();
                row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if(!row.isEmpty()){
                            Resource resource = row.getItem();
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        
                            alert.setTitle("Perform action on Resource");
                            alert.getDialogPane().getStylesheets().add(Main.class.getResource("style/theme.css").toExternalForm());
                            
                            ButtonType requestButton;
                            if(user.getRequestedResourcesID().contains(resource.getRid())){
                                alert.setAlertType(Alert.AlertType.INFORMATION);
                                alert.setContentText("Request for this resource Already Sent !!!");
                                alert.showAndWait();
                                return;
                            }
                            else if(user.getCollectedResourcesID().contains(resource.getRid())){
                                alert.setAlertType(Alert.AlertType.INFORMATION);
                                alert.setContentText("You have Already Collected this Resource !!!");
                                alert.showAndWait();
                                return;
                            }
                            else
                                requestButton = new ButtonType("Send Resource Request");
                            
                            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                            
                            alert.setGraphic(null);
                            alert.setHeaderText(null);
                            alert.setContentText(null);
                            alert.getButtonTypes().setAll(requestButton, cancelButton);
                            Optional<ButtonType> result = alert.showAndWait();
                            
                            if(result.get() == requestButton){
                                boolean isAvailable = user.requestResource(resource.getRid());
                                if(user.getStatus() == LibraryUser.Status.FINED){
                                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                                    info.setContentText("Sorry, you are currently Fined, contact the Librarian to clear you !!!");
                                    info.getDialogPane().getStylesheets().add(Main.class.getResource("style/theme.css").toExternalForm());
                                    info.showAndWait();
                                }
                                else if(isAvailable){
                                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                                    info.setContentText("Request sent Succesfully !!!");
                                    info.getDialogPane().getStylesheets().add(Main.class.getResource("style/theme.css").toExternalForm());
                                    info.showAndWait();
                                }
                                else{
                                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                                    info.setContentText("Request sent but the resource is currently not Available !!!");
                                    info.getDialogPane().getStylesheets().add(Main.class.getResource("style/theme.css").toExternalForm());
                                    info.showAndWait();
                                }
                                initialize(null, null);
                            }
                        }
                    }
                });
                return row;
            }
        });
    }    
    
}
