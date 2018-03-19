package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.RobotMap;

import edu.wpi.first.wpilibj.command.CommandGroup;

public final class PrepareRobotElevationBehavior extends CommandGroup {
	public PrepareRobotElevationBehavior() {
		addSequential(new LiftingUnitMoveToSavePosition());
		addSequential(new LiftingUnitWagonMoveToFullBack());
		addParallel(new LiftingUnitMoveToPosition(RobotMap.ROBOT.LIFTING_UNIT_HANGING_ALTITUDE_IN_TICKS));
	}
}
