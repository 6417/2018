package org.usfirst.frc.team6417.robot.service.powermanagement;

public abstract class PowerManagementStrategy {
	/**
	 * Calculates the power-factor. This factor is used i.e. to adaptively control motors.
	 * It is not the power (x*x) if any value.
	 * @return value between 0 and 1
	 */
	public abstract double calculatePower();
}
