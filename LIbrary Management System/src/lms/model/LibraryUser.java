package lms.model;

import java.util.ArrayList;
import lms.Main;

public class LibraryUser extends User{
    public enum Status{CLEARED, FINED, SUSPENDED};
    private final String department;
    private final String level;
    private final ArrayList<String> collectedResourcesID;
    private final ArrayList<String> requestedResourcesID;
    private Status status;
    
    public LibraryUser(String uid, String firstname, String lastname, String gender, String department, String level){
        super(uid, firstname, lastname, gender);
        this.department = department;
        this.level = level;
        status = Status.CLEARED;
        collectedResourcesID = new ArrayList<>();
        requestedResourcesID = new ArrayList<>();
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    
    public String getDepartment(){
        return department;
    }
    
    public String getLevel(){
        return level;
    }
    
    public ArrayList<String> getRequestedResourcesID(){
        return requestedResourcesID;
    }
    
    public ArrayList<String> getCollectedResourcesID(){
        return collectedResourcesID;
    }
    
    public boolean requestResource(String rid){
        if(status != Status.CLEARED)
            return false;
        Resource resource = Main.resourcesMemory.get(rid);
        requestedResourcesID.add(rid);
        resource.getRequestorsID().add(getUid());
        
        String activityNumber = String.valueOf(Main.journalMemory.size() + 1);
        Activity activity = new Activity(activityNumber, "None", this.getUid(), rid, "Resource Request Sent to Librarian");
        Main.journalMemory.put(activityNumber, activity);
        
        return (resource.getRemainingQuantity() > 0);
    }
    
    public void returnResource(String rid){
        Resource resource = Main.resourcesMemory.get(rid);
        resource.getCollectorsID().remove(getUid());
        resource.incrementQuantity();
        collectedResourcesID.remove(rid);
        
        String activityNumber = String.valueOf(Main.journalMemory.size() + 1);
        Activity activity = new Activity(activityNumber, "None", this.getUid(), rid, "User Return Resource");
        Main.journalMemory.put(activityNumber, activity);
    }
}
