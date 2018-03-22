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
		/* sign this so forward is positive */
		double forward = -1.0 * OI.getInstance().joystickOne.getY();
		/* sign this so right is positive. */
		double turn = +1.0 * OI.getInstance().joystickOne.getX();
		/* deadband */
		if (Math.abs(forward) < 0.10) {
			/* within 10% joystick, make it zero */
			forward = 0;
		}
		if (Math.abs(turn) < 0.10) {
			/* within 10% joystick, make it zero */
			turn = 0;
		}
		/* print the joystick values to sign them, comment
		 * out this line after checking the joystick directions. */
//		System.out.println("JoyY:" + forward + "  turn:" + turn );
		/* drive the robot, when driving forward one side will be red.  
		 * This is because DifferentialDrive assumes 
		 * one side must be negative */	
		Robot.drive.arcadeDrive(forward, turn);
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}

}
