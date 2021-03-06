package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public final class SwerveDriveResetVelocityEncoder extends Command {
	
	public SwerveDriveResetVelocityEncoder() {
		requires(Robot.swerveDrive);
	}
	
	@Override
	protected void execute() {
		System.out.println("SwerveDriveResetVelocityEncoder.execute()");
		Robot.swerveDrive.resetVelocityEncoders();
	}
	
	@Override
	protected boolean isFinished() {
		return true;
	}

}
