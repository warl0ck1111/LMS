package lms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Resource implements Serializable{
    private final String rid;
    private final String name;
    private final String type;
    private final int totalQuantity;
    private int remainingQuantity;
    private final String category;
    private final Date dateAdded;
    private final ArrayList<String> collectorsID;
    private final ArrayList<String> requestorsID;
    
    public Resource(String rid, String name, String type, String category, int totalQuantity){
        this.rid = rid;
        this.name = name;
        this.type = type;
        this.category = category;
        this.totalQuantity = totalQuantity;
        
        dateAdded = new Date();
        remainingQuantity = totalQuantity;
        collectorsID = new ArrayList<>();
        requestorsID = new ArrayList<>(); 
    }
    
    public String getRid(){
        return rid;
    }
    
    public String getName(){
        return name;
    }
    
    public String getType(){
        return type;
    }
    
    public int getTotalQuantity(){
        return totalQuantity;
    }
    
    public int getRemainingQuantity(){
        return remainingQuantity;
    }
    
    public String getCategory(){
        return category;
    }
    
    public Date getDateAdded(){
        return dateAdded;
    }
    
    public ArrayList<String> getCollectorsID(){
        return collectorsID;
    }
    
    public ArrayList<String> getRequestorsID(){
        return requestorsID;
    }
    
    public void decrementQuantity(){
        remainingQuantity--;
    }
    
    public void incrementQuantity(){
        remainingQuantity++;
    }
    
}