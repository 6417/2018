package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Implementation of the swerve-drive of JM. Further readings here:
 * https://jacobmisirian.gitbooks.io/frc-swerve-drive-programming/content/chapter1.html
 */
public final class SwerveDriveTeleoperated extends Command {
	
	public SwerveDriveTeleoperated() {
		requires(Robot.swerveDrive);
	}

	@Override
	protected void execute() {
//		Robot.swerveDrive.drive (OI.getInstance().joystickOne.getY(), 
//								 OI.getInstance().joystickOne.getX(), 
//								 OI.getInstance().joystickOne.getZ());
//		Robot.swerveDrive.drive (
//				OI.getInstance().joystickOne.getRawAxis (1),
//				OI.getInstance().joystickOne.getRawAxis (0), 
//				OI.getInstance().joystickOne.getRawAxis (4));
	}
	
	@Override
	protected boolean isFinished() {
		// Always return false to keep this command running until interruption from the scheduler.
		return false;
	}

}
