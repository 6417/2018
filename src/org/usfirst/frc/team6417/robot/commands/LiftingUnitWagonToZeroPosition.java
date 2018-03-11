package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public final class LiftingUnitWagonToZeroPosition extends Command {

	
	public LiftingUnitWagonToZeroPosition() {
		requires(Robot.liftingUnitWagon);
	}
	
	@Override
	protected void initialize() {
//		Robot.liftingUnitWagon.onEvent(LiftingUni);.startZeroPointCalibration();
	}
	
	@Override
	protected boolean isFinished() {
		return Robot.swerveDrive.isZeroPointCalibrationFinished();
	}

}
