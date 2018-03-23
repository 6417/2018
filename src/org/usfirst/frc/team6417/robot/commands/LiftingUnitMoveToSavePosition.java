package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public final class LiftingUnitMoveToSavePosition extends Command {
	
	public LiftingUnitMoveToSavePosition() {
		requires(Robot.liftingUnit);
	}
	
	@Override
	protected void initialize() {
		Robot.liftingUnit.moveToAboveSafetyAltitude();
	}
	
	@Override
	protected boolean isFinished() {
//		System.out.println("LiftingUnitMoveToSavePosition.isFinished()");
		if( Robot.liftingUnit.isAboveSafetyAltitude() ) {
			Robot.liftingUnit.holdPosition();
			return true;
		}
		return false;
	}

}
