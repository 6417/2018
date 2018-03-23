package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.OI;
import org.usfirst.frc.team6417.robot.Robot;
import org.usfirst.frc.team6417.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Simple implementation of a parallel wheel swerve-drive
 */
public final class SwerveDriveMecanumSimilarTeleoperated extends Command {
	
	public SwerveDriveMecanumSimilarTeleoperated() {
		requires(Robot.swerveDrive);
	}
	
	@Override
	protected void initialize() {
		Robot.swerveDrive.resetAngleEncoders();
	}

	@Override
	protected void execute() {
		double x = OI.getInstance().joystickOne.getX();
		double y = OI.getInstance().joystickOne.getY();
		double twist = OI.getInstance().joystickOne.getZ();
		
		if(Math.abs(x) <= RobotMap.JOYSTICK.DEADZONES.JOYSTICK1_X) {
			x = 0.0;
		}
		if(Math.abs(y) <= RobotMap.JOYSTICK.DEADZONES.JOYSTICK1_Y) {
			y = 0.0;
		}
//			Robot.swerveDrive.driveMecanumSimilar(-y, directionInRadians);
//		Robot.swerveDrive.driveChristian(x, y, twist);
		Robot.swerveDrive.driveJulian(x, -y);
	}
	
	@Override
	protected boolean isFinished() {
		// Always return false to keep this command running until interruption from the scheduler.
		return false;
	}

}
