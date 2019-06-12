package chatServer;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chatClient.Message;
import chatClient.Peer;
import chatClient.PeerList;

public class ServerTest {
	int peer1Port = 40001;
	int peer2Port = 40002;
	int peer3Port = 40003;

	Peer peer1 = new Peer();
	Peer peer2 = new Peer();
	Peer peer3 = new Peer();
	
	PeerList peerList1 = new PeerList();
	PeerList peerList2 = new PeerList();
	PeerList peerList3 = new PeerList();
	
	String msg1String = new String("Hello Im peer1");
	String msg2String = new String("Hello Im peer2");
	String msg3String = new String("Hello Im peer3");

	Message message1 = null;
	Message message2 = null;
	Message message3 = null;

	@Before
	public void setUp() throws Exception {
		peer1.setPeerInfo("Peer1", "localhost");
		peer2.setPeerInfo("Peer2", "localhost");
		peer3.setPeerInfo("Peer3", "localhost");
		
		peerList1.addPeer(peer2);
		peerList1.addPeer(peer3);
		
		peerList2.addPeer(peer1);
		peerList2.addPeer(peer3);
		
		peerList3.addPeer(peer1);
		peerList3.addPeer(peer2);
		
		message1 = new Message(msg1String, peer1.getUsername());
		message2 = new Message(msg2String, peer2.getUsername());
		message3 = new Message(msg3String, peer3.getUsername());
		
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void sendPeerListTest() {
		fail("Not yet implemented");

	}
	
	@Test
	public void sendNotificationTest() {
		fail("Not yet implemented");
	}
	
	@Test
	public void loginValidTest() {
		

	}
	
	@Test
	public void listenTest() {
		fail("Not yet implemented");
	}
	@Test
	public void startTest() {
		fail("Not yet implemented");
	}
}
