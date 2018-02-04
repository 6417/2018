package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;
import org.usfirst.frc.team6417.robot.model.Event;

import edu.wpi.first.wpilibj.command.Command;

public final class LiftingUnitWagonMove extends Command {
	
	private final Event event;
	
	public LiftingUnitWagonMove(final Event event) {
		this.event = event;
		requires(Robot.liftingUnitWagon);
	}

	@Override
	protected void initialize() {
		Robot.liftingUnitWagon.onEvent(event);
	}
	
	@Override
	protected void execute() {
		Robot.liftingUnitWagon.tick();
	}
	
	@Override
	protected boolean isFinished() {
		return Robot.liftingUnitWagon.isFinished();
	}

}
