package lms.model;

import java.io.Serializable;
import java.util.Date;

public class Activity implements Serializable{
    private final String activityNumber;
    private final String uidLibrarian;
    private final String uidLibraryUser;
    private final String rid;
    private final String name;
    private final Date date;
    
    public Activity(String activityNumber, String uidLibrarian, String uidLibraryUser, String rid, String name){
        this.activityNumber = activityNumber;
        this.uidLibrarian = uidLibrarian;
        this.uidLibraryUser = uidLibraryUser;
        this.rid = rid;
        this.name = name;
        date = new Date();
    }
    
    public String getActivityNumber(){
        return activityNumber;
    }
    
    public String getUidLibrarian(){
        return uidLibrarian;
    }
    
    public String getUidLibraryUser(){
        return uidLibraryUser;
    }
    
    public String getRid(){
        return rid;
    }
    
    public String getName(){
        return name;
    }
    
    public Date getDate(){
        return date;
    }
}
