package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;
import org.usfirst.frc.team6417.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Command;

public final class LiftingUnitWagonMoveToFullBack extends Command {
	
	DigitalInput swi = new DigitalInput(4);
	
	float curTime = 0;
	float approxMillis = 3000;

	public LiftingUnitWagonMoveToFullBack() {
		requires(Robot.liftingUnitWagon);
	}

	@Override
	protected void initialize() {
		if(RobotMap.SUBSYSTEM.GET_TO_CLIMB_TIME) {
			curTime = System.currentTimeMillis();
		}
		else if(RobotMap.SUBSYSTEM.GET_TO_CLIMB_POSITION) {
			Robot.liftingUnitWagon.moveToPos(RobotMap.ROBOT.LIFTING_UNIT_WAGON_BACK_POSITION_IN_TICKS);
		}
		else {
			System.out.println("Both versions of climb deactiated error");
		}
	}
	
	@Override
	protected void execute() {
		if(RobotMap.SUBSYSTEM.GET_TO_CLIMB_TIME) {
			if(System.currentTimeMillis() > (curTime + approxMillis)) {
				Robot.liftingUnitWagon.setSpeed(RobotMap.VELOCITY.LIFTING_UNIT_WAGON_MOTOR_BACKWARD_VELOCITY);
			}
			else {
				if(swi.get()) {
					Robot.liftingUnitWagon.setSpeed(0);
				}
				else {
					Robot.liftingUnitWagon.setSpeed(RobotMap.VELOCITY.LIFTING_UNIT_WAGON_MOTOR_VERY_SLOW_FORWARD_VELOCITY*-1);
				}
			}
		}
		else if(RobotMap.SUBSYSTEM.GET_TO_CLIMB_POSITION) {
			Robot.liftingUnitWagon.tick();
		}
	}
	
	@Override
	protected boolean isFinished() {
		if(RobotMap.SUBSYSTEM.GET_TO_CLIMB_TIME) {
			return true;
		}
		else if(RobotMap.SUBSYSTEM.GET_TO_CLIMB_POSITION) {
			return Robot.liftingUnitWagon.isFinished();
		}
		else {
			return true;
		}
	}

}
