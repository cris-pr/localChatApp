package chatClient;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServerConnection implements Runnable{

	@Override
	public void run() {
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

}
