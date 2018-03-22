package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class LiftingUnitWagonFindEndpointFront extends Command {
	
	public LiftingUnitWagonFindEndpointFront() {
		requires(Robot.liftingUnitWagon);
	}
	
	@Override
	protected void initialize() {
		SmartDashboard.putBoolean("LUW calibration", true);
		Robot.liftingUnitWagon.startMoveToEndpointFront();
	}
	
	@Override
	protected boolean isFinished() {
		if( Robot.liftingUnitWagon.isInEndpositionFront() ) {
			Robot.liftingUnitWagon.stopMoveToEndpointFront();
			return true;
		}
		return false;
	}
	
	@Override
	protected void end() {
		SmartDashboard.putBoolean("LUW calibration", false);
	}

}
