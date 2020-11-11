package lms.controller;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lms.Main;
import lms.model.*;

public class ViewJournalController implements Initializable {
    
    @FXML private TableView<Activity> journalTable;
    @FXML private TableColumn<Activity, Date> dateCol;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Librarian librarian = (Librarian)Main.loggedUser;
        journalTable.setItems(librarian.viewJournal());
        journalTable.getSortOrder().add(dateCol);
    }    
    
}
