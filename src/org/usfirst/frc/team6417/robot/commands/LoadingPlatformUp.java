package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;
import org.usfirst.frc.team6417.robot.subsystems.LoadingPlatform;

import edu.wpi.first.wpilibj.command.Command;

public final class LoadingPlatformUp extends Command {
	
	public LoadingPlatformUp() {
		requires(Robot.loadingPlatform);
	}

	@Override
	protected void initialize() {
		Robot.loadingPlatform.onEvent(LoadingPlatform.UP);
	}
	
	@Override
	protected void execute() {
		Robot.loadingPlatform.tick();
	}
	
	@Override
	protected boolean isFinished() {
		return Robot.loadingPlatform.isFinished();
	}

}
