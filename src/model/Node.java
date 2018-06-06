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
    		temp.append(leftIndex + "," + "DUMMY" + rightIndex + "\n");
		}
		if (this.flag == Node.DELETE) {
			temp.append(leftIndex + "," + (leftIndex + context.length()) + "d" + rightIndex + "\n");
			temp.append("> \"" + context.toString() + "\""+ "\n");
		} else if (this.flag == Node.ADD) {
			temp.append(leftIndex + "a" + rightIndex + "," + (rightIndex + context.length())+ "\n");
			temp.append("> \"" + context.toString() + "\"" + "\n");
		}
		
		return temp.toString();
    }
    
}
