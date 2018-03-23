package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public final class LiftingUnitWagonGameStart extends Command {
	
	public LiftingUnitWagonGameStart() {
		requires(Robot.liftingUnitWagon);
	}
	
	@Override
	protected void execute() {
		Robot.liftingUnitWagon.gameStart();
	}
	
	@Override
	protected boolean isFinished() {
		return true;
	}

}
