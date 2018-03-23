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
		initialValue = Robot.swerveDrive.frontLeft.velocityMotor.getSelectedSensorPosition(0);
		Robot.swerveDrive.drive(velocity, 0, 0);
	}
	
	@Override
	protected void execute() {
		if(isFwd) {
			if(Robot.swerveDrive.frontLeft.velocityMotor.getSelectedSensorPosition(0) >= initialValue + distanceInTicks) {
				Robot.swerveDrive.frontLeft.velocityMotor.set(RobotMap.VELOCITY.STOP_VELOCITY);
				Robot.swerveDrive.frontRight.velocityMotor.set(RobotMap.VELOCITY.STOP_VELOCITY);
				Robot.swerveDrive.backLeft.velocityMotor.set(RobotMap.VELOCITY.STOP_VELOCITY);
				Robot.swerveDrive.backRight.velocityMotor.set(RobotMap.VELOCITY.STOP_VELOCITY);
				isFinished = true;
			}
		}else {
			if(Robot.swerveDrive.frontLeft.velocityMotor.getSelectedSensorPosition(0) <= initialValue - distanceInTicks) {
				Robot.swerveDrive.frontLeft.velocityMotor.set(RobotMap.VELOCITY.STOP_VELOCITY);
				Robot.swerveDrive.frontRight.velocityMotor.set(RobotMap.VELOCITY.STOP_VELOCITY);
				Robot.swerveDrive.backLeft.velocityMotor.set(RobotMap.VELOCITY.STOP_VELOCITY);
				Robot.swerveDrive.backRight.velocityMotor.set(RobotMap.VELOCITY.STOP_VELOCITY);
				isFinished = true;
			}
		}
	
//		SmartDashboard.putNumber("FL-V - pos", Robot.swerveDrive.frontLeft.velocityMotor.getSelectedSensorPosition(0));
//		SmartDashboard.putNumber("NavX.Angle", Robot.navX.get().getAngle());		
//		SmartDashboard.putNumber("NavX.compass", Robot.navX.get().getCompassHeading());		
//		SmartDashboard.putNumber("NavX.DispX", Robot.navX.get().getDisplacementY());		
////		System.out.println("SwerveDriveStraight.execute("+(System.currentTimeMillis() - startTimestamp)+")");
//		if((System.currentTimeMillis() - startTimestamp) > 30000) {
//			Robot.swerveDrive.driveParallel(0, 0);
////			System.out.println("SwerveDriveStraight.execute done");
//			isFinished = true;
//		} else {
//			Robot.swerveDrive.driveParallel(0.5,0);// -Robot.navX.get().getDisplacementX());
////			System.out.println("SwerveDriveStraight.execute. Correcting displacement "+(-Robot.navX.get().getDisplacementX()));
//		}
	}	
	
	@Override
	protected boolean isFinished() {
		return isFinished;
	}

}
