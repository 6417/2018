package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;
import org.usfirst.frc.team6417.robot.model.Event;
import org.usfirst.frc.team6417.robot.repository.GameStrategyRepository;
import org.usfirst.frc.team6417.robot.subsystems.Gripper;

import edu.wpi.first.wpilibj.command.Command;

public final class GripperMoveTimed extends Command {
	private int millies;
	private Event event;
	private Long startTimestamp = null;
	private boolean isFinished = false;
	
	public GripperMoveTimed(int millies, Event event) {
		this.millies = millies;
		this.event = event;
		requires(Robot.gripper);		
	}
	
	@Override
	protected void initialize() {
		Robot.gripper.onEvent(event);
	}
	
	@Override	
	protected void execute() {
		if(startTimestamp == null) {
			startTimestamp = System.currentTimeMillis();
		}
		if(GameStrategyRepository.getInstance().isNeedPushWithGripperAtSwitch()) {
			if(millies < System.currentTimeMillis() - startTimestamp) {
				isFinished = true;
				Robot.gripper.onEvent(Gripper.STOP);
			}else {
				Robot.gripper.tick();
			}
		}else {
			isFinished = true;
		}
	}

	@Override
	protected boolean isFinished() {
		return isFinished;
	}

}
