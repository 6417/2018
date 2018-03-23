package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;
import org.usfirst.frc.team6417.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

public final class SwerveDriveStraight extends Command {
	private final int distanceInTicks;
	private boolean isFinished = false;
	private boolean isFwd;
	private double velocity = 0.2;
	private int initialValue;
	
	public SwerveDriveStraight(int distanceInTicks, boolean isFwd) {
		this.distanceInTicks = distanceInTicks;
		this.isFwd = isFwd;
		if(!isFwd) {
			velocity *= -1;
		}
		requires(Robot.swerveDrive);
	}
	
	@Override
	protected void initialize() {
		isFinished = false;
		initialValue = Robot.swerveDrive.frontRight.velocityMotor.getSelectedSensorPosition(0);
		Robot.swerveDrive.drive(velocity, 0, 0);
		System.out.println("SwerveDriveStraight.initialize()");
	}
	
	@Override
	protected void execute() {
		System.out.println("SwerveDriveStraight.execute(p os:"+Robot.swerveDrive.frontRight.velocityMotor.getSelectedSensorPosition(0)+", "+velocity+", isFWD:"+isFwd+")");
		if(isFwd) {
			if(Robot.swerveDrive.frontRight.velocityMotor.getSelectedSensorPosition(0) <= initialValue - distanceInTicks) {
				isFinished = true;
			}
		}else {
			if(Robot.swerveDrive.frontRight.velocityMotor.getSelectedSensorPosition(0) >= initialValue + distanceInTicks) {
				isFinished = true;
			}
		}

		if(isFinished) {
			Robot.swerveDrive.frontLeft.velocityMotor.set(RobotMap.VELOCITY.STOP_VELOCITY);
			Robot.swerveDrive.frontRight.velocityMotor.set(RobotMap.VELOCITY.STOP_VELOCITY);
			Robot.swerveDrive.backLeft.velocityMotor.set(RobotMap.VELOCITY.STOP_VELOCITY);
			Robot.swerveDrive.backRight.velocityMotor.set(RobotMap.VELOCITY.STOP_VELOCITY);
		}
		
	}	
	
	@Override
	protected boolean isFinished() {
		return isFinished;
	}

}
