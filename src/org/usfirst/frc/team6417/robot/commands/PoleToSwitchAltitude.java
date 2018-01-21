package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public final class PoleToSwitchAltitude extends Command {

	public PoleToSwitchAltitude() {
		requires(Robot.pole);
	}
	
	@Override
	protected void initialize() {
		Robot.pole.moveDown();
	}
	
	@Override
	protected boolean isFinished() {
		return Robot.pole.isOnGroundAltitude();
	}
	
	@Override
	protected void end() {
		Robot.pole.stop();
	}

}
