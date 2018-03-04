package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.OI;
import org.usfirst.frc.team6417.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class SwerveDriveRotateAll extends Command {

	
	public SwerveDriveRotateAll() {
		requires(Robot.swerveDrive);
	}
	
	@Override
	protected void execute() {
		SmartDashboard.putBoolean("SwerveDriveRotateAll", true);
		double speed = -OI.getInstance().joystickOne.getY() * 0.5;
//		Robot.swerveDrive.frontLeft.angleMotor.set(speed);
//		Robot.swerveDrive.frontRight.angleMotor.set(speed);
//		Robot.swerveDrive.backLeft.angleMotor.set(speed);
		Robot.swerveDrive.backRight.angleMotor.set(speed);
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}

}
