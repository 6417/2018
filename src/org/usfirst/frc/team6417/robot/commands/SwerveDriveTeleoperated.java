package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.OI;
import org.usfirst.frc.team6417.robot.Robot;
import org.usfirst.frc.team6417.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Implementation of the swerve-drive of JM. Further readings here:
 * https://jacobmisirian.gitbooks.io/frc-swerve-drive-programming/content/chapter1.html
 */
public final class SwerveDriveTeleoperated extends Command {
	
	public SwerveDriveTeleoperated() {
		requires(Robot.swerveDrive);
	}
	
	@Override
	protected void initialize() {
		Robot.swerveDrive.resetEncoders();
	}

	@Override
	protected void execute() {
			double y = OI.getInstance().joystickOne.getY();
			double x = OI.getInstance().joystickOne.getX();
			double z = OI.getInstance().joystickOne.getZ();
			
			if(Math.abs(x) <= RobotMap.JOYSTICK.DEADZONES.JOYSTICK1_X) {
				x = 0.0;
			}
			if(Math.abs(y) <= RobotMap.JOYSTICK.DEADZONES.JOYSTICK1_Y) {
				y = 0.0;
			}
			if(Math.abs(z) <= RobotMap.JOYSTICK.DEADZONES.JOYSTICK1_TWIST) {
				z = 0.0;
			}
//			z = 0.0; // TODO Remove when ready for rotation.
			
			Robot.swerveDrive.drive(-y, x, z);

//			Robot.swerveDrive.drive (
//					OI.getInstance().joystickOne.getRawAxis (1),
//					OI.getInstance().joystickOne.getRawAxis (0), 
//					OI.getInstance().joystickOne.getRawAxis (4));
	}
	
	@Override
	protected boolean isFinished() {
//		Robot.swerveDrive.checkAnglesOnTarget();
		// Always return false to keep this command running until interruption from the scheduler.
		return false;
	}

}
