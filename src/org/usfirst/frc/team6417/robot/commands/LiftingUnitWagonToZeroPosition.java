package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public final class LiftingUnitWagonToZeroPosition extends Command {

	
	public LiftingUnitWagonToZeroPosition() {
		requires(Robot.swerveDrive);
	}
	
	@Override
	protected void initialize() {
		Robot.swerveDrive.startZeroPointCalibration();
	}
	
	@Override
	protected boolean isFinished() {
		return Robot.swerveDrive.isZeroPointCalibrationFinished();
	}

}
