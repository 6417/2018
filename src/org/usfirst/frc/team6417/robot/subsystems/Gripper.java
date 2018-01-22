package org.usfirst.frc.team6417.robot.subsystems;

import org.usfirst.frc.team6417.robot.Fridolin;
import org.usfirst.frc.team6417.robot.RobotMap;
import org.usfirst.frc.team6417.robot.commands.GripperStop;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The Gripper is able to pull-in or push-out the physical boxes.
 * Push and pull are not hard but smoothened between the start- and end-velocity.
 * Stopping is hard.
 */
public final class Gripper extends Subsystem {
	private final double PUSH_VELOCITY = 0.15;
	private final double PULL_VELOCITY = -0.15;
	private final double STOP_VELOCITY = 0;
	private final int STEP_COUNT = 100;
	
	private final Fridolin leftMotor;
	private final Fridolin rightMotor;

	int stepCount = STEP_COUNT;
	int currentStep = 0;

	public Gripper() {
		leftMotor = new Fridolin(RobotMap.MOTOR.GRIPPER_LEFT_PORT);
		rightMotor = new Fridolin(RobotMap.MOTOR.GRIPPER_RIGHT_PORT);		
	}
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new GripperStop());
	}
	
	/**
	 * Call this initialize method before running the push or pull.
	 * It sets back the counter so that the push or pull can perform
	 * a smooth transtion from the start position to the end position.
	 */
	public void initialize() {
		stepCount = STEP_COUNT;
		currentStep = 0;
	}
	
	/**
	 * Increments or keeps the positive velocity
	 */
	public void push() {
		doStep(PUSH_VELOCITY, STOP_VELOCITY);
	}

	/**
	 * Increments or keeps the negative velocity
	 */
	public void pull() {
		doStep(PULL_VELOCITY, STOP_VELOCITY);
	}

	/**
	 * Calculate the next velocity value using the smoothstep function
	 * between the start- and end-velocity.
	 * If the end-velocity is reached it maintains the velocity.
	 * @param startVelocity
	 * @param endVelocity
	 */
	private void doStep(double startVelocity, double endVelocity) {
		double v = currentStep / stepCount;
		v = smoothstep(v);
		setVelocity((endVelocity * v) + (startVelocity * (1 - v)));
		if(currentStep < stepCount) {
			currentStep++;
		}
	}
	
	public void stop() {
		setVelocity(STOP_VELOCITY);
	}
	
	private void setVelocity(double vel) {
		leftMotor.set(vel);
		rightMotor.set(vel);
		SmartDashboard.putNumber("Gripper velocity", vel);
	}
	

	/**
	 * SmoothStep function http://sol.gfxile.net/interpolation/index.html
	 * @param x
	 * @return smoothened value
	 */
	private static double smoothstep(double x) {
		return ((x) * (x) * (3 - 2 * (x)));
	}
	
}
