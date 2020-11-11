package lms.model;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lms.Main;

public class Librarian extends User{
    
    public Librarian(String uid, String firstname, String lastname, String gender){
        super(uid, firstname, lastname, gender);
    }
    
    public ObservableList<LibraryUser> viewUsers(){
        ArrayList<LibraryUser> users = new ArrayList<>();
        for(String id: Main.usersMemory.keySet()){
            if(Main.usersMemory.get(id).getClass() == Librarian.class)
                continue;
            users.add((LibraryUser)Main.usersMemory.get(id));
        }
        return FXCollections.observableArrayList(users);
    }
    
    public ObservableList<Activity> viewJournal(){
        ArrayList<Activity> journal = new ArrayList<>();
        journal.addAll(Main.journalMemory.values());
        return FXCollections.observableArrayList(journal);
    }
    
    public String registerUser(String uid, String firstname, String lastname, String gender, String department, String level){
        LibraryUser newUser = new LibraryUser(uid, firstname, lastname, gender, department, level);
        int password = (int)(Math.random() * 10000);
        newUser.setPassword(String.valueOf(password));
        Main.usersMemory.put(uid, newUser);
        
        String activityNumber = String.valueOf(Main.journalMemory.size() + 1);
        Activity activity = new Activity(activityNumber, this.getUid(), newUser.getUid(), "None", "New User Registered with password: " + password);
        Main.journalMemory.put(activityNumber, activity);
        
        return String.valueOf(password);
    }
    
    public String registerUser(String uid, String firstname, String lastname, String gender){
        Librarian newUser = new Librarian(uid, firstname, lastname, gender);
        int password = (int)(Math.random() * 10000);
        newUser.setPassword(String.valueOf(password));
        Main.usersMemory.put(uid, newUser);
        
        String activityNumber = String.valueOf(Main.journalMemory.size() + 1);
        Activity activity = new Activity(activityNumber, this.getUid(), newUser.getUid(), "None", "New Librarian Registered with password: " + password);
        Main.journalMemory.put(activityNumber, activity);
        
        return String.valueOf(password);
    }
    
    public void suspendUser(String uid){
        LibraryUser user = (LibraryUser)Main.usersMemory.get(uid);
        for(String rid: user.getCollectedResourcesID()){
            Main.resourcesMemory.get(rid).getCollectorsID().remove(uid);
        }
        for(String rid: user.getRequestedResourcesID()){
            Main.resourcesMemory.get(rid).getRequestorsID().remove(uid);
        }
        user.setStatus(LibraryUser.Status.SUSPENDED);
        
        String activityNumber = String.valueOf(Main.journalMemory.size() + 1);
        Activity activity = new Activity(activityNumber, this.getUid(), user.getUid(), "None", "Existing User Suspended/Deleted");
        Main.journalMemory.put(activityNumber, activity);
    }
    
    public ObservableList<LibraryUser> searchUser(String searchKey, String searchBy){
        ArrayList<LibraryUser> result = new ArrayList<>();
        switch(searchBy){
            case "By Name":  
                for(LibraryUser user: viewUsers()){
                    if(user.getFirstname().toLowerCase().contains(searchKey.toLowerCase()) ||
                            user.getLastname().toLowerCase().contains(searchKey.toLowerCase())){
                        result.add(user);
                    }
                }
                break;
            case "By Matric Number":
                for(LibraryUser user: viewUsers()){
                    if(user.getUid().toLowerCase().contains(searchKey.toLowerCase())){
                        result.add(user);
                    }
                }
                break;
            case "By Department":
                for(LibraryUser user: viewUsers()){
                    if(user.getDepartment().toLowerCase().contains(searchKey.toLowerCase())){
                        result.add(user);
                    }
                }
                break;
            case "By Level":
                for(LibraryUser user: viewUsers()){
                    if(user.getLevel().toLowerCase().contains(searchKey.toLowerCase())){
                        result.add(user);
                    }
                }
                break;    
        }
        return FXCollections.observableArrayList(result);       
    }
       
    public boolean treatResourceRequest(String uid, String rid, boolean isGranted){
        LibraryUser user = (LibraryUser)Main.usersMemory.get(uid);
        Resource resource = Main.resourcesMemory.get(rid);
        if(isGranted){
            if(resource.getRemainingQuantity() <= 0)         
                return false;
            resource.getCollectorsID().add(uid);
            user.getCollectedResourcesID().add(rid);
            
            resource.getRequestorsID().remove(uid);
            user.getRequestedResourcesID().remove(rid);
            
            resource.decrementQuantity();
        }
        else{
            user.getRequestedResourcesID().remove(rid);
            resource.getRequestorsID().remove(uid);
        }
        
        String activityNumber = String.valueOf(Main.journalMemory.size() + 1);
        Activity activity = new Activity(activityNumber, this.getUid(), uid, rid,
                "Resource Request " + ((isGranted)? "Granted":"Rejected"));
        Main.journalMemory.put(activityNumber, activity);
        
        return true;
    }
    
    public String addResource(String name, String type, String category, int quantity){
        String rid;
        do{
            int rnd = (int)(Math.random()*10000);
            rid = String.valueOf(rnd);
        }while(Main.resourcesMemory.containsKey(rid));
        
        Resource resource = new Resource(rid, name, type, category, quantity);
        Main.resourcesMemory.put(resource.getRid(), resource);
        
        String activityNumber = String.valueOf(Main.journalMemory.size() + 1);
        Activity activity = new Activity(activityNumber, this.getUid(), "None", rid, "New Resource Added");
        Main.journalMemory.put(activityNumber, activity);
        
        return rid;
    }
    
    public void removeResource(String rid){
        Resource resource = Main.resourcesMemory.get(rid);
        for(String uid: resource.getCollectorsID()){
            LibraryUser user = (LibraryUser)Main.usersMemory.get(uid);
            user.getCollectedResourcesID().remove(rid);
        }
        for(String uid: resource.getRequestorsID()){
            LibraryUser user = (LibraryUser)Main.usersMemory.get(uid);
            user.getRequestedResourcesID().remove(rid);
        }
        Main.resourcesMemory.remove(rid);
        
        String activityNumber = String.valueOf(Main.journalMemory.size() + 1);
        Activity activity = new Activity(activityNumber, this.getUid(), "None", rid, "Existing Resource Suspended");
        Main.journalMemory.put(activityNumber, activity);
    }
    
    public void imposeFine(String uid){
        LibraryUser user = (LibraryUser)Main.usersMemory.get(uid);
        user.setStatus(LibraryUser.Status.FINED);
        
        String activityNumber = String.valueOf(Main.journalMemory.size() + 1);
        Activity activity = new Activity(activityNumber, this.getUid(), uid, "None", "Fine Imposed on User");
        Main.journalMemory.put(activityNumber, activity);
    }
    
    public void clearUser(String uid){
        LibraryUser user = (LibraryUser)Main.usersMemory.get(uid);
        user.setStatus(LibraryUser.Status.CLEARED);
        
        String activityNumber = String.valueOf(Main.journalMemory.size() + 1);
        Activity activity = new Activity(activityNumber, this.getUid(), uid, "None", "User Cleared");
        Main.journalMemory.put(activityNumber, activity);
    }
}
