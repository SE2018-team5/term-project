package model;

import java.util.LinkedList;

public class MainWindowModel {

	private Boolean isBothLoaded = false;
	private Boolean isCompared = false;
	private Boolean isHighlighted = false;
	private LinkedList<Node> resultList=null;
	public LinkedList<Node> leftList = null;
	public LinkedList<Node> rightList = null;
	private int nodeNum = 0;
	
	
	public void add(String s,Node n) {
		if(s.compareTo("left")==0) {
			leftList.add(n);
		}else if (s.compareTo("right")==0){
			rightList.add(n);
		}else {
			System.out.println("no list");
		}
	}
	public void setNodeNumZero() {
		this.nodeNum=0;
	}
	public void increaseNodeNum() {
		this.nodeNum++;
	}
	public void decreaseNodeNum() {
		this.nodeNum--;
	}
	public int getNodeNum() {
		return this.nodeNum;
	}
	public LinkedList<Node> getLeftList(){
		return this.leftList;
	}
	public LinkedList<Node> getRightList(){
		return this.rightList;
	}
	public LinkedList<Node> getResultList(){
		return this.resultList;
	}
	public void setResultList(LinkedList<Node> resultList) {
		this.resultList = resultList;
	}
	
	public Boolean getIsHighlighted() {
		return this.isHighlighted;
	}
	public void setIsHighlighted(Boolean isHighlighted) {
		this.isHighlighted = isHighlighted;
	}
	
	public void setIsBothLoaded(Boolean isLoaded) {
		this.isBothLoaded = isLoaded;
	}
	public Boolean getIsBothLoaded() {
		return this.isBothLoaded;
	}
	
	public void setIsCompared(Boolean isCompared) {
		this.isCompared = isCompared;
	}
	public Boolean getIsCompared() {
		return this.isCompared;
	}
	
}
