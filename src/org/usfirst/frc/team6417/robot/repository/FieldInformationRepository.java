package org.usfirst.frc.team6417.robot.repository;

public final class FieldInformationRepository {
	private String fieldInfo = "XXX";
	
	private static FieldInformationRepository INSTANCE;
	public static FieldInformationRepository getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new FieldInformationRepository();
		}
		return INSTANCE;
	}
	
	private FieldInformationRepository() {;}
	
	public void setFieldInfo(String info) {
		if(info.trim().length() != 3) {
			throw new IllegalArgumentException("Field information consists of 3 characters");
		}
		this.fieldInfo = info.trim();		
	}
	
	public boolean isFirstSwitchLeft() {
		return compareFieldInformation(0) ;
	}

	public boolean isSecondSwitchLeft() {
		return compareFieldInformation(2) ;
	}
	
	public boolean isScaleLeft() {
		return compareFieldInformation(1) ;
	}

	private boolean compareFieldInformation(int index) {
		return "L".compareTo(fieldInfo.substring(index, 1)) == 0;
	}
	
}
