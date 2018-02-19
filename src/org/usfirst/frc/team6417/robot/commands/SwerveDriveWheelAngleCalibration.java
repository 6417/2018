package org.usfirst.frc.team6417.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public final class SwerveDriveWheelAngleCalibration extends CommandGroup {
	
	public SwerveDriveWheelAngleCalibration() {
		addSequential(new SwerveDriveWheelsToZeroPosition());
		addSequential(new SwerveDriveWheelsToParallelPosition());
	}

}
