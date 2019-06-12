package chatClient;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import chatServer.Server;

public class ClientTest {
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
	public void testClientConstructor() {
		
		Client client1 = new Client(peerList1,peer1);
		Client client2 = new Client(peerList2,peer2);
		Client client3 = new Client(peerList3,peer3);
		
	}
	@Ignore
	public void testlisten2Server() {
		Server server = new Server();
		
	}
	
	@Test
	public void testListen() throws Exception{
		Client client1 = new Client(peerList1,peer1);
		Client client2 = new Client(peerList2,peer2);
		Client client3 = new Client(peerList3,peer3);
		
		client1.setClientPort(40001);
		client2.setClientPort(40002);
		client3.setClientPort(40003);

		
		client1.listenPeers();
		client2.listenPeers();
		client3.listenPeers();
		Thread.sleep(500);
		client2.sendToPeer(message2, peer1, peer1Port);
		Thread.sleep(100);
		client1.sendToPeer(message1, peer3, peer3Port);

		assertEquals(message2.toString(),client1.getPeerList().getPeer(peer2.getUsername()).getMessageList().get(0).toString());
		}
}