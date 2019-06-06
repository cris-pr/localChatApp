//chatWindow.java
//Created by Cristian Sanchez for Group 1

//This class creates the chat's main window. It opens after the user is authenticated by the server. It contains the following sections:
//A peer list, a message box, a message list and a send button. 

package chatClient;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JList;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.AbstractListModel;
import javax.swing.JPopupMenu;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.ScrollPane;
import java.awt.TextArea;
import java.awt.Panel;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.awt.event.ActionEvent;

public class chatWindow {

	JFrame frame;
	JList list;
	private Peer theUser;
	int clientPort = 50000;
	int serverPort = 40000;
	private InetAddress address;
	private DatagramSocket socket = null;
	private DatagramSocket serverSocket = null;
	private DatagramPacket packet;
	private byte[] sendBuf = new byte[256];
	private boolean connected = false; 
	private PeerList peerList;
	private Peer selectedPeer = null;
	DefaultListModel<String> listModel = new DefaultListModel<String>();
	int modelIndex = 0;

	JTextArea messageListContent;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					chatWindow window = new chatWindow(new PeerList(),new Peer());
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public chatWindow(PeerList peerList,Peer user) {
		initialize(peerList, user);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(PeerList peerList, Peer user) {
		this.peerList = peerList;
		this.theUser = user;

		frame = new JFrame();
		frame.setBounds(100, 100, 880, 584);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		connected = true;
		listen2Server(serverPort);
		listen(clientPort);
		if(peerList.getList().size()>0) {
			for(Peer aPeer:peerList.getList()) {
				if(!(aPeer.getUsername().compareTo(theUser.getUsername())==0)){
					listModel.add(modelIndex,aPeer.getUsername());
					modelIndex++;
				}
				if(modelIndex>0) {
					list = new JList<String>(listModel);
				}
				else {
					listModel.add(0, "No Peers Online");
					list = new JList<String>(listModel);
				}
			}
		}
		else {
			listModel.add(0, "No Peers Online");
			list = new JList<String>(listModel);	
		}
		
		list.setFont(new Font("Tahoma", Font.PLAIN, 14));
		list.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent evt) {
		        JList<String> list = (JList<String>)evt.getSource();
		        if (evt.getClickCount() == 2) {
		        	
		            // When the user double clicks on the peer list, the messageList gets populated
		        	// with the messages from that peers conversation.
		            int index = list.locationToIndex(evt.getPoint());
		            String peerName = list.getComponent(index).toString();
		            selectedPeer = peerList.getPeer(peerName);
		            if (selectedPeer.getMessageList().getCounter() >0) {
						MessageList peerMessageList = selectedPeer.getMessageList();
						String peerMessages = peerMessageList.toString();
						messageListContent.setText(peerMessages);
					}
		            
		        }
		    }
		});		
		JLabel lblPeers = new JLabel("Peers");
		lblPeers.setFont(new Font("Tahoma", Font.PLAIN, 22));
		
		JLabel lblmessageListContent = new JLabel("Message List:");
		lblmessageListContent.setFont(new Font("Tahoma", Font.PLAIN, 22));
		
		JLabel lblMessage = new JLabel("Message:");
		lblMessage.setFont(new Font("Tahoma", Font.PLAIN, 22));
		
		messageListContent = new JTextArea();
		messageListContent.setLineWrap(true);
		messageListContent.setFont(new Font("Monospaced", Font.PLAIN, 15));
		messageListContent.setEditable(false);
		
		JTextArea messageBox = new JTextArea();
		messageBox.setLineWrap(true);
		messageBox.setToolTipText("Enter your text here");
		
		messageBox.setRows(5);
		
		JButton send = new JButton("Send");
		//When the send button is clicked, the contents of the message box are cleared and sent to the selected peer.
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(selectedPeer != null) {
					Message sentMessage = new Message(messageBox.getText(),theUser.getUsername());
					sendToPeer(sentMessage,selectedPeer);
					messageListContent.append("Sent:"+sentMessage.toString());
					messageBox.setText(null);
				}
				
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(messageListContent,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(35)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblPeers, GroupLayout.PREFERRED_SIZE, 217, GroupLayout.PREFERRED_SIZE)
						.addComponent(list, GroupLayout.PREFERRED_SIZE, 227, GroupLayout.PREFERRED_SIZE))
					.addGap(48)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblmessageListContent, GroupLayout.PREFERRED_SIZE, 217, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblMessage, GroupLayout.PREFERRED_SIZE, 217, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(messageBox, GroupLayout.PREFERRED_SIZE, 474, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(send))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(messageListContent, GroupLayout.PREFERRED_SIZE, 530, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(17))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblmessageListContent, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPeers, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(messageListContent, GroupLayout.PREFERRED_SIZE, 308, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(lblMessage, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
								.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(send, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(messageBox, GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE))
							.addGap(14))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(list, GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
							.addContainerGap())))
		);
		frame.getContentPane().setLayout(groupLayout);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("New menu item");
		mnFile.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("New menu item");
		mnFile.add(mntmNewMenuItem_1);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
	}
	//Opens a socket and sends a message to a peer
	public void sendToPeer(Message message,Peer peer) {
		try {
			
			address= InetAddress.getByName(peer.getIp());
			message.setSender(theUser.getUsername());
			byte [] bytes = message.getBytes();
			packet = new DatagramPacket(bytes,bytes.length,address,clientPort);
			socket.send(packet);
			receiveAck();
			peerList.getPeer(peer.getUsername()).getMessageList().addMessage(message);
			showMessages(peer);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Could not connect to Server", "Login Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	// The listen function is always running in the background for messages from peers. When a message is received, the message
	//is added to the sender's messageList
	public void listen(int port) {
		Thread listenThread = new Thread("Receiving") {
			public void run() {
				try {
					while(connected) {
						System.out.println("Running listen().");
						byte[] data = new byte [1024];
						socket = new DatagramSocket(50000);
						DatagramPacket packet = new DatagramPacket(data,data.length);
						socket.receive(packet);
						
						Message received = Message.toMessage(data);
						for(Peer aPeer:peerList.getList()) {
							if(aPeer.getUsername().compareTo(received.getSender())==0) {
								aPeer.getMessageList().addMessage(received);
							}
						}
						//messageListContent.append("Received: "+received.toString());
						System.out.println("Received message.");
						socket.close();
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		};listenThread.start();
	}
	public void listen2Server(int serverPort) {
		Thread listenThread = new Thread("Receiving") {
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
		};listenThread.start();
	}
	//Receives acknowledgement from peer.
	public void receiveAck() {
		Timer timer = new Timer();
		class ResendDatagram extends TimerTask {
			public void run() {
				
			}
		}
		TimerTask task = new ResendDatagram();
		
	}
	//When the user double clicks on a peer, this function shows the messages on the messageList area.
	public void showMessages(Peer aPeer){
		messageListContent.setText(aPeer.getMessageList().toString());
	}
	public void disconnect() {
		connected = false;
	}
	public void renewPeerList() {
		
	}
	
}
