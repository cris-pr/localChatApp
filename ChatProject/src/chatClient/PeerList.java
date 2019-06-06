//Created by Cristian Sanchez for Group 1
//PeerList.Java

//This class contains the list of peers that will be managed by the login window.
package chatClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.SortedSet;

public class PeerList implements Serializable{
    private ArrayList<Peer> peerList = new ArrayList<Peer>();
   
    private  String path="C:\\Users\\cris\\Desktop\\peerList.txt";
   //constructor
    public PeerList() {
       
    }
    //returns the list as an arrayList object
    public ArrayList<Peer> getList(){
        return peerList;
    }
    //adds a peer to the list
    public void addPeer(Peer p){
        peerList.add(p);
    }
    //Removes the peer from the peerlist when they log out
    public void removePeer(Peer p) {
        peerList.remove(p);
        System.out.println("New peer list saved to file.");
    }
    //Returns a peer
    public Peer getPeer(String userName) {
        for(Peer peer:peerList) {
            if(peer.getUsername().compareTo(userName)==0) {
                return peer;
            }
        }
        String noUser = "No user by that name.";
        Peer noPeer = new Peer(noUser,"0");
        return noPeer;
    }
    //Saves the peer list to a file
    public void saveListToFile() {
          try {
                FileOutputStream fileOut = new FileOutputStream(path);
                ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
                objectOut.writeObject(this);
                objectOut.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
    }
    //gets the peer list from a file
    public void getListFromFile() {
       
    }
    //converts the peerlist object into an array of bytes
    public byte[] getBytes()throws Exception{
        byte[] bytes = null;
        ByteArrayOutputStream byteArr = null;
        ObjectOutputStream oos = null;
        try{
            byteArr = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(byteArr);
            oos.writeObject(this);
            oos.flush();   
            bytes = byteArr.toByteArray();
        }finally {
            if (oos != null) {
                oos.close();
            }
            if (byteArr != null) {
                byteArr.close();
            }
        }
        return bytes;
       
    }
    //converts from an array of bytes to the peerlist object
    public static PeerList byteToPeer(byte[] bytes) throws IOException, ClassNotFoundException {
        Object obj = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            bis = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bis);
            obj = ois.readObject();
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (ois != null) {
                ois.close();
            }
        }
        return (PeerList) obj;
    }
    //prints the list of peers
    public void printList() {
        for(Peer aPeer: peerList) {
            System.out.println("Peer: "+ aPeer.getUsername());
        }
    }
    public void addMessageToPeer(Peer peer) {
    	getPeer(peer.getUsername());
    }
    
}