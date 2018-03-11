package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public final class LiftingUnitReset extends Command {
	public LiftingUnitReset() {
		requires(Robot.liftingUnit);
	}

	@Override
	protected void initialize() {
		Robot.liftingUnit.resetEncoder();
	}
	
	@Override
	protected boolean isFinished() {
		return true;
	}

}
