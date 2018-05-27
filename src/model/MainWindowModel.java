package model;

public class MainWindowModel {
	private static Boolean isLoaded = false;
	private static Boolean isCompared = false;
	
	
	public static Boolean getIsLoaded() {
		return isLoaded;
	}
	public static Boolean getIsCompared() {
		return isCompared;
	}
	public static void loaded() {
		isLoaded = true;
	}
	public static void compared() {
		isCompared = true;
	}
}
