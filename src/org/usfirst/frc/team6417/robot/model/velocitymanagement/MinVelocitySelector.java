package org.usfirst.frc.team6417.robot.model.velocitymanagement;

public final class MinVelocitySelector {
	private final VelocityByPositionCalculator c;

	public MinVelocitySelector(VelocityByPositionCalculator c) {
		this.c = c;
	}
	
	public double calculateVelocity(int position, double currentVel) {
		double vel2 = c.calculateVelocity(position);
		if(currentVel < vel2) {
			return currentVel;
		}
		return vel2;
	}

	/**
	 * @param position
	 * @param currentVel
	 * @return the lower velocity
	 */
	public double calculateVelocityInv(int position, double currentVel) {
		double vel2 = c.calculateVelocityInv(position);
		if(currentVel < vel2) {
			return currentVel;
		}
		return vel2;
	}
}
