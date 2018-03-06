package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.OI;
import org.usfirst.frc.team6417.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Implementation of the swerve-drive of JM. Further readings here:
 * https://jacobmisirian.gitbooks.io/frc-swerve-drive-programming/content/chapter1.html
 */
public final class SwerveDriveWheelTeleoperated extends Command {
	
	public SwerveDriveWheelTeleoperated() {
		requires(Robot.swerveDriveWheel);
	}

	@Override
	protected void execute() {
//		Robot.swerveDrive.drive(
//				OI.getInstance().joystickOne.getY(),
//				OI.getInstance().joystickOne.getX(),
//				OI.getInstance().joystickOne.getZ()
////				OI.getInstance().joystickOne.getX(), 
////				OI.getInstance().joystickOne.getZ()
//				);
//		Robot.swerveDrive.checkAnglesOnTarget();
		SmartDashboard.putNumber(Robot.swerveDriveWheel.angleMotor.getName()+" pos:", Robot.swerveDriveWheel.angleMotor.getSelectedSensorPosition(0));
		Robot.swerveDriveWheel.angleMotor.set(0.6 * -OI.getInstance().joystickOne.getY());
//		Robot.swerveDrive.frontLeft.drive (OI.getInstance().joystickOne.getY(), 
//								 	  -OI.getInstance().joystickOne.getX());		
//		Robot.swerveDrive.frontRight.drive (OI.getInstance().joystickOne.getY(), 
//			 	  -OI.getInstance().joystickOne.getX());		
//		Robot.swerveDrive.backLeft.drive (OI.getInstance().joystickOne.getY(), 
//			 	  -OI.getInstance().joystickOne.getX());		
//		Robot.swerveDrive.backRight.drive (OI.getInstance().joystickOne.getY(), 
//			 	  -OI.getInstance().joystickOne.getX());		
	}
	
	
	
	@Override
	protected boolean isFinished() {

		// Always return false to keep this command running until interruption from the scheduler.
		return false;
	}

}
