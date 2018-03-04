package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.OI;
import org.usfirst.frc.team6417.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Implementation of the swerve-drive of JM. Further readings here:
 * https://jacobmisirian.gitbooks.io/frc-swerve-drive-programming/content/chapter1.html
 */
public final class SwerveDriveWheelTeleoperated extends Command {
	
	public SwerveDriveWheelTeleoperated() {
		requires(Robot.swerveDrive);
	}

	@Override
	protected void execute() {
		Robot.swerveDrive.drive(
				OI.getInstance().joystickOne.getY(),
				OI.getInstance().joystickOne.getX(),
				OI.getInstance().joystickOne.getZ()
//				OI.getInstance().joystickOne.getX(), 
//				OI.getInstance().joystickOne.getZ()
				);
		Robot.swerveDrive.checkAnglesOnTarget();
		
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
