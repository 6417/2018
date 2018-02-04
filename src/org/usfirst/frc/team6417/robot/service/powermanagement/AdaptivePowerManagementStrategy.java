package org.usfirst.frc.team6417.robot.service.powermanagement;

import org.usfirst.frc.team6417.robot.model.powermanagement.PowerExtremals;

public final class AdaptivePowerManagementStrategy extends PowerManagementStrategy {
	private double m;
	private double q;
	
	public AdaptivePowerManagementStrategy(PowerExtremals pe) {
		this.m = pe.a - pe.b;
		this.q = pe.b;
	}
	
	@Override
	public double calculatePower() {
//		TODO Uncomment the calculation. Make performance tests regarding the getThrottle method. nils
//		double t = OI.getInstance().joystickOne.getThrottle();
//		return m * t + q;
		return 1.0;
	}

}
