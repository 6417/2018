package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public final class LiftingUnitHoldPosition extends Command {
	
	public LiftingUnitHoldPosition() {
		requires(Robot.liftingUnit);
	}
	
	@Override
	protected void initialize() {
		Robot.liftingUnit.holdPosition();
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}

}
