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
		Robot.swerveDrive.resetEncoders();
	}

	@Override
	protected void execute() {
			double y = OI.getInstance().joystickOne.getMagnitude();;//OI.getInstance().joystickOne.getY();
			double directionInRadians = 0;//OI.getInstance().joystickOne
//			double value 
			
			if(Math.abs(y) <= RobotMap.JOYSTICK.DEADZONES.JOYSTICK1_Y) {
				y = 0.0;
			}
			if(Math.abs(directionInRadians) <= RobotMap.JOYSTICK.DEADZONES.JOYSTICK1_TWIST) {
				directionInRadians = 0.0;
			}
			Robot.swerveDrive.driveMecanumSimilar(-y, directionInRadians);
	}
	
	@Override
	protected boolean isFinished() {
		// Always return false to keep this command running until interruption from the scheduler.
		return false;
	}

}
