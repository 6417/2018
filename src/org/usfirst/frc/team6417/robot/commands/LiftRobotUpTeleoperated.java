package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.OI;
import org.usfirst.frc.team6417.robot.Robot;
import org.usfirst.frc.team6417.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

public final class LiftRobotUpTeleoperated extends Command {
	int interval = 100;
	int counter = 0;
	public LiftRobotUpTeleoperated() {
		requires(Robot.liftingUnit);
	}
	
	@Override
	protected void execute() {
		System.out.println("LiftRobotUpTe-----leoperated.execute()");
		double y = OI.getInstance().liftingUnitController.getY();
		if(Math.abs(y) <= RobotMap.JOYSTICK.DEADZONES.JOYSTICK1_Y) {
			y = 0.0;
		}

		y *= -1;
		
		if(y > 0.0) {
			Robot.liftingUnit.moveNoHoldPosition(y);
		}else {
			if(counter % interval == 0) {
				System.out.println("LU not allowed go go up only down");
				counter = 0;
			}
			counter++;
		}
		
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		System.out.println("LiftRobotUpTeleoperated.end()");
	}
}
