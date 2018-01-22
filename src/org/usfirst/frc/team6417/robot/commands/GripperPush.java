package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;
import org.usfirst.frc.team6417.robot.subsystems.Gripper;

import edu.wpi.first.wpilibj.command.Command;

public final class GripperPush extends Command {
	
	public GripperPush() {
		requires(Robot.gripper);
	}
	
	@Override
	protected void initialize() {
		Robot.gripper.onEvent(Gripper.PUSH);
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
