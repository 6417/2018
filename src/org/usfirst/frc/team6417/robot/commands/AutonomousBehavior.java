package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.subsystems.LiftingUnit;

import edu.wpi.first.wpilibj.command.CommandGroup;

public final class AutonomousBehavior extends CommandGroup {
	
	 public AutonomousBehavior() {
	    	addSequential(new GetFieldInformation());
//	    	addParallel(new MovePoleToFrontPosition());
//	    	addSequential(new GoFromStationToSwitch());
	    	addSequential(new LiftingUnitMove(LiftingUnit.TO_SWITCH));
	    	addSequential(new GripperPush());
	    	addSequential(new GripperStop());
	 }
}
