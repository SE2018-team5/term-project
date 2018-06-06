package model;
public class MatchNode{
	Node left;
	Node right;
	
	public MatchNode (Node left, Node right){
		assert left.leftIndex != -1;
		assert right.rightIndex != -1;
		this.left = left;
		this.right = right;
	}
	
	public boolean equalsOnLeft(Node left) {
		assert left.leftIndex != -1;
		assert left.flag == Node.DELETE || left.flag == Node.DUMMY;
		
		return this.left.equals(left);
	}
	
	public boolean equalsOnRight(Node right) {
		assert right.rightIndex != -1;
		assert right.flag == Node.ADD || right.flag == Node.DUMMY;
		
		return this.right.equals(right);
	}
}