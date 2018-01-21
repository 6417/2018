package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public final class GripperPull extends Command {
	
	public GripperPull() {
		requires(Robot.gripper);
	}
	
	@Override
	protected void initialize() {
		Robot.gripper.pull();
	}
	
	@Override
	protected boolean isFinished() {
		return true;
	}

}
