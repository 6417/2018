package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.OI;
import org.usfirst.frc.team6417.robot.Robot;
import org.usfirst.frc.team6417.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

public final class SwerveDriveStraight extends Command {

	
	public SwerveDriveStraight() {
		requires(Robot.swerveDrive);
	}
	
	@Override
	protected void initialize() {
		Robot.swerveDrive.drive(0,0.3,0);
	}
	
	@Override
	protected void execute() {
		double v = -OI.getInstance().joystickOne.getY();
		double angle = OI.getInstance().joystickOne.getX() * RobotMap.MATH.PI;
		
		Robot.swerveDrive.frontLeft.gotoAngle(angle);
		Robot.swerveDrive.frontRight.gotoAngle(angle);
		Robot.swerveDrive.backLeft.gotoAngle(angle);
		Robot.swerveDrive.backRight.gotoAngle(angle);

	
//		Robot.swerveDrive.frontLeft.angleMotor.set(v);
//		Robot.swerveDrive.frontRight.angleMotor.set(v);
//		Robot.swerveDrive.backLeft.angleMotor.set(v);
//		Robot.swerveDrive.backRight.angleMotor.set(v);
}
	
	@Override
	protected boolean isFinished() {
		return false;
	}

}
