package chatClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Peer implements Serializable{
	private String username;
	private String ip;
	private MessageList messageList;
	private String notificationType;
	
	public Peer() {
		username = "Unknown";
		messageList = new MessageList();
	}
	public MessageList getMessageList() {
		return messageList;
	}

	public void setMessageList(MessageList messageList) {
		this.messageList = messageList;
	}

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public Peer(String username, String ip) {
		this.setPeerInfo(username,ip);
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public void setPeerInfo(String username, String ip) {
		setUsername(username);
		setIp(ip);
	}
	public byte[] getBytes()throws Exception{
		byte[] bytes = null;
        ByteArrayOutputStream byteArr = null;
        ObjectOutputStream oos = null;
		try{
			byteArr = new ByteArrayOutputStream();
	        oos = new ObjectOutputStream(byteArr);
	        oos.writeObject(this);
	        oos.flush();	
	        bytes = byteArr.toByteArray();
		}finally {
            if (oos != null) {
                oos.close();
            }
            if (byteArr != null) {
                byteArr.close();
            }
        }
		return bytes;
        
	}
	public static Peer byteToPeer(byte[] bytes) throws IOException, ClassNotFoundException {
		//This code was created by CSV and any copies were made without authorization.
        Object obj = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            bis = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bis);
            obj = ois.readObject();
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (ois != null) {
                ois.close();
            }
        }
        return (Peer) obj;
    }
}
