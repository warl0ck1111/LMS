package lms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lms.Main;

public abstract class User implements Serializable {
    private final String uid;
    private String password;
    private final String firstname;
    private final String lastname;
    private final String gender;
    private final Date registrationDate;
    
    public User(String uid, String firstname, String lastname, String gender){
        this.uid = uid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        registrationDate = new Date();
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return password;
    }
    
    public String getUid(){
        return uid;
    }
    
    public String getFirstname(){
        return firstname;
    }
    
    public String getLastname(){
        return lastname;
    }
    
    public String getGender(){
        return gender;
    }
      
    public Date getRegistrationDate(){
        return registrationDate;
    }
    
    public ObservableList<Resource> viewResources(){
        ArrayList<Resource> resources = new ArrayList<>();
        resources.addAll(Main.resourcesMemory.values());
        return FXCollections.observableArrayList(resources);
    }
    
    public ObservableList<Resource> searchResource(String searchKey, String searchBy){
        ArrayList<Resource> result = new ArrayList<>();
        switch(searchBy){
            case "By Name":  
                for(Resource resource: viewResources()){
                    if(resource.getName().toLowerCase().contains(searchKey.toLowerCase())){
                        result.add(resource);
                    }
                }
                break;
            case "By Resource ID":
                for(Resource resource: viewResources()){
                    if(resource.getRid().toLowerCase().contains(searchKey.toLowerCase())){
                        result.add(resource);
                    }
                }
                break;
            case "By Type":
                for(Resource resource: viewResources()){
                    if(resource.getType().toLowerCase().contains(searchKey.toLowerCase())){
                        result.add(resource);
                    }
                }
                break;
            case "By Category":
                for(Resource resource: viewResources()){
                    if(resource.getCategory().toLowerCase().contains(searchKey.toLowerCase())){
                        result.add(resource);
                    }
                }
                break;    
        }
        return FXCollections.observableArrayList(result);    
    }
    
    public void changePassword(String newPassword){
        String activityNumber = String.valueOf(Main.journalMemory.size() + 1);
        Activity activity;
        if(Main.loggedUser.getClass() == LibraryUser.class){
            activity = new Activity(activityNumber, "None", this.getUid(), "None", "Library User Change Password to: " + newPassword);
        }
        else{
            activity = new Activity(activityNumber, this.getUid(), "None", "None", "Librarian Change Password to: " + newPassword);

        }
        Main.journalMemory.put(activityNumber, activity);
        setPassword(newPassword);
    }
}
