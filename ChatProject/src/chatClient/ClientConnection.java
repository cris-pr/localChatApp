package chatClient;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ClientConnection implements Runnable{

	@Override
	public void run() {
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
						//messageListContent.append("Received: "+received.toString());
						System.out.println(theUser.getUsername()+" Received message:\n");
						socket.close();
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		};
		
		listenPeers.start();
	}

}
