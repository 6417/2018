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
		double speed = -OI.getInstance().joystickOne.getY();
		Robot.swerveDrive.frontLeft.velocityMotor.set(speed);
		Robot.swerveDrive.frontRight.velocityMotor.set(speed);
		Robot.swerveDrive.backLeft.velocityMotor.set(speed);
		Robot.swerveDrive.backRight.velocityMotor.set(speed);
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}

}
