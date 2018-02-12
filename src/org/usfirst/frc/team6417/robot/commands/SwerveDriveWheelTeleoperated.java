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
		requires(Robot.swerveDriveWheel);
	}

	@Override
	protected void execute() {
		Robot.swerveDriveWheel.drive (OI.getInstance().joystickOne.getThrottle(), 
								 	  -OI.getInstance().joystickOne.getY());		
	}
	
	
	
	@Override
	protected boolean isFinished() {
		// Always return false to keep this command running until interruption from the scheduler.
		return false;
	}

}
