package org.usfirst.frc.team6417.robot.model.velocitymanagement;

public final class VelocityByPositionCalculator {
	private final double m;
	private final double q;
	
	public VelocityByPositionCalculator(double v1, int pos1, double v2, int pos2) {
		this.m = (v2 - v1)/( pos2 - pos1);
		this.q = v1;
	}
	
	public double calculateVelocity(int position) {
		return m * position + q;
	}

	public double calculateVelocityInv(int position) {
		return 1.0 - calculateVelocity(position);
	}
}
