package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;
import org.usfirst.frc.team6417.robot.model.Event;

import edu.wpi.first.wpilibj.command.Command;

public final class LiftingUnitMove extends Command {
	private final Event event;
	
	public LiftingUnitMove(final Event event) {
		this.event = event;
		requires(Robot.liftingUnit);
	}

	@Override
	protected void initialize() {
		Robot.liftingUnit.onEvent(event);
	}
	
	@Override
	protected boolean isFinished() {
//		return Robot.liftingUnit.onTarget();
		return true;
	}

}
