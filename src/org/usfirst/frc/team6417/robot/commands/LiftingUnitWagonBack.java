package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;
import org.usfirst.frc.team6417.robot.subsystems.LiftingUnitWagon;

import edu.wpi.first.wpilibj.command.Command;

public final class LiftingUnitWagonBack extends Command {
	
	public LiftingUnitWagonBack() {
		requires(Robot.liftingUnitWagon);
	}

	@Override
	protected void initialize() {
		Robot.liftingUnitWagon.onEvent(LiftingUnitWagon.BACK);
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
