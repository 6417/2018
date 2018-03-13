package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.OI;
import org.usfirst.frc.team6417.robot.Robot;
import org.usfirst.frc.team6417.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

public final class LiftingUnitWagonTeleoperated extends Command {
	
	public LiftingUnitWagonTeleoperated() {
		requires(Robot.liftingUnitWagon);
	}

	@Override
	protected void execute() {
		double x = OI.getInstance().liftingUnitController.getRawAxis(4);
		if(Math.abs(x) <= RobotMap.JOYSTICK.DEADZONES.JOYSTICK2_X) {
			x = 0.0;
		}
		
		if(x == 0.0) {
			Robot.liftingUnitWagon.holdPosition();
		} else {
			Robot.liftingUnitWagon.move(x);
		}
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}

}
