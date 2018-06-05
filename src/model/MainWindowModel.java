package model;

public class MainWindowModel {

	private static Boolean isBothLoaded = false;
	private static Boolean isCompared = false;
	private static Boolean isHighlighted = false;
	
	public static Boolean getIsHighlighted() {
		return isHighlighted;
	}
	public static void setIsHighlighted(Boolean isHighlighted) {
		MainWindowModel.isHighlighted = isHighlighted;
	}
	public static void setIsBothLoaded(Boolean isLoaded) {
		MainWindowModel.isBothLoaded = isLoaded;
	}
	public static void setIsCompared(Boolean isCompared) {
		MainWindowModel.isCompared = isCompared;
	}
	public static Boolean getIsBothLoaded() {
		return isBothLoaded;
	}
	public static Boolean getIsCompared() {
		return isCompared;
	}
	public static void compared() {
		isCompared = true;
	}
	public static void highlighted() {
		isHighlighted = true;
	}
	
}
