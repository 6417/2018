package org.usfirst.frc.team6417.robot.model.powermanagement;

public final class PowerManagementPoint {
	public final double powerOfDrive;
	public final double powerOfLiftingUnit;
	public final double powerOfGripper;
	public final double powerOfLiftingUnitWagon;
	
	public PowerManagementPoint(
			double powerOfDrive, 
			double powerOfLiftingUnit, 
			double powerOfGripper,
			double powerOfLiftingUnitWagon) {
		this.powerOfDrive = powerOfDrive;
		this.powerOfLiftingUnit = powerOfLiftingUnit;
		this.powerOfGripper = powerOfGripper;
		this.powerOfLiftingUnitWagon = powerOfLiftingUnitWagon;
	}
	
}
