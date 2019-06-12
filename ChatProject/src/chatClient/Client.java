package chatClient;

import static org.junit.Assert.assertEquals;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

public class Client{
	private Peer theUser;
	int clientPort = 50000;
	int serverPort = 40000;
	private InetAddress address;
	private DatagramSocket socket = null;
	private DatagramSocket serverSocket = null;
	private DatagramPacket packet;
	private byte[] sendBuf = new byte[256];
	private boolean connected = true; 
	private PeerList peerList;
	private Peer selectedPeer = null;
	DefaultListModel<String> listModel = new DefaultListModel<String>();
	int modelIndex = 0;
	Thread listenServer;
	Thread listenPeers;
	
	public Client(PeerList aPeerList, Peer user){
		this.setPeerList(aPeerList);
		this.setTheUser(user);
		connected = true;
	}
	public void run() {
		listenServer();
		listenPeers();
	}
	public void listenServer() {
		listenServer = new Thread("Receiving") {
			public void run() {
				try {
					while(connected) {
						System.out.println("Running listen().");
						byte[] data = new byte [1024];
						serverSocket = new DatagramSocket(serverPort);
						DatagramPacket packet = new DatagramPacket(data,data.length);
						serverSocket.receive(packet);
						Peer newPeer = Peer.byteToPeer(data);
						System.out.println("Received Notification for: "+newPeer.getUsername()+" of type "+ newPeer.getNotificationType());
						if(newPeer.getNotificationType().compareTo("Registration")==0 || newPeer.getNotificationType().compareTo("Login")==0) {
							System.out.println("Entered If");

							if(listModel.get(0).compareTo("No Peers Online")==0) {
								listModel.remove(0);
								System.out.println("Removed No PeersOnline");

							}
							peerList.addPeer(newPeer);
							listModel.add(modelIndex,newPeer.getUsername());
							modelIndex++;
							System.out.println("Added to model");

						}
						else if(newPeer.getNotificationType().compareTo("Logout")==0) {
							peerList.removePeer(newPeer);
							
							for(int i = 0; i<=modelIndex;i++) {
								if(listModel.get(i).compareTo(newPeer.getUsername())==0) {
									listModel.remove(i);
								}
							}
							
							System.out.println("Removed Peer.");
							renewPeerList();
						}
						serverSocket.close();
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		};listenServer.start();
	}
	public void listenPeers() {
		listenPeers = new Thread(theUser.getUsername()) {
			public void run() {
				try {
					while(connected) {
						System.out.println("Running listen(). At ip: "+ theUser.getIp()+ " at port "+clientPort);
						byte[] data = new byte [1024];
						socket = new DatagramSocket(clientPort);
						DatagramPacket packet = new DatagramPacket(data,data.length);
						
						socket.receive(packet);
						
						Message received = Message.toMessage(data);
						for(Peer aPeer:peerList.getList()) {
							if(aPeer.getUsername().compareTo(received.getSender())==0) {
								aPeer.getMessageList().add(received);
								System.out.println("Added message to list of "+ aPeer.getUsername()+ "\nMessage Content: "+ received.getMessageContent());
							}
						}
						socket.close();
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		};
		
		listenPeers.start();
	}
	public void disconnect() {
		connected = false;
	}
	public void receiveAck() {
		Timer timer = new Timer();
		class ResendDatagram extends TimerTask {
			public void run() {
				
			}
		}
		TimerTask task = new ResendDatagram();
		
	}
	/*
	 * //When the user double clicks on a peer, this function shows the messages on
	 * the messageList area. 
	 * public void showMessages(Peer aPeer){
	 * messageListContent.setText(aPeer.getMessageList().toString()); }
	 */
	
	public void renewPeerList() {
		
	}
	public void sendToPeer(Message message,Peer peer, int port) {
		try {
			
			InetAddress address = InetAddress.getByName(peer.getIp());
			message.setSender(theUser.getUsername());
			byte [] bytes = message.getBytes();
			DatagramPacket packet = new DatagramPacket(bytes,bytes.length,address,port);
			DatagramSocket socket = new DatagramSocket();
			System.out.println("Sending message to: "+ peer.getUsername()+" at ip: "+peer.getIp()+" and port" + port);
			socket.send(packet);
			socket.close();
			receiveAck();
			addMessageToList(message, peer);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not connect to Peer", "Communication Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	public void addMessageToList(Message message, Peer peer) {
		peerList.getPeer(peer.getUsername()).getMessageList().add(message);
	}
	
	public Peer getTheUser() {
		return theUser;
	}
	public void setTheUser(Peer theUser) {
		this.theUser = theUser;
	}
	public int getClientPort() {
		return clientPort;
	}
	public void setClientPort(int clientPort) {
		this.clientPort = clientPort;
	}
	public int getServerPort() {
		return serverPort;
	}
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	public InetAddress getAddress() {
		return address;
	}
	public void setAddress(InetAddress address) {
		this.address = address;
	}
	public DatagramSocket getSocket() {
		return socket;
	}
	public void setSocket(DatagramSocket socket) {
		this.socket = socket;
	}
	public DatagramSocket getServerSocket() {
		return serverSocket;
	}
	public void setServerSocket(DatagramSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	public DatagramPacket getPacket() {
		return packet;
	}
	public void setPacket(DatagramPacket packet) {
		this.packet = packet;
	}
	public byte[] getSendBuf() {
		return sendBuf;
	}
	public void setSendBuf(byte[] sendBuf) {
		this.sendBuf = sendBuf;
	}
	public boolean isConnected() {
		return connected;
	}
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	public PeerList getPeerList() {
		return peerList;
	}
	public void setPeerList(PeerList peerList) {
		this.peerList = peerList;
	}
	public Peer getSelectedPeer() {
		return selectedPeer;
	}
	public void setSelectedPeer(Peer selectedPeer) {
		this.selectedPeer = selectedPeer;
	}
	public DefaultListModel<String> getListModel() {
		return listModel;
	}
	public void setListModel(DefaultListModel<String> listModel) {
		this.listModel = listModel;
	}
	public int getModelIndex() {
		return modelIndex;
	}
	public void setModelIndex(int modelIndex) {
		this.modelIndex = modelIndex;
	}
	public void setUp() throws Exception {
		
	}
		
}
