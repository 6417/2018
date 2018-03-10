package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.OI;
import org.usfirst.frc.team6417.robot.Robot;
import org.usfirst.frc.team6417.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

public final class LiftingUnitTeleoperated extends Command {
	
	public LiftingUnitTeleoperated() {
		requires(Robot.liftingUnit);
	}

	@Override
	protected void execute() {
		double y = OI.getInstance().liftingUnitController.getY();
		if(Math.abs(y) <= RobotMap.JOYSTICK.DEADZONES.JOYSTICK1_Y) {
			y = 0.0;
		}
		
		if(y == 0.0) {
			Robot.liftingUnit.holdPosition();
		} else {
			Robot.liftingUnit.move(y);
		}
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}

}
