package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;
import org.usfirst.frc.team6417.robot.subsystems.LoadingPlatform;

import edu.wpi.first.wpilibj.command.Command;

public final class LoadingPlatformDown extends Command {
	
	public LoadingPlatformDown() {
		requires(Robot.loadingPlatform);
	}

	@Override
	protected void initialize() {
		Robot.loadingPlatform.onEvent(LoadingPlatform.DOWN);
	}
	
	@Override
	protected void execute() {
		Robot.loadingPlatform.tick();
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}

}
