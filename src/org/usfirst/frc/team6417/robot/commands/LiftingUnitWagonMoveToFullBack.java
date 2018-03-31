package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;
import org.usfirst.frc.team6417.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public final class LiftingUnitWagonMoveToFullBack extends Command {
	
	private DigitalInput swi = new DigitalInput(4);
	private Joystick stick;

	public LiftingUnitWagonMoveToFullBack(Joystick joy) {
		stick = joy;
		requires(Robot.liftingUnitWagon);
	}

	@Override
	protected void initialize() {
		if(RobotMap.SUBSYSTEM.GET_TO_CLIMB_POSITION) {
			Robot.liftingUnitWagon.moveToPos(RobotMap.ROBOT.LIFTING_UNIT_WAGON_BACK_POSITION_IN_TICKS);
		}
		else {
			System.out.println("Both versions of climb deactiated error");
		}
	}
	
	@Override
	protected void execute() {
		if(RobotMap.SUBSYSTEM.GET_TO_CLIMB_TIME) {
			if(!swi.get()) {
				Robot.liftingUnitWagon.setSpeed(0);
			}
			else {
				Robot.liftingUnitWagon.setSpeed(stick.getRawAxis(5));
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
