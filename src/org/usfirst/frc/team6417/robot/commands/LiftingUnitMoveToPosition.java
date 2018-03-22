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
//		System.out.println("LiftingUnitMoveToPosition.initialize()");
		Robot.liftingUnit.setHoldPosition(false);
		Robot.liftingUnit.moveToAbsolutePos(absolutePosition);
	}
	
	@Override
	protected boolean isFinished() {
		System.out.println("LiftingUnitMoveToPosition.isFinished()");
//		return Robot.liftingUnit.isOnTarget();
		return true;
	}

}
