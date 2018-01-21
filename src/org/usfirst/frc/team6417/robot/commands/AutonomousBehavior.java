package org.usfirst.frc.team6417.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public final class AutonomousBehavior extends CommandGroup {
	
	 public AutonomousBehavior() {
//	    	addSequential(new GetFieldInformation());
//	    	addParallel(new MovePoleToFrontPosition());
//	    	addSequential(new GoFromStationToSwitch());
	    	addParallel(new PoleToSwitchAltitude());
	    	addSequential(new GripperPush());
	 }
}
