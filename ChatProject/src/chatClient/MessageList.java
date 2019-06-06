package chatClient;

public class MessageList {
	private Message[] list;
	private int counter = 0;
	
	public MessageList() {
		list= new Message[200];
	}
	public Message[] getList() {
		return list;
	}
	public void setList(Message[] list) {
		this.list = list;
	}
	public int getCounter() {
		return counter;
	}
	public void setCounter(int counter) {
		this.counter = counter;
	}
	public void addMessage(Message m) {
		counter = counter++;
		list[counter]=m;
	}
	public Message getMessage(int i){
		return list[i];
	}
	public String toString() {
		String str = new String();
		String str2 = "\n";
		for(int i=0;i<counter;i++) {
			str.concat(list[i].toString());
			str.concat(str2);
		}
		return str;
	}
}
