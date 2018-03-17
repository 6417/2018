package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class LiftingUnitFindEndpointDown extends Command {
	
	public LiftingUnitFindEndpointDown() {
		requires(Robot.liftingUnit);
	}
	
	@Override
	protected void initialize() {
		System.out.println("LiftingUnitFindEndpointDown.initialize()");
		Robot.liftingUnit.startMoveToEndpointDown();
		SmartDashboard.putBoolean("LU calibration", true);
	}
	
	@Override
	protected boolean isFinished() {
		System.out.println("LiftingUnitFindEndpointDown.isFinished()");
		if( Robot.liftingUnit.isInEndpointBottom()) {
			Robot.liftingUnit.stopMoveToEndpointDown();
			return true;
		}
		return false;
	}
	
	@Override
	protected void end() {
		SmartDashboard.putBoolean("LU calibration", false);
		System.out.println("LiftingUnitFindEndpointDown.end()");
	}

}
