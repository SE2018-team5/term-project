package model;

public class EditPanelModel {

	private Boolean isLoaded=false;
	private Boolean isModified=false;
	private StringBuffer text;
	
	public void loaded() {
		if(this.isLoaded)
			this.isLoaded = false;
		else
			this.isLoaded = true;

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
	
	public void setSB(String s) {
		this.text = new StringBuffer(s);
	}
	public void setSB(StringBuffer sb) {
		this.text = sb;
	}
	public String getSB() {
		return this.text.toString();
	}
	
}
