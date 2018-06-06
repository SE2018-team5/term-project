package model;

public class StringBufferModel {
	
	StringBuffer Buffer = new StringBuffer();
	String string;
	
	public StringBufferModel(String s) {
		string = s;
		this.Buffer.append(string);
	}
	
	public void setStringBuffer(String s) {
		this.Buffer.delete(0 ,Buffer.length());
		this.Buffer.append(s);
	}
	
	public StringBuffer getStringBuffer() {
		return this.Buffer;
	}
}
