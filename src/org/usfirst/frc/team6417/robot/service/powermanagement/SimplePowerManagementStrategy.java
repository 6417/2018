package org.usfirst.frc.team6417.robot.service.powermanagement;

public final class SimplePowerManagementStrategy extends PowerManagementStrategy {

	private double x;
	
	public SimplePowerManagementStrategy(double x) {
		this.x = x;
	}
	
	@Override
	public double calculatePower() {
		return x*x;
	}

}
