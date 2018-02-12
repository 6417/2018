package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;
import org.usfirst.frc.team6417.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Implementation of the swerve-drive of JM. Further readings here:
 * https://jacobmisirian.gitbooks.io/frc-swerve-drive-programming/content/chapter1.html
 */
public final class SwerveDriveWheelToAngle extends Command {
	
	public SwerveDriveWheelToAngle() {
		requires(Robot.swerveDriveWheel);
	}

	@Override
	protected void initialize() {
		Robot.swerveDriveWheel.gotoAngle(RobotMap.MATH.PI);
	}
	
	@Override
	protected void execute() {
		Robot.swerveDriveWheel.tick();		
	}
	
	
	
	@Override
	protected boolean isFinished() {
		// Always return false to keep this command running until interruption from the scheduler.
		return Robot.swerveDriveWheel.onTarget();
	}

}
