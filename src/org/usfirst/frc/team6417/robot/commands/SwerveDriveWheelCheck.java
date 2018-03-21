package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.MotorController;
import org.usfirst.frc.team6417.robot.Robot;
import org.usfirst.frc.team6417.robot.subsystems.SwerveWheelDrive;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public final class SwerveDriveWheelCheck extends Command {

	
	public SwerveDriveWheelCheck() {
		requires(Robot.swerveDrive);
	}
	
	@Override
	protected void execute() {
//		System.out.println("SwerveDriveWheelCheck. This is goint to take while ...");
		
		checkMotors("FL", Robot.swerveDrive.frontLeft.angleMotor, Robot.swerveDrive.frontLeft.velocityMotor);
		checkMotors("FR", Robot.swerveDrive.frontRight.angleMotor, Robot.swerveDrive.frontRight.velocityMotor);
		checkMotors("BL", Robot.swerveDrive.backLeft.angleMotor, Robot.swerveDrive.backLeft.velocityMotor);
		checkMotors("BR", Robot.swerveDrive.backRight.angleMotor, Robot.swerveDrive.backRight.velocityMotor);
//		
//		checkZeroPoint("FL", Robot.swerveDrive.frontLeft);
//		checkZeroPoint("FR", Robot.swerveDrive.frontRight);
//		checkZeroPoint("BL", Robot.swerveDrive.backLeft);
//		checkZeroPoint("BR", Robot.swerveDrive.backRight);

		System.out.println("SwerveDriveWheelCheck. Done.");
	}
	
	private void checkZeroPoint(String label, SwerveWheelDrive swerveWheelDrive) {
//		System.out.print("Checking "+label+" position sensor ...");
		swerveWheelDrive.startZeroPointCalibration();
		while(!swerveWheelDrive.isOnZeroPoint()) {
			System.out.print(".");
			swerveWheelDrive.tick();
			Timer.delay(0.002);
		}
//		System.out.println();
//		System.out.print("Checking "+label+" position sensor DONE");
	}

	private void checkMotors(String label, MotorController angleMotor, MotorController velocityMotor) {
//		System.out.println("Checking "+label);
//		System.out.println("Checking angle motor "+label);
		checkMotor(angleMotor);
//		System.out.println("Checking velocity motor "+label);
		checkMotor(velocityMotor);
	}

	private void checkMotor(MotorController motor) {		
		motor.set(0.3);
		Timer.delay(10);
		motor.set(0.0);
		Timer.delay(2);
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

}
