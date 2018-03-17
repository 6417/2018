package org.usfirst.frc.team6417.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public final class LiftingUnitWagonMoveBehavior extends CommandGroup {
	public LiftingUnitWagonMoveBehavior() {
		addSequential(new LiftingUnitMoveToSavePosition());
	}
}
