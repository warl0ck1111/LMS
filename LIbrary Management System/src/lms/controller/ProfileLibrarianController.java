package lms.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lms.Main;
import lms.model.*;

public class ProfileLibrarianController implements Initializable {

    @FXML private Label firstnameLabel;
    @FXML private Label lastnameLabel;
    @FXML private Label uidLabel;
    @FXML private Label genderLabel;
    @FXML private Label dateLabel;
    @FXML private Label messageLabel;
    @FXML private TextField oldPasswordField;
    @FXML private TextField newPasswordField;
    @FXML private TextField confirmPasswordField;
    
    @FXML
    public void changePasswordButtonHandler(ActionEvent e){
        User user = Main.loggedUser;
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        if(oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()){
            messageLabel.setText("All Fields are required. Try Again !!!");
        }
        else{
            if(oldPassword.equals(user.getPassword())){
                if(newPassword.equals(confirmPassword)){
                    user.changePassword(newPassword);
                    messageLabel.setText("Password Changed Successfully !!!");
                    oldPasswordField.setText("");
                    newPasswordField.setText("");
                    confirmPasswordField.setText("");
                }
                else{
                    messageLabel.setText("Confirm Password not the same with New Password. Try Again !!!");
                }
            }
            else{
                messageLabel.setText("Old Password Incorrect. Try Again !!!");
            }
        }
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Librarian user = (Librarian)Main.loggedUser;
        firstnameLabel.setText(user.getFirstname());
        lastnameLabel.setText(user.getLastname());
        uidLabel.setText(user.getUid());
        genderLabel.setText(user.getGender());
        dateLabel.setText(user.getRegistrationDate().toString());
    }    
    
}
