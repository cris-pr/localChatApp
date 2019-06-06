//Message.java

//This class contains the message information. Its able  to convert back and forth from bytes
// This makes it possible to send it through sockets in datagram packets.
package chatClient;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
public class Message implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date messageSentTime;
	private String messageContent;
	private String sender;
	private String receiver;
	private boolean received;
	private boolean acknowleged;
	private String messageType;
	//Message constructor
	public Message(String message,String sender) {
		messageContent = message;
		this.sender = sender;
		setMessageSentTime();
	}
	//sets the sender
	public void setSender(String sender) {
		this.sender = sender;
		
	}
	//sets the time when the message was sent
	public void setMessageSentTime() {
		this.messageSentTime = new Date();
	}
	//sets the different attributes of the message.
	public void setMessageInfo(String message, String aSender, String aReceiver) {
		this.messageContent = message;
		this.sender = aSender;
		this.receiver = aReceiver;
		setMessageSentTime();
	}
	//gets the content of the message
	public String getMessageContent() {
		return this.messageContent;
	}
	//gets the message sender's username
	public String getSender() {
		return this.sender;
	}
	//gets the message receiver's username
	public String getReceiver() {
		return this.receiver;
	}
	//sets whether the message was received
	public void setReceived(Boolean rec) {
		this.received = rec;
	}
	//sets whether the message was acknowledged
	public void setAcknowledged(Boolean rea) {
		this.received = acknowleged;
	}
	public boolean getReceived() {
		return received;
	}
	public boolean getAcknowleged() {
		return acknowleged;
	}
	public Date getMessageSentTime() {
		return this.messageSentTime;
	}
	public void setMessageType(String type) {
		String r = "received";
		String s = "sent";
		if ((type.compareTo(r) == 0) || (type.compareTo(s) == 0)) {
			messageType = type;
		}
	}
	public String getMessageType() {
		return this.messageType;
	}
	//returns a string to be displayed in the message list.
	public String toString() {
		return this.sender+":\n"+this.messageContent+"\n"+this.messageSentTime.toString()+"\n\n";
	}
	//returns a byte array of the Message object
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
	//Returns a message object from a byte array
	public static Message toMessage(byte[] bytes) throws IOException, ClassNotFoundException {
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
        return (Message) obj;
    }
	//Main function for testing
	public static void main(String[] args) {
		Message message = new Message("Hello there","Cristian");
		try {
			System.out.println("Initial Message: "+message.getMessageContent());
			byte[] bytes = message.getBytes();
			Message outMessage = Message.toMessage(bytes);
			System.out.println("Final message: "+outMessage.getMessageContent());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

