package model;

public class Node {
    public static final int DUMMY = 0;
    public static final int ADD = 1;
    public static final int DELETE = 2;
    public static final int CHANGED = 3;
    
    public int leftIndex;
    public int rightIndex;
    public int flag;
    
    public StringBuffer context;
    
    public Node(String context, int flag){
        this.context = new StringBuffer(context);
        
        this.flag = flag;
    }
    
    public Node(StringBuffer context, int leftIndex, int rightIndex, int flag){
        this.context = context;
        
        this.leftIndex = leftIndex;
        this.rightIndex = rightIndex;
        this.flag = flag;
    }
    
    public void addChar(char a) {
        context.insert(0, a);
    }
    
    @Override
    public String toString() {
    	StringBuffer temp = new StringBuffer();
    	if (this.flag == Node.DUMMY) {
			return "";
		}
		if (this.flag == Node.DELETE) {
			temp.append(leftIndex + "," + (leftIndex + context.length()) + "d" + rightIndex + "\r\n");
			temp.append("> \"" + context.toString() + "\""+ "\r\n");
		} else if (this.flag == Node.ADD) {
			temp.append(leftIndex + "a" + rightIndex + "," + (rightIndex + context.length())+ "\r\n");
			temp.append("> \"" + context.toString() + "\"" + "\r\n");
		}
		
		return temp.toString();
    }
    
}
