package lms.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import lms.Main;
import lms.model.*;

public class RegisterUserController implements Initializable{

    @FXML private TextField matricNumberField;
    @FXML private TextField pfNumberField;
    @FXML private TextField firstnameLibraryUserField;
    @FXML private TextField lastnameLibraryUserField;
    @FXML private TextField firstnameLibrarianField;
    @FXML private TextField lastnameLibrarianField;
    @FXML private ToggleGroup genderLibraryUserToggle;
    @FXML private ToggleGroup genderLibrarianToggle;
    @FXML private ChoiceBox departmentChoiceBox;
    @FXML private ChoiceBox levelChoiceBox;
    
    
    @FXML private Label messageLabel;
    
    @FXML
    public void addLibraryUserButtonHandler(ActionEvent e){
        try{
            String uid = matricNumberField.getText();
            String firstname = firstnameLibraryUserField.getText();
            String lastname = lastnameLibraryUserField.getText();
            RadioButton genderRadio = (RadioButton)genderLibraryUserToggle.getSelectedToggle();
            String gender = genderRadio.getText();
            String department = departmentChoiceBox.getValue().toString();
            String level = levelChoiceBox.getValue().toString();
            
            if(uid.isEmpty() || firstname.isEmpty() || lastname.isEmpty()){
                messageLabel.setText("All fields are required !!!");
                return;
            }
            
            if(!Main.usersMemory.containsKey(uid)){
                if(uid.length() != 14){
                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setContentText("Invalid Matric Number!!!");
                    info.getDialogPane().getStylesheets().add(Main.class.getResource("style/theme.css").toExternalForm());
                    info.showAndWait();
                    return;
                }
                Librarian librarian = (Librarian)Main.loggedUser;
                String password = librarian.registerUser(uid, firstname, lastname, gender, department, level);
                
                messageLabel.setText("New User added successfully with PASSWORD: " + password);
                matricNumberField.setText("");
                firstnameLibraryUserField.setText("");
                lastnameLibraryUserField.setText("");
            }
            else{
                messageLabel.setText("Matric number already exist !!!");
            }
        }catch(Exception ex){
            messageLabel.setText("Fail to add new user");
            ex.printStackTrace();
        }
    }
    
    @FXML
    public void addLibrarianButtonHandler(ActionEvent e){
        try{
            String uid = pfNumberField.getText();
            String firstname = firstnameLibrarianField.getText();
            String lastname = lastnameLibrarianField.getText();
            RadioButton genderRadio = (RadioButton)genderLibrarianToggle.getSelectedToggle();
            String gender = genderRadio.getText();
            
            if(uid.isEmpty() || firstname.isEmpty() || lastname.isEmpty()){
                messageLabel.setText("All fields are required !!!");
                return;
            }
            
            if(!Main.usersMemory.containsKey(uid)){
                Librarian librarian = (Librarian)Main.loggedUser;
                String password = librarian.registerUser(uid, firstname, lastname, gender);
                
                messageLabel.setText("New Librarian added successfully with PASSWORD: " + password);
                matricNumberField.setText("");
                firstnameLibrarianField.setText("");
                lastnameLibrarianField.setText("");
            }
            else{
                messageLabel.setText("PF number already exist !!!");
            }
        }catch(Exception ex){
            messageLabel.setText("Fail to add new Librarian");
            ex.printStackTrace();
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<String> listDepartment = new ArrayList<>();
        listDepartment.add("Information and Media Technology (IMT)");
        listDepartment.add("Cyber Security Science (CSS)");
        listDepartment.add("Computer Science (CPT)");
        departmentChoiceBox.setItems(FXCollections.observableArrayList(listDepartment));
        departmentChoiceBox.setValue(listDepartment.get(0));
        
        ArrayList<String> listLevel = new ArrayList<>();
        listLevel.add("100");
        listLevel.add("200");
        listLevel.add("300");
        listLevel.add("400");
        listLevel.add("500");
        levelChoiceBox.setItems(FXCollections.observableArrayList(listLevel));
        levelChoiceBox.setValue(listLevel.get(0));
        
    }
    
}