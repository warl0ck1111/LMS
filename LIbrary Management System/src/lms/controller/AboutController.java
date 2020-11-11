package lms.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;

public class AboutController implements Initializable {

    @FXML private TableView<Member> membersTable;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ArrayList<Member> members = new ArrayList<>();
        members.add(new Member("SULEIMAN Sera", "2014/1/50813CI"));
        members.add(new Member("UKPE Esther Success", "2014/1/50831CI"));
        members.add(new Member("ABDULKAREEM Aliyu Suleiman", "2014/1/50868CI"));
        members.add(new Member("ANEBI Benjamin", "2014/1/50834CI"));
        members.add(new Member("AKANDE Ayodele Oluwatoyin", "2014/1/50841CI"));
        members.add(new Member("ARINZE Stanley", "2015/2/57505CI"));
        members.add(new Member("SAIDU Abdulazeez Hayatu", "2014/1/50852CI"));
        members.add(new Member("SULEIMAN Ahmad Daudu", "2014/1/52175CI"));
        members.add(new Member("LANKWAGH Sesugh Simon", "2015/2/57641CI"));
        members.add(new Member("OKALA Bashir Onuche", "2014/1/50743CI"));
        members.add(new Member("ILORI Oluwashina Babatunde", "2014/1/50874CI"));
        
        membersTable.setItems(FXCollections.observableArrayList(members));
    }    
    
    public class Member{
        private String name;
        private String matricNumber;
        
        public Member(String n, String m){
            name= n;
            matricNumber = m;
        }
        
        public String getName(){
            return name;
        }
        
        public String getMatricNumber(){
            return matricNumber;
        }
    }
}
