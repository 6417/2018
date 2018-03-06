package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public final class SwerveDriveWheelStop extends Command {

	
	public SwerveDriveWheelStop() {
		requires(Robot.swerveDriveWheel);
	}
	
	@Override
	protected void initialize() {
		Robot.swerveDriveWheel.angleMotor.set(0);
	}
	
	@Override
	protected boolean isFinished() {
		return true;
	}

}
