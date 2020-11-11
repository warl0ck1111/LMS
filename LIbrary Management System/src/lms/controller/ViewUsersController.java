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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
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
import lms.model.LibraryUser;

public class ViewUsersController implements Initializable {

    @FXML
    private TableView<LibraryUser> usersTable;
    @FXML
    private TableColumn<LibraryUser, Date> dateCol;
    @FXML
    private ChoiceBox<String> searchByChoiceBox;
    @FXML
    private TextField searchField;
    @FXML
    private Label titleLabel;

    @FXML
    public void searchButtonHandler(ActionEvent e) {
        String searchKey = searchField.getText();
        Librarian librarian = (Librarian) Main.loggedUser;

        usersTable.setItems(librarian.searchUser(searchKey, searchByChoiceBox.getValue()));
        usersTable.setPlaceholder(new Label("No Result Found"));
        titleLabel.setText("Search Result: " + searchByChoiceBox.getValue());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ArrayList<String> list = new ArrayList<>();
        list.add("By Name");
        list.add("By Matric Number");
        list.add("By Department");
        list.add("By Level");

        searchByChoiceBox.setItems(FXCollections.observableArrayList(list));
        searchByChoiceBox.setValue(list.get(0));

        titleLabel.setText("All Registered Users");
        usersTable.setPlaceholder(new Label("No Registered Library User"));
        usersTable.getSortOrder().add(dateCol);

        Librarian librarian = (Librarian) Main.loggedUser;
        usersTable.setItems(librarian.viewUsers());

        usersTable.setRowFactory(new Callback<TableView<LibraryUser>, TableRow<LibraryUser>>() {
            @Override
            public TableRow<LibraryUser> call(TableView<LibraryUser> tableView) {
                TableRow<LibraryUser> row = new TableRow<>();
                row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (!row.isEmpty()) {
                            LibraryUser user = row.getItem();
                            Alert alert = new Alert(AlertType.CONFIRMATION);
                            alert.setTitle("Perform action on Library User");
                            alert.setGraphic(null);
                            alert.setHeaderText(null);
                            alert.setContentText(null);

                            ButtonType suspendButton = new ButtonType("Suspend");
                            ButtonType clearButton = new ButtonType("Clear");
                            ButtonType fineButton = new ButtonType("Fine");
                            ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

                            alert.getDialogPane().getStylesheets().add(Main.class.getResource("style/theme.css").toExternalForm());
                            alert.getButtonTypes().setAll(suspendButton, clearButton, fineButton, cancelButton);
                            Optional<ButtonType> result = alert.showAndWait();

                            if (result.get() == suspendButton) {
                                if (user.getStatus() == LibraryUser.Status.SUSPENDED) {
                                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                                    info.setContentText("User Already Suspended !!!");
                                    info.getDialogPane().getStylesheets().add(Main.class.getResource("style/theme.css").toExternalForm());
                                    info.showAndWait();
                                } else {
                                    librarian.suspendUser(user.getUid());
                                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                                    info.setContentText("User Suspended Successfully !!!");
                                    info.getDialogPane().getStylesheets().add(Main.class.getResource("style/theme.css").toExternalForm());
                                    info.showAndWait();
                                    initialize(null, null);
                                }
                            } else if (result.get() == clearButton) {
                                if (user.getStatus() == LibraryUser.Status.CLEARED) {
                                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                                    info.setContentText("User Already Cleared !!!");
                                    info.getDialogPane().getStylesheets().add(Main.class.getResource("style/theme.css").toExternalForm());
                                    info.showAndWait();
                                } else {
                                    librarian.clearUser(user.getUid());
                                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                                    info.setContentText("User Cleared Successfully !!!");
                                    info.getDialogPane().getStylesheets().add(Main.class.getResource("style/theme.css").toExternalForm());
                                    info.showAndWait();
                                }
                                initialize(null, null);
                            } else if (result.get() == fineButton) {
                                if (user.getStatus() == LibraryUser.Status.FINED) {
                                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                                    info.setContentText("User Already Fined !!!");
                                    info.getDialogPane().getStylesheets().add(Main.class.getResource("style/theme.css").toExternalForm());
                                    info.showAndWait();
                                } else {
                                    librarian.imposeFine(user.getUid());
                                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                                    info.setContentText("User Fined Successfully !!!");
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
