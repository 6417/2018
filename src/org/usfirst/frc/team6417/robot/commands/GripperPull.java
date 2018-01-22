package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;
import org.usfirst.frc.team6417.robot.subsystems.Gripper.Event;

import edu.wpi.first.wpilibj.command.Command;

public final class GripperPull extends Command {
	
	public GripperPull() {
		requires(Robot.gripper);
	}
	
	@Override
	protected void initialize() {
		Robot.gripper.onEvent(Event.PULL);
	}

	@Override
	protected void execute() {
		Robot.gripper.tick();
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}

}
