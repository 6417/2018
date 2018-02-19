package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.MotorController;
import org.usfirst.frc.team6417.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public final class SwerveDriveWheelCheck extends Command {

	
	public SwerveDriveWheelCheck() {
		requires(Robot.swerveDrive);
	}
	
	@Override
	protected void execute() {
		checkMotors(Robot.swerveDrive.frontLeft.angleMotor, Robot.swerveDrive.frontLeft.velocityMotor);
		checkMotors(Robot.swerveDrive.frontRight.angleMotor, Robot.swerveDrive.frontRight.velocityMotor);
		checkMotors(Robot.swerveDrive.backLeft.angleMotor, Robot.swerveDrive.backLeft.velocityMotor);
		checkMotors(Robot.swerveDrive.backRight.angleMotor, Robot.swerveDrive.backRight.velocityMotor);
	}

	private void checkMotors(MotorController angleMotor, MotorController velocityMotor) {
		checkMotor(angleMotor);
		checkMotor(velocityMotor);
	}

	private void checkMotor(MotorController motor) {
		motor.set(0.3);
		Timer.delay(5);
		motor.set(0.0);
		Timer.delay(2);
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

}
