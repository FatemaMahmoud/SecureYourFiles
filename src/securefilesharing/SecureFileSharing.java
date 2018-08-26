/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package securefilesharing;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

public class SecureFileSharing {

    public static ArrayList<User> users;
    public static int currentUser;
    private static final String ACCESS_TOKEN = "1--iufXJjYAAAAAAAAAAIf6wdDi1yLY2qLrdDBNLbWczvKaPZeBKxgd76ER7N2Mf";
    private static DbxClientV2 client;
    private static DbxAppInfo user;
    public static ListFolderResult result;

    public static void addUser(User user) {
        users.add(user);
        users.get(users.size() - 1).setId(users.size() - 1);
        currentUser = users.size()-1;
    }
    
    public static void sendRequest(String fileName, int ownId){
        try {
            String reqName = currentUser + "&" + fileName;
            File f = new File(reqName);
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(reqName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            //write in the file the request info
            oos.writeObject("Request ID: ");
            oos.flush();
            oos.writeObject("Requester ID: " + users.get(currentUser));
            oos.flush();
            oos.writeObject("Requester Email: " + users.get(currentUser).getUserName()+"gmail.com");
            oos.flush();
            oos.writeObject("Requester Public Key: ");
            oos.flush();
            FileInputStream fis = new FileInputStream(reqName);
            client.files().uploadBuilder("/" + users.get(ownId).getUserName() + "/Requests/" + reqName)
                    .uploadAndFinish(fis);
            users.get(ownId).addRequest(reqName);
            users.get(currentUser).addSentReq(fileName);
            SecureFileSharing.saveUsers("Users.list");
            JOptionPane.showMessageDialog(null, "Requested");
            fis.close();
            fos.close();
            oos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SecureFileSharing.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DbxException ex) {
            Logger.getLogger(SecureFileSharing.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Please try again.");
        }
        
    }
    
    public static void approveRequest(String reqName){
        try {
            String[] arr = reqName.split("&");
            int requester = Integer.parseInt(arr[0]);
            String reqFile = arr[1];
            client.files().deleteV2("/" + users.get(currentUser).getUserName() + "/Requests/" + reqName);
            users.get(requester).removeSentReq(reqFile);
            users.get(requester).addAccess(reqFile);
            users.get(currentUser).removeRequest(reqName);
        } catch (DbxException ex) {
            Logger.getLogger(SecureFileSharing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void denyRequest(String reqName){
         try {
            String[] arr = reqName.split("&");
            int requester = Integer.parseInt(arr[0]);
            String reqFile = arr[1];
            client.files().deleteV2("/" + users.get(currentUser).getUserName() + "/Requests/" + reqName);
            users.get(requester).removeSentReq(reqFile);
            users.get(currentUser).removeRequest(reqName);
        } catch (DbxException ex) {
            Logger.getLogger(SecureFileSharing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean login_info(String userName, String password) {
        for (int i = 0; i < users.size(); i++) {
            if (userName.equals(users.get(i).getUserName()) && password.equals(users.get(i).getPassword())) {
                currentUser = i;
                return true;
            }
        }
        return false;
    }

    public static void saveUsers(String fname) {
        
        try {
            FileOutputStream fos = new FileOutputStream(fname);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(users);
            oos.flush();
            fos.close();
            oos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SecureFileSharing.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SecureFileSharing.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void loadUsers(String fname) {
        try {
            FileInputStream fis = new FileInputStream(fname);
            ObjectInputStream ois = new ObjectInputStream(fis);
            users = (ArrayList<User>) ois.readObject();
            fis.close();
            ois.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SecureFileSharing.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SecureFileSharing.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SecureFileSharing.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void accountLink() {
        try {
            DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
            client = new DbxClientV2(config, ACCESS_TOKEN);
            client.users().getCurrentAccount();
        } catch (DbxException ex) {
            accountLink();
        }
    }

    public static void uploadFile(){

        InputStream is = null;
        try {
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            int returnValue = jfc.showOpenDialog(null);
            File selectedFile = null;
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                selectedFile = jfc.getSelectedFile();
            }
            is = new FileInputStream(selectedFile);
            client.files().uploadBuilder("/" + users.get(currentUser).getUserName() 
                    + "/" + selectedFile.getName())
                    .uploadAndFinish(is);
            users.get(currentUser).addFileOwn(selectedFile.getName());
            SecureFileSharing.saveUsers("Users.list");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SecureFileSharing.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DbxException ex) {
            JOptionPane.showMessageDialog(null, "Please try again.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Please try again.");
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(SecureFileSharing.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

 
    public static void downloadFile(String fname, String path) {
        try {
            OutputStream downloadFile = new FileOutputStream(fname);
            try {
                client.files().downloadBuilder(path)
                        .download(downloadFile);
            } finally {
                downloadFile.close();
            }
        } //exception handled
        catch (DbxException e) {
            //error downloading file
            JOptionPane.showMessageDialog(null, "Unable to download file to local system\n Error: " + e);
        } catch (IOException e) {
            //error downloading file
            JOptionPane.showMessageDialog(null, "Unable to download file to local system\n Error: " + e);
        }
    }

    public static void main(String[] args) {
        accountLink();
        //list_folder_contents();
        //download_file("/test.txt");
        users = new ArrayList<>();
        loadUsers("Users.list");
        SecureFileSharingFrame sfsf = new SecureFileSharingFrame();
        //System.out.println(result.getEntries().get(0).toStringMultiline());

    }

}
