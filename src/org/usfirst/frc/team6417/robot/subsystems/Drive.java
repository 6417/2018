package org.usfirst.frc.team6417.robot.subsystems;

import org.usfirst.frc.team6417.robot.Fridolin;
import org.usfirst.frc.team6417.robot.Robot;
import org.usfirst.frc.team6417.robot.RobotMap;
import org.usfirst.frc.team6417.robot.commands.DriveTeleoperated;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * This class represents the drive (Fahrwerk) of the robot
 */
public final class Drive extends Subsystem {
	private final DifferentialDrive drive;
	
	public Drive() {
		drive = new DifferentialDrive(
					new Fridolin(RobotMap.MOTOR.DRIVE_LEFT_PORT), 
					new Fridolin(RobotMap.MOTOR.DRIVE_RIGHT_PORT));
	}	
	
	public void setVelocity(double leftVel, double rightVel) {
		drive.tankDrive(p() * leftVel, p() * rightVel);
	}
	
	public void stop() {
		drive.stopMotor();
	}
	
	private double p() {
		return Robot.powerManager.calculatePowerFor(this);
	}
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new DriveTeleoperated());
	}

}
