package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;
import org.usfirst.frc.team6417.robot.model.Event;
import org.usfirst.frc.team6417.robot.subsystems.Gripper;

import edu.wpi.first.wpilibj.command.Command;

public final class GripperMoveTimed extends Command {
	private int millies;
	private Event event;
	private long startTimestamp;
	private boolean isFinished = false;
	
	public GripperMoveTimed(int millies, Event event) {
		this.millies = millies;
		this.event = event;
		requires(Robot.gripper);		
	}
	
	@Override
	protected void initialize() {
		Robot.gripper.onEvent(event);
		startTimestamp = System.currentTimeMillis();
	}
	
	@Override	
	protected void execute() {
		if(millies > System.currentTimeMillis() - startTimestamp) {
			isFinished = true;
			Robot.gripper.onEvent(Gripper.STOP);
		}else {
			Robot.gripper.tick();
		}
	}

	@Override
	protected boolean isFinished() {
		return isFinished;//Robot.gripper.isFinished();
	}

}
