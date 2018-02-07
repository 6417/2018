package org.usfirst.frc.team6417.robot.model.interpolation;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class SmoothStepInterpolationStrategy implements InterpolationStrategy {
	private static final double epsilon = 0.03;
	private final double startVelocity;
	private final double endVelocity;

	private int maxSteps = 50;
	private int currentStep = 0;
	
	public SmoothStepInterpolationStrategy(double startVelocity, double endVelocity){
		this.startVelocity = startVelocity;
		this.endVelocity = endVelocity; 
	}
	
	public SmoothStepInterpolationStrategy(double startVelocity, double endVelocity, int steps){
		this(startVelocity, endVelocity); 
		this.maxSteps = steps;
	}
	
	@Override
	public double nextX() {
		double x = (double)currentStep / (double)maxSteps;
		x = smoothstep(x);
		double velocity = (this.endVelocity * x) + (this.startVelocity * (1.0 - x));
		if(velocity > 0 && velocity < epsilon) {
			velocity = 0;
		} else if(velocity < 0 && velocity > -epsilon) {
			velocity = 0;
		}
		
		SmartDashboard.putNumber("Velocity", velocity);
		if(currentStep < maxSteps) {
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

	@Override
	public boolean onTarget() {
		return currentStep >= maxSteps;
	}
}
