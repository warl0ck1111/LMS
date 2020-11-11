package lms;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lms.model.*;

public class Main extends Application {
    
    public static HashMap<String, User> usersMemory;
    public static HashMap<String, Resource> resourcesMemory;
    public static HashMap<String, Activity> journalMemory;
    
    private static File recordDirectory;
    
    private static File usersRecord;
    private static File resourcesRecord;
    private static File journal;
    
    public static User loggedUser;
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("view/login.fxml"));
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Library Management System (LMS)");
  
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent> (){
            @Override
            public void handle(WindowEvent e){
                if(loggedUser != null){
                    String activityNumber = String.valueOf(Main.journalMemory.size() + 1);
                    Activity activity;
                    if(loggedUser.getClass()== Librarian.class){
                        activity = new Activity(activityNumber, loggedUser.getUid(), "None", "None", "Librarian Logged out");
                    }else{
                        activity = new Activity(activityNumber, "None", loggedUser.getUid(), "None", "Library User Logged out");
                    }
                    journalMemory.put(activityNumber, activity);
                }
                
                if(save()){
                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setHeaderText(null);
                    info.setGraphic(null);
                    info.setContentText("Thank You for using this Application"
                            + "\nYour Records has been Saved "
                            + "\nSuccessfully."
                            + "\nGood Bye!!!");
                    info.getDialogPane().getStylesheets().add(getClass().getResource("style/theme.css").toExternalForm());
                    info.showAndWait();
                }
                else{
                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setContentText("Sorry, your records was not saved due to some reasons"
                            + "contact the the Software Developer");
                    info.getDialogPane().getStylesheets().add(getClass().getResource("style/theme.css").toExternalForm());
                    info.showAndWait();
                }
            }
        }); 
    }
    
    public static void main(String[] args) {
        usersMemory = new HashMap<>();
        resourcesMemory = new HashMap<>();
        journalMemory = new HashMap<>();
        
        recordDirectory = new File(System.getenv("APPDATA") + "/LMS");
        
        usersRecord = new File(recordDirectory + "/users.ser");
        resourcesRecord = new File(recordDirectory + "/resources.ser");
        journal = new File(recordDirectory + "/journal.ser");
        
        load();
        if(!usersMemory.containsKey("admin")){
            User admin = new Librarian("admin", "Group 3", "Members", "Male");
            admin.setPassword("admin");
            usersMemory.put(admin.getUid(), admin);
        }
        
        launch(args);
    }
    
    public static boolean load(){
        ObjectInputStream reader;
        if(recordDirectory.exists()){
            try{
                reader = new ObjectInputStream(new FileInputStream(usersRecord));
                User user = (User)reader.readObject();
                while(user != null){
                    if(user.getClass() == Librarian.class){
                        Librarian librarian = (Librarian) user;
                        String uid = librarian.getUid(); 
                        usersMemory.put(uid, librarian);
                    }
                    else{
                        LibraryUser libraryUser = (LibraryUser) user;
                        String uid = libraryUser.getUid();
                        usersMemory.put(uid, libraryUser);
                    }
                    user = (User)reader.readObject();
                }
                reader.close();
            }catch(IOException | ClassNotFoundException e){
                if(e.getClass() != EOFException.class){
                    System.out.println("Error while loading users record " + e);
                    e.printStackTrace();
                    return false;
                }
            }
            try{
                reader = new ObjectInputStream(new FileInputStream(resourcesRecord));
                Resource resource = (Resource)reader.readObject();
                while(resource != null){
                    String rid = resource.getRid();
                    resourcesMemory.put(rid, resource);
                    resource = (Resource)reader.readObject();    
                }
                reader.close();
            }catch(IOException | ClassNotFoundException e){
                if(e.getClass() != EOFException.class){
                    System.out.println("Error while loading resources record " + e);
                    return false;
                }
            }
            try{
                reader = new ObjectInputStream(new FileInputStream(journal));
                Activity activity = (Activity)reader.readObject();
                while(activity != null){
                    String activityNumber = activity.getActivityNumber();
                    journalMemory.put(activityNumber, activity);
                    activity = (Activity)reader.readObject();    
                }
                reader.close();
            }catch(IOException | ClassNotFoundException e){
                if(e.getClass() != EOFException.class){
                    System.out.println("Error while loading journal record " + e);
                    return false;
                }
            }
            return true;
        }else{
            return false;
        }    
    }
    
    public boolean save(){
        ObjectOutputStream writer;
        boolean success = true;
        if(!recordDirectory.exists()){
            success = recordDirectory.mkdir();
        }
        if(success){
            try{
                writer = new ObjectOutputStream(new FileOutputStream(usersRecord));
                for(String key: usersMemory.keySet()){
                    writer.writeObject(usersMemory.get(key));
                }
                writer.flush();
                writer.close();
            }catch(IOException e){
                System.out.println("Error while saving users record " + e);
                success = false;
            }
            try{
                writer = new ObjectOutputStream(new FileOutputStream(resourcesRecord));
                for(String key: resourcesMemory.keySet()){
                    writer.writeObject(resourcesMemory.get(key));
                }
                writer.flush();
                writer.close();
            }catch(IOException e){
                System.out.println("Error while saving resources record " + e);
                success = false;
            }
            try{
                writer = new ObjectOutputStream(new FileOutputStream(journal));
                for(String key: journalMemory.keySet()){
                    writer.writeObject(journalMemory.get(key));
                }
                writer.flush();
                writer.close();
            }catch(IOException e){
                System.out.println("Error while saving users record " + e);
                success = false;
            }
        }
        return success;
    }
    
}
