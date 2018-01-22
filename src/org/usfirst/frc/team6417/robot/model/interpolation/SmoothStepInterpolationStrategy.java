package org.usfirst.frc.team6417.robot.model.interpolation;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class SmoothStepInterpolationStrategy implements InterpolationStrategy {
	double stepCount = 50.0;
	double currentStep = 0.0;

	private final double startVelocity;
	private final double endVelocity;
	
	public SmoothStepInterpolationStrategy(double startVelocity, double endVelocity){
		this.startVelocity = startVelocity;
		this.endVelocity = endVelocity; 
	}
	
	public SmoothStepInterpolationStrategy(double startVelocity, double endVelocity, int steps){
		this(startVelocity, endVelocity); 
		this.stepCount = steps;
	}
	
	@Override
	public double nextX() {
		double x = currentStep / stepCount;
		x = smoothstep(x);
		double velocity = (this.endVelocity * x) + (this.startVelocity * (1.0 - x));
		SmartDashboard.putNumber("Velocity", velocity);
		if(currentStep < stepCount) {
			currentStep++;
		}
		return velocity;
	}
	/**
	 * SmoothStep function from http://sol.gfxile.net/interpolation/index.html
	 * @param x
	 * @return smoothened value
	 */
	private double smoothstep(double x) {
		return ((x) * (x) * (3.0 - 2.0 * (x)));
	}
}
