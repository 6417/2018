package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;
import org.usfirst.frc.team6417.robot.subsystems.LiftingUnit;

import edu.wpi.first.wpilibj.command.Command;

public final class LiftingUnitMoveMetric extends Command {
	
	public LiftingUnitMoveMetric() {
		requires(Robot.liftingUnit);
	}

	@Override
	protected void initialize() {
		Robot.liftingUnit.onEvent(LiftingUnit.TO_POSITION);
	}
	
	@Override
	protected boolean isFinished() {
		return Robot.liftingUnit.onTarget();
	}

}
