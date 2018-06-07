package model;

public class Node {
    public static final int DUMMY = 0;
    public static final int ADD = 1;
    public static final int DELETE = 2;
    
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
    	
    	assert this.context != null;
    	
    	if (this.flag == Node.DUMMY) {
    		if(leftIndex == -1) {
    			temp.append(leftIndex + "DUM" + rightIndex + ", " + (rightIndex + context.length()) + "\n");
    		} else {
    			temp.append(leftIndex + "," + (leftIndex + context.length()) + "DUM" + rightIndex + "\n");
    		}
    		
		}
		if (this.flag == Node.DELETE) {
			temp.append(leftIndex + "," + (leftIndex + context.length()) + "d" + rightIndex + "\n");
		} else if (this.flag == Node.ADD) {
			temp.append(leftIndex + "a" + rightIndex + "," + (rightIndex + context.length())+ "\n");
		}
		
		temp.append("> \"" + context.toString() + "\""+ "\n");
		
		return temp.toString();
    }
    
    @Override
    public boolean equals(Object o) {
    	Node node = null;
    	
    	if(o instanceof Node) {
    		node = (Node) o;
    	} else {
    		return super.equals(o);
    	}
    	
    	if(this.flag == Node.ADD && this.rightIndex == node.rightIndex) {
			return this.context.toString().equals(node.context.toString());
    	}
    	
    	if(this.flag == Node.DELETE && this.leftIndex == node.leftIndex) {
			return this.context.toString().equals(node.context.toString());
    	}
    	
    	if(this.flag == Node.DUMMY && this.leftIndex == node.leftIndex && this.leftIndex != -1) {
			return this.context.toString().equals(node.context.toString());
    	}
    	
    	if(this.flag == Node.DUMMY && this.rightIndex == node.rightIndex && this.rightIndex != -1) {
			return this.context.toString().equals(node.context.toString());
    	}
    	
    	return false;
    }
}


