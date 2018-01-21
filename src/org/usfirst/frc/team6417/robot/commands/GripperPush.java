package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public final class GripperPush extends Command {
	
	public GripperPush() {
		requires(Robot.gripper);
	}
	
	@Override
	protected void initialize() {
		Robot.gripper.push();
	}
	
	// protected void execute() {}

	@Override
	protected boolean isFinished() {
		return true;
	}

}
