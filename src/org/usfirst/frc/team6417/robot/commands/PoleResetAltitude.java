package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public final class PoleResetAltitude extends Command {

	public PoleResetAltitude() {
		requires(Robot.pole);
	}
	
	@Override
	protected void initialize() {
		Robot.pole.resetEncoders();
	}
	
	@Override
	protected boolean isFinished() {
		return true;
	}

}
