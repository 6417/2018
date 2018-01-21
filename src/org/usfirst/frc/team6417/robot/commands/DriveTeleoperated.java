package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.OI;
import org.usfirst.frc.team6417.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public final class DriveTeleoperated extends Command {

	public DriveTeleoperated() {
		requires(Robot.drive);
	}
	
	@Override
	protected void execute() {
		Robot.drive.setVelocity(OI.getInstance().joystickOne.getX(), 
								OI.getInstance().joystickOne.getY());
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}

}
