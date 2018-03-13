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
		Robot.liftingUnit.startMoveToEndpointDown();
		SmartDashboard.putBoolean("LU calibration", true);
	}
	
	@Override
	protected void execute() {
		Robot.liftingUnit.tickMoveToEndpointDown();
	}
	
	@Override
	protected boolean isFinished() {
		if( Robot.liftingUnit.isInEndpointBottom()) {
			Robot.liftingUnit.stopMoveToEndpointDown();
			return true;
		}
		return false;
	}
	
	@Override
	protected void end() {
		SmartDashboard.putBoolean("LU calibration", false);
	}

}
