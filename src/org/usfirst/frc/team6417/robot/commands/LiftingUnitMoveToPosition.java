package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public final class LiftingUnitMoveToPosition extends Command {

	private final int absolutePosition;
	
	public LiftingUnitMoveToPosition(int absolutePosition) {
		this.absolutePosition = absolutePosition;
		requires(Robot.liftingUnit);
	}
	
	@Override
	protected void initialize() {
		Robot.liftingUnit.moveToAbsolutePos(absolutePosition);
	}
	
	@Override
	protected boolean isFinished() {
		return Robot.liftingUnit.isOnTarget();
	}

}
