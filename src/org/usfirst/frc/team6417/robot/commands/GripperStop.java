package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public final class GripperStop extends Command {
	
	public GripperStop() {
		requires(Robot.gripper);
	}
	
	@Override
	protected void initialize() {
		Robot.gripper.stop();
	}
	
	// protected void execute() {}

	@Override
	protected boolean isFinished() {
		return true;
	}

}
