package chatClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class LoginInfo implements Serializable{

	
	
	private String Username;
	private String Password;
	private String Type;
	
	public LoginInfo() {
		
	}
	public String getUsername() {
		return Username;
	}
	
	
	public void setUsername(String username) {
		Username = username;
	}
	
	public String getPassword() {
		return Password;
	}
	
	public void setPassword(String password) {
		Password = password;
	}
	
	public String getType() {
		return Type;
	}


	public void setType(String type) {
		Type = type;
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
	public static LoginInfo toLoginInfo(byte[] bytes) throws IOException, ClassNotFoundException {
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
        return (LoginInfo) obj;
    }
	public boolean checkLogin(LoginInfo login) {
		return (this.getPassword().equals(login.getPassword())&&this.getUsername().equals(login.getUsername()));
	}
}
