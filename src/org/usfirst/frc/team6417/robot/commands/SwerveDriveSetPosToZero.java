package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public final class SwerveDriveSetPosToZero extends Command {

	
	public SwerveDriveSetPosToZero() {
		requires(Robot.swerveDrive);
	}
	
	@Override
	protected void execute() {
		Robot.swerveDrive.resetEncoders();
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}

}
