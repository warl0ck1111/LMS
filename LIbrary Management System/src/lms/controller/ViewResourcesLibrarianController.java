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
import lms.model.Librarian;
import lms.model.Resource;

public class ViewResourcesLibrarianController implements Initializable {
    
    @FXML private TableView<Resource> resourcesTable;
    @FXML private ChoiceBox<String> searchByChoiceBox;
    @FXML private TextField searchField;
    @FXML private Label titleLabel;
    @FXML private Label messageLabel;
    @FXML private TextField nameField;
    @FXML private ChoiceBox<String> typeChoiceBox;
    @FXML private ChoiceBox<String> categoryChoiceBox;
    @FXML private ChoiceBox<Integer> quantityChoiceBox;
    @FXML private TableColumn<Resource, Date> dateCol;
    
    @FXML
    public void addResourceButtonHandler(ActionEvent e){
        Librarian librarian = (Librarian)Main.loggedUser;
        
        String name = nameField.getText();
        String type = typeChoiceBox.getValue();
        String category = categoryChoiceBox.getValue();
        int quantity = quantityChoiceBox.getValue();
        
        String rid = librarian.addResource(name, type, category, quantity);
        messageLabel.setText("New Resource Added Succesfully with ID: " + rid);
        initialize(null, null);
    }
    
    @FXML
    public void searchButtonHandler(ActionEvent e){
        String searchKey = searchField.getText();
        Librarian librarian = (Librarian)Main.loggedUser;
        
        resourcesTable.setItems(librarian.searchResource(searchKey, searchByChoiceBox.getValue()));
        resourcesTable.setPlaceholder(new Label("No Result Found"));
        titleLabel.setText("Search Result: "+ searchByChoiceBox.getValue());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Librarian librarian = (Librarian) Main.loggedUser;
        
        ArrayList<String> listSearchBy = new ArrayList<>();
        listSearchBy.add("By Name");
        listSearchBy.add("By Resource ID");
        listSearchBy.add("By Type");
        listSearchBy.add("By Category");
        searchByChoiceBox.setItems(FXCollections.observableArrayList(listSearchBy));
        searchByChoiceBox.setValue(listSearchBy.get(0));
        
        ArrayList<String> listType = new ArrayList<>();
        listType.add("Book");
        listType.add("Journal");
        listType.add("Magazine");
        listType.add("Newspaper");
        listType.add("Audio Tape/CD");
        listType.add("Video Tape/CD");
        typeChoiceBox.setItems(FXCollections.observableArrayList(listType));
        typeChoiceBox.setValue(listType.get(0));
        
        ArrayList<String> listCategory = new ArrayList<>();
        listCategory.add("Operating Systems");
        listCategory.add("Data Structures");
        listCategory.add("Programming Languages");
        listCategory.add("Graphics");
        listCategory.add("Web Design");
        listCategory.add("System Analysis");
        listCategory.add("Digital Design");
        categoryChoiceBox.setItems(FXCollections.observableArrayList(listCategory));
        categoryChoiceBox.setValue(listCategory.get(0));   
        
        ArrayList<Integer> listQuantity = new ArrayList<>();
        for(int i=1; i<=50; i++)
            listQuantity.add(i);
        quantityChoiceBox.setItems(FXCollections.observableArrayList(listQuantity));
        quantityChoiceBox.setValue(listQuantity.get(0));   
        
        titleLabel.setText("All Resources");
        
        resourcesTable.setPlaceholder(new Label("No Resource Added Yet"));
        resourcesTable.setItems(librarian.viewResources());
        resourcesTable.getSortOrder().add(dateCol);
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
                            
                            String collectors = "Collectors ID:\n";
                            String requestors = "Requestors ID:\n";
                            for(String uid: resource.getCollectorsID()){
                                collectors = collectors.concat(uid +"\n");
                            }
                            for(String uid: resource.getRequestorsID()){
                                requestors = requestors.concat(uid +"\n");
                            }
                            
                            ButtonType deleteButton = new ButtonType("Delete");
                            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                            
                            alert.setTitle("Perform action on Resource");
                            alert.setGraphic(null);
                            alert.setHeaderText(null);
                            alert.setContentText(collectors + requestors);                            
                            alert.getButtonTypes().setAll(deleteButton, cancelButton);
                            alert.getDialogPane().getStylesheets().add(Main.class.getResource("style/theme.css").toExternalForm());
                            Optional<ButtonType> result = alert.showAndWait();
                            
                            if(result.get() == deleteButton){
                                librarian.removeResource(resource.getRid());
                                Alert info = new Alert(Alert.AlertType.INFORMATION);
                                info.setContentText("Resource Deleted Successfully !!!");
                                info.getDialogPane().getStylesheets().add(Main.class.getResource("style/theme.css").toExternalForm());
                                info.showAndWait();
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
