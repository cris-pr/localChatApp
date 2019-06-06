package chatClient;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import chatClient.LoginGUI;


public class LoginGUI  {

	
	JFrame frame;
	JLabel updateState;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	
	static JButton btnLogin;
	
	
	int portNo = 40000;
	private InetAddress address;
	private DatagramSocket socket;
	private DatagramPacket packet;
	private byte[] sendBuf = new byte[256];
	private boolean connected = true;
	private PeerList peerList;
	private Peer user;
	LoginInfo info = new LoginInfo();

	DatagramSocket aSocket;



	
	 
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginGUI window = new LoginGUI();
					window.frame.setLocationRelativeTo(null);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public LoginGUI() {
		
		initialize();
	}
	public void initialize() {
		

		frame = new JFrame();
		frame.getContentPane().setBackground(Color.BLACK);
		frame.setBackground(Color.WHITE);
		frame.setUndecorated(true);
		frame.getContentPane().setBounds(new Rectangle(0, 0, 650, 420));
		frame.getContentPane().setLayout(null);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("Bahnschrift", Font.PLAIN, 11));
		lblUsername.setForeground(new Color(105, 105, 105));
		lblUsername.setBounds(351, 119, 112, 29);
		frame.getContentPane().add(lblUsername);
		
		JSeparator separator = new JSeparator();
		separator.setPreferredSize(new Dimension(0, 1));
		separator.setBackground(new Color(105, 105, 105));
		separator.setBounds(351, 178, 253, 14);
		frame.getContentPane().add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setPreferredSize(new Dimension(0, 1));
		separator_1.setBackground(SystemColor.controlDkShadow);
		separator_1.setBounds(351, 270, 253, 14);
		frame.getContentPane().add(separator_1);
		
		updateState = new JLabel("");
		updateState.setBounds(351, 282, 253, 20);
		frame.getContentPane().add(updateState);
		frame.setBounds(100, 100, 650, 420);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		
		txtUsername = new JTextField();
		
		
			
		txtUsername.setBorder(null);
		txtUsername.setBounds(351, 146, 253, 29);
		frame.getContentPane().add(txtUsername);
		txtUsername.setColumns(10);
		
		
		txtPassword = new JPasswordField();
		txtPassword.setEnabled(true);
		
		txtPassword.setBorder(null);
		txtPassword.setBounds(351, 240, 253, 29);
		frame.getContentPane().add(txtPassword);
		txtPassword.setEditable(true);
		
		JButton btnClose = new JButton("CLOSE");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent exitEvent) {
				try {
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.exit(0);			
				}
		});
		btnClose.setFont(new Font("Bahnschrift", Font.BOLD, 11));
		btnClose.setForeground(new Color(255, 255, 255));
		btnClose.setBackground(Color.LIGHT_GRAY);
		btnClose.setBorder(null);
		btnClose.setBounds(348, 307, 101, 41);
		frame.getContentPane().add(btnClose);
		
		//The login button sends the LoginInfo object to the server.
		btnLogin = new JButton("LOGIN");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
			
				if(txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty())
					updateState.setText("<html><font color =" + "red" + ">Make sure to fill both Usernanme/Paswword</font></html>");
				else {
					try {
						DatagramSocket aSocket = new DatagramSocket(40000);
					} catch (SocketException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					updateState.setText("");
					info.setUsername(txtUsername.getText());
					info.setPassword(txtPassword.getText());
					info.setType("Login");
					sendLogin(info,40000);
					listenValidator(40000);
						//JOptionPane.showMessageDialog(null,"match" );
				}
		
				
			}
		});
		btnLogin.setFont(new Font("Bahnschrift", Font.BOLD, 13));
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setBorder(null);
		btnLogin.setBackground(new Color(210, 105, 30));
		btnLogin.setBounds(459, 307, 145, 41);
		frame.getContentPane().add(btnLogin);
		
		JButton btnRegister = new JButton("REGISTER");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				updateState.setText("");
				info.setUsername(txtUsername.getText());
				info.setPassword(txtPassword.getText());
				info.setType("Registration");
				user = new Peer();
				user.setUsername(txtUsername.getText());
				user.setNotificationType("Registration");
				try {
					user.setIp(InetAddress.getByName("localhost").toString());
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("About to send login.");
				sendLogin(info,40000);
				System.out.print("Sent login");
				listenValidator(40000);
				
		
			}
		});
		btnRegister.setFont(new Font("Bahnschrift", Font.BOLD, 12));
		btnRegister.setForeground(Color.WHITE);
		btnRegister.setBorder(null);
		btnRegister.setBackground(new Color(51, 51, 204));
		btnRegister.setBounds(351, 359, 253, 41);
		frame.getContentPane().add(btnRegister);
		
		JLabel lblLogin = new JLabel("LOGIN");
		lblLogin.setHorizontalTextPosition(SwingConstants.CENTER);
		lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogin.setForeground(new Color(105, 105, 105));
		lblLogin.setFont(new Font("Bahnschrift", Font.BOLD, 19));
		lblLogin.setBorder(null);
		lblLogin.setBounds(424, 70, 78, 31);
		frame.getContentPane().add(lblLogin);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Bahnschrift", Font.PLAIN, 11));
		lblPassword.setForeground(SystemColor.controlDkShadow);
		lblPassword.setBounds(351, 212, 112, 29);
		frame.getContentPane().add(lblPassword);
		
		JLabel loginFrame = new JLabel("");
		loginFrame.setHorizontalAlignment(SwingConstants.CENTER);
		//loginFrame.setIcon(new ImageIcon(LoginGUI.class.getResource("/edu/pupr/proyectocd/img/Back2.png")));
		loginFrame.setBounds(0, 0, 650, 420);
		frame.getContentPane().add(loginFrame);
	}
	//Listens for the server's validation of the user login info.
	public void listenValidator(int port) {
		try {
			byte[] listData = new byte [1024];
			DatagramPacket listPacket = new DatagramPacket(listData,listData.length);
			aSocket = new DatagramSocket(port);
			System.out.println("about to run receive.");
			aSocket.receive(listPacket);
			System.out.println("ran receive.");
			peerList = PeerList.byteToPeer(listData);
			aSocket.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			chatWindow m = new chatWindow(peerList,user);
			m.frame.setVisible(true);
			frame.dispose();
	}
	
	//Sends login information of the user to the server.
	public void sendLogin(LoginInfo info,int port) {
		try {	
			aSocket = new DatagramSocket(40000);
			InetAddress anAddress= InetAddress.getByName("192.168.43.190");
			byte [] bytes = info.getBytes();
			DatagramPacket loginPacket = new DatagramPacket(bytes,bytes.length,anAddress,40000);
			aSocket.send(loginPacket);
			aSocket.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Could not connect to Server", "Login Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}

