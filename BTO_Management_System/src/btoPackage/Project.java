package btoPackage;

public class Project {
	public enum FlatType { TWO_ROOM, THREE_ROOM; }
	
	private String projectName; 
	private String neighborhood; 
	private FlatType typesOfFlat;
	private String appOpeningDate; 
	private String appClosingDate; 
	private HDBManager managerInCharge; 
	private int officerSlots; 
	private boolean visibility; 
	
	public Project(String projectName, String neighborhood, FlatType typesOfFlat, 
			String appOpeningDate, String appClosingDate, HDBManager managerInCharge, 
			int officerSlots) {
		this.projectName = projectName; 
		this.neighborhood = neighborhood; 
		this.typesOfFlat = typesOfFlat; 
		this.appOpeningDate = appOpeningDate; 
		this.appClosingDate = appClosingDate; 
		this.managerInCharge = managerInCharge; 
		this.officerSlots = officerSlots; 
		visibility = false; 
	}
	
	
	public void toggleVisibility() {
		visibility = !visibility; 
	}
	
//	-- Getters and Setters
	public String getProjectName() {
		return projectName;
	}
	
	public String getNeighborhood() {
		return neighborhood;
	}
	
	public FlatType getTypesOfFlat() {
		return typesOfFlat;
	}
	
	public String getAppOpeningDate() {
		return appOpeningDate;
	}
	
	public String getAppClosingDate() {
		return appClosingDate;
	}
	
	private boolean getVisibility() {
		return visibility; 
	}
	
	public HDBManager getManagerInCharge() {
		return managerInCharge;
	}
	
	public int getOfficerSlots() {
		return officerSlots;
	}
	
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}
	
	
	public void setTypesOfFlat(FlatType typesOfFlat) {
		this.typesOfFlat = typesOfFlat;
	}
	
	
	public void setAppOpeningDate(String appOpeningDate) {
		this.appOpeningDate = appOpeningDate;
	}
	
	
	public void setAppClosingDate(String appClosingDate) {
		this.appClosingDate = appClosingDate;
	}
	
	
	public void setManagerInCharge(HDBManager managerInCharge) {
		this.managerInCharge = managerInCharge;
	}
	
	
	public void setOfficerSlots(int officerSlots) {
		this.officerSlots = officerSlots;
	}

//  Getters and Setters --
	
	
}
