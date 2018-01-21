package org.usfirst.frc.team6417.robot.subsystems;

import org.usfirst.frc.team6417.robot.Fridolin;
import org.usfirst.frc.team6417.robot.RobotMap;
import org.usfirst.frc.team6417.robot.commands.GripperStop;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * The Gripper is able to pull-in or push-out the physical boxes.
 */
public final class Gripper extends Subsystem {
	private final double PUSH_VELOCITY = 0.15;
	private final double PULL_VELOCITY = -0.15;
	private final double STOP_VELOCITY = 0;
	
	private final Fridolin leftMotor;
	private final Fridolin rightMotor;

	public Gripper() {
		leftMotor = new Fridolin(RobotMap.MOTOR.GRIPPER_LEFT_PORT);
		rightMotor = new Fridolin(RobotMap.MOTOR.GRIPPER_RIGHT_PORT);		
	}
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new GripperStop());
	}
	
	public void push() {
		setVelocity(PUSH_VELOCITY);
	}

	public void pull() {
		setVelocity(PULL_VELOCITY);
	}
	
	public void stop() {
		setVelocity(STOP_VELOCITY);
	}
	
	private void setVelocity(double vel) {
		leftMotor.set(vel);
		rightMotor.set(vel);
	}

}
