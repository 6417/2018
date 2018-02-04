package org.usfirst.frc.team6417.robot.subsystems;

import org.usfirst.frc.team6417.robot.Fridolin;
import org.usfirst.frc.team6417.robot.Robot;
import org.usfirst.frc.team6417.robot.RobotMap;
import org.usfirst.frc.team6417.robot.commands.DriveTeleoperated;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * This class represents the Plan-B drive (Fahrwerk) of the robot. It represents a differential-drive.
 */
public final class Drive extends Subsystem {
	private final DifferentialDrive drive;
	
	public Drive() {
		drive = new DifferentialDrive(
					new Fridolin(RobotMap.MOTOR.DIFFERENTIAL_DRIVE_FRONT_LEFT_PORT), 
					new Fridolin(RobotMap.MOTOR.DIFFERENTIAL_DRIVE_FRONT_RIGHT_PORT));
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
