package securefilesharing;


import java.io.Serializable;
import java.util.ArrayList;


public class User implements Serializable{
    private String userName;
    private String password;
    private ArrayList<String> fileOwn;
    private ArrayList<String> requests; //incoming requests
    private ArrayList<String> sentReqs;
    private int id;
    private ArrayList<String> access;
    
    
    
    public User(String userName, String password){
        this.userName = userName;
        this.password = password;
        this.fileOwn = new ArrayList<>();
        this.requests = new ArrayList<>();
        this.sentReqs = new ArrayList<>();
        this.access = new ArrayList<>();
    }
    
    public void addSentReq(String fname){
        this.sentReqs.add(fname);
    }

    public ArrayList<String> getSentReqs() {
        return this.sentReqs;
    }
    
    public void removeSentReq(String req){
        this.sentReqs.remove(req);
    }
    
    public void removeRequest(String req){
        this.requests.remove(req);
    }
    
    public void addAccess(String fname){
        this.access.add(fname);
    }
    
    public ArrayList<String> getAccess(){
        return this.access;
    }
    
    public void addFileOwn(String fname){
        this.fileOwn.add(fname);
    }
    
    public void addRequest(String request){
        this.requests.add(request);
    }

    public ArrayList<String> getRequests() {
        return requests;
    }
    
    
    
    public ArrayList<String> getFileOwn(){
        return fileOwn;
    }
    
    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getId(){
        return this.id;
    }
    
    
}
