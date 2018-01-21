package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public final class PoleToGroundAltitude extends Command {

	public PoleToGroundAltitude() {
		requires(Robot.pole);
	}
	
	@Override
	protected void initialize() {
		Robot.pole.moveUp();
	}
	
	@Override
	protected boolean isFinished() {
		return Robot.pole.isOnSwitchAltitude();
	}
	
	@Override
	protected void end() {
		Robot.pole.stop();
	}

}
