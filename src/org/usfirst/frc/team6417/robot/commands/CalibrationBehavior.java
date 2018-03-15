package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.RobotMap;

import edu.wpi.first.wpilibj.command.CommandGroup;

public final class CalibrationBehavior extends CommandGroup {
	public CalibrationBehavior() {
		if(RobotMap.SUBSYSTEM.IS_LIFTING_UNIT_IN_USE) {
			addSequential(new LiftingUnitFindEndpointDown());
			addSequential(new LiftingUnitMoveToPosition(RobotMap.ROBOT.LIFTING_UNIT_SWITCH_ALTITUDE_IN_TICKS));
		}
		if(RobotMap.SUBSYSTEM.IS_LIFTING_UNIT_WAGON_IN_USE) {
			addSequential(new LiftingUnitWagonFindEndpointFront());
		}
		if(RobotMap.SUBSYSTEM.IS_SWERVE_DRIVE_IN_USE) {
			addSequential(new SwerveDriveWheelsToZeroPosition());
			addSequential(new SwerveDriveWheelsToParallelPosition());
			addSequential(new SwerveDriveSetPosToZero());
		}
	}
}
