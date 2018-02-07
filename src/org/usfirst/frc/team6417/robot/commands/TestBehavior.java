package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.subsystems.LiftingUnit;

import edu.wpi.first.wpilibj.command.CommandGroup;

public final class TestBehavior extends CommandGroup {
	 public TestBehavior() {
	    	addSequential(new GetFieldInformation());
	    	addSequential(new LiftingUnitMove(LiftingUnit.TO_SWITCH));
	 }
}
