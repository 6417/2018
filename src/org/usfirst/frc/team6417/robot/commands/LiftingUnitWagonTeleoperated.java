package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.OI;
import org.usfirst.frc.team6417.robot.Robot;
import org.usfirst.frc.team6417.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class LiftingUnitWagonTeleoperated extends Command {
	
	public LiftingUnitWagonTeleoperated() {
		requires(Robot.liftingUnitWagon);
	}

	@Override
	protected void execute() {
		double y = OI.getInstance().liftingUnitController.getRawAxis(5);
		if(Math.abs(y) <= RobotMap.JOYSTICK.DEADZONES.JOYSTICK2_X) {
			y = 0.0;
		}
		
		if(y == 0.0) {
			Robot.liftingUnitWagon.holdPosition();
		} else {
			Robot.liftingUnitWagon.move(-y);
		}
		
		SmartDashboard.putNumber("LUW-POS", Robot.liftingUnitWagon.motor.getSelectedSensorPosition(0));
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}

}
