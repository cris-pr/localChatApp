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
	private Peer selectedPeer = null;
	private Client client = null;
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
		client = new Client(peerList,user);

		frame = new JFrame();
		frame.setBounds(100, 100, 880, 584);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.setConnected(true);
		client.listenServer();
		client.listenPeers();
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
		            if (!selectedPeer.getMessageList().isEmpty()) {
				
						String peerMessages = selectedPeer.getMessageListString();
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
					Message sentMessage = new Message(messageBox.getText(),client.getTheUser().getUsername());
					client.sendToPeer(sentMessage,selectedPeer,clientPort);
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
	
}
