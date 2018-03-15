package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.RobotMap;

import edu.wpi.first.wpilibj.command.CommandGroup;

public final class CalibrationBehavior extends CommandGroup {
	public CalibrationBehavior() {
		addSequential(new LiftingUnitFindEndpointDown());
		addSequential(new LiftingUnitMoveToPosition(RobotMap.ROBOT.LIFTING_UNIT_SWITCH_ALTITUDE_IN_TICKS));
		addSequential(new LiftingUnitWagonFindEndpointFront());
		addSequential(new SwerveDriveWheelsToZeroPosition());
		addSequential(new SwerveDriveWheelsToParallelPosition());
		addSequential(new SwerveDriveSetPosToZero());
	}
}
