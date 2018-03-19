package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;
import org.usfirst.frc.team6417.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

public final class LiftingUnitWagonMoveToFullBack extends Command {

	public LiftingUnitWagonMoveToFullBack() {
		requires(Robot.liftingUnitWagon);
	}

	@Override
	protected void initialize() {
		Robot.liftingUnitWagon.moveToPos(RobotMap.ROBOT.LIFTING_UNIT_WAGON_BACK_POSITION_IN_TICKS);
	}
	
	@Override
	protected void execute() {
		Robot.liftingUnitWagon.tick();
	}
	
	@Override
	protected boolean isFinished() {
		return Robot.liftingUnitWagon.isFinished();
	}

}
