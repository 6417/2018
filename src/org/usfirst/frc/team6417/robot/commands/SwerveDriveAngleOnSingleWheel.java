package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.MotorController;
import org.usfirst.frc.team6417.robot.OI;
import org.usfirst.frc.team6417.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class SwerveDriveAngleOnSingleWheel  extends Command {
	
	public SwerveDriveAngleOnSingleWheel() {
		requires(Robot.swerveDrive);
	}
	
	@Override
	protected void execute() {
		SmartDashboard.putBoolean("SwerveDriveAngleOnSingleWheel", true);
		
		SmartDashboard.putNumber(Robot.swerveDrive.frontLeft.angleMotor.getName()+" ticks", Robot.swerveDrive.frontLeft.angleMotor.getSelectedSensorPosition(0));
		SmartDashboard.putNumber(Robot.swerveDrive.frontRight.angleMotor.getName()+" ticks", Robot.swerveDrive.frontRight.angleMotor.getSelectedSensorPosition(0));
		SmartDashboard.putNumber(Robot.swerveDrive.backLeft.angleMotor.getName()+" ticks", Robot.swerveDrive.backLeft.angleMotor.getSelectedSensorPosition(0));
		SmartDashboard.putNumber(Robot.swerveDrive.backRight.angleMotor.getName()+" ticks", Robot.swerveDrive.backRight.angleMotor.getSelectedSensorPosition(0));
		
		
		if(OI.getInstance().joystickOne.getRawButton(9)) {
			Robot.swerveDrive.frontLeft.angleMotor.set(-OI.getInstance().joystickOne.getY());
			return;
		}else {
			Robot.swerveDrive.frontLeft.angleMotor.set(0);
			Robot.swerveDrive.frontLeft.angleMotor.setSelectedSensorPosition(0, MotorController.kPIDLoopIdx, MotorController.kTimeoutMs);
		}
		
		if(OI.getInstance().joystickOne.getRawButton(10)) {
			Robot.swerveDrive.frontRight.angleMotor.set(-OI.getInstance().joystickOne.getY());
			return;
		}else {
			Robot.swerveDrive.frontRight.angleMotor.set(0);
			Robot.swerveDrive.frontRight.angleMotor.setSelectedSensorPosition(0, MotorController.kPIDLoopIdx, MotorController.kTimeoutMs);
		}
		if(OI.getInstance().joystickOne.getRawButton(11)) {
			Robot.swerveDrive.backLeft.angleMotor.set(-OI.getInstance().joystickOne.getY());
			return;
		}else {
			Robot.swerveDrive.backLeft.angleMotor.set(0);
			Robot.swerveDrive.backLeft.angleMotor.setSelectedSensorPosition(0, MotorController.kPIDLoopIdx, MotorController.kTimeoutMs);
		}
		if(OI.getInstance().joystickOne.getRawButton(12)) {
			Robot.swerveDrive.backRight.angleMotor.set(-OI.getInstance().joystickOne.getY());
			return;
		}else {
			Robot.swerveDrive.backRight.angleMotor.set(0);
			Robot.swerveDrive.backRight.angleMotor.setSelectedSensorPosition(0, MotorController.kPIDLoopIdx, MotorController.kTimeoutMs);
		}
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}

}
