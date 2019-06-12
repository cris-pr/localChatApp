package chatServer;

import java.io.ByteArrayInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import chatClient.PeerList;
import chatClient.LoginInfo;
import chatClient.Message;
import chatClient.Peer;


public class Server {
    private static boolean running = true;
    private static int aPort = 40000;
    private PeerList peerList = new PeerList();
    private ArrayList<LoginInfo> loginList = new ArrayList<LoginInfo>();
    DatagramSocket socket;
    Thread listenThread;

   
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Server server = new Server();
        server.start(aPort);
    }
    public Server() {
    	this.start(aPort);
    }
    public void start(int port) {
        try {
            running = true;
            aPort = port;
            listen();
            System.out.println("Server started on Port, "+port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
    //Listens for user actions of type: login, logout, registration
    public void listen() {
        listenThread = new Thread("ChatProgram Listener") {
            public void run() {
                try {
                    while(running) {
                        //Create packet where user actions will be received
                        byte[] data = new byte [1200];
                        socket = new DatagramSocket(aPort);
                        DatagramPacket packet = new DatagramPacket(data,data.length);
                        System.out.println("Server running listen().");

                        socket.receive(packet);
                        System.out.println("Got something");

                        //Convert bytes in "data" to LoginInfo object
                        LoginInfo login= LoginInfo.toLoginInfo(data);
                        //get string that says what type of user action was taken.
                        String type = login.getType();
                        System.out.println("type: "+ type);
                        Peer loginPeer = new Peer(login.getUsername(),packet.getAddress().getHostAddress());
                        socket.close();
                        if(type.compareTo("Login")==0) {
                            if(loginValid(login)) {
                                peerList.addPeer(loginPeer);
                                loginPeer.setNotificationType("Login");
                                sendPeerList(loginPeer);
                            }
                        }else if(type.compareTo("Logout")==0) {//if the user action is logout, removes user from
                                                                //peer list and sends logout notification to all peers
                            String logoutUserName = login.getUsername();//getUserName()
                            loginPeer.setNotificationType("Logout");
                            peerList.removePeer(peerList.getPeer(login.getUsername()));
                           
                        }else if(type.compareTo("Registration")==0) {//if the user action is registration, adds to list
                                                                    // of logins and adds new peer to online peers
                            //checks if registration is valid
                                loginPeer.setNotificationType("Registration");
                                loginList.add(login);
                                peerList.addPeer(loginPeer);
                                Thread.sleep(1000);
                               
                                sendPeerList(loginPeer);
                                System.out.println("Did registration code");

                            }
                        else { //if the action is not valid, throws exception
                            throw new Exception("Invalid Action Type");
                        }
                        sendNotification(loginPeer); //sends notification based on action type.
                    }
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        };listenThread.start();
    }
   
    private  boolean loginValid(LoginInfo newLogin) {
        for(LoginInfo login: this.loginList) {
            if(login.checkLogin(newLogin)) {//checkLogin compares the userName and password
                                            //and returns true if they are both  the same                                                                    //This code was created by Cristian for group 1 and any copies were made without authorization.
                return true;
            }
        }
       
        return false;
    }
    public static void stop() {
        running = false;
    }
   
    public void sendNotification(Peer newPeer) {
        DatagramPacket packet = null;
        try {
           
            byte[] data = newPeer.getBytes();
            for(Peer aPeer: peerList.getList()) {
                if(!aPeer.equals(newPeer)) {
                    packet = new DatagramPacket(data,data.length,InetAddress.getByName(aPeer.getIp()),aPort);
                    this.socket = new DatagramSocket(aPort);
                    this.socket.send(packet);
                    System.out.println("Sent Message to all peers about "+ newPeer.getUsername());
                    socket.close();
                }
            }
           
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void sendPeerList(Peer newPeer) {
        DatagramPacket packet = null;
        try {
            byte[] data = peerList.getBytes();
            Thread.sleep(5000);
            packet = new DatagramPacket(data,data.length,InetAddress.getByName(newPeer.getIp()),40000);
            this.socket = new DatagramSocket(40000);
            this.socket.send(packet);
            System.out.println("Sent peerList");
            peerList.printList();
            socket.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

}

