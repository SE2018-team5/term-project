package model;

public class EditPanelModel {

	private Boolean isLoaded=false;
	private Boolean isModified=false;
	private StringBuffer text;
	
	public void loaded() {
		isLoaded = true;
	}
	
	public Boolean getIsLoaded() {
		return this.isLoaded;
	}
	
	public void setIsModified(Boolean b) {
		this.isModified = b;
	}
	public Boolean getIsModified() {
		return this.isModified;
	}
	
	
	
}
