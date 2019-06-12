package chatClient;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
public class PeerTest {
//setUp method includes the creation of three peers that will be used in the testing
	int peer1Port = 40001;
	int peer2Port = 40002;
	int peer3Port = 40003;

	Peer peer1 = new Peer();
	Peer peer2 = new Peer();
	Peer peer3 = new Peer();
	@Before
	public void setUp() throws Exception {

		

		
	}

	@After
	public void tearDown() throws Exception {
	}
//The following method tests whether a peer can receive the info about another peer from the server
	@Test
	public void peerTest() {
		int peer1Port = 40001;
		int peer2Port = 40002;
		int peer3Port = 40003;
		Peer peer1 = new Peer();
		Peer peer2 = new Peer();
		Peer peer3 = new Peer();
		peer1.setPeerInfo("Peer1", "local_host");
		peer2.setPeerInfo("Peer2", "local_host");
		peer3.setPeerInfo("Peer3", "local_host");
		
		peer1.addMessage(new Message("Hola.","Jose"));
		peer1.addMessage(new Message("Adios.","Pedro"));
		System.out.println(peer1.getMessageListString());
		
	}

}
