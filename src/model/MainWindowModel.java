package model;

import java.util.LinkedList;

public class MainWindowModel {

	private Boolean isBothLoaded = false;
	private Boolean isCompared = false;
	private Boolean isHighlighted = false;
	private LinkedList<Node> resultList=null;
	private int nodeNum = 0;
	
	public void initResultNode() {
		resultList = new LinkedList<Node>();
	}
	public Node getPresentNode() {
		return this.resultList.get(nodeNum);
	}
	public void add(Node e1) {
		this.resultList.add(e1);
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
