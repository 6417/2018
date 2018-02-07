package org.usfirst.frc.team6417.robot.subsystems;

import org.usfirst.frc.team6417.robot.Fridolin;
import org.usfirst.frc.team6417.robot.RobotMap;
import org.usfirst.frc.team6417.robot.commands.DriveTeleoperated;
import org.usfirst.frc.team6417.robot.service.powermanagement.PowerManagementStrategy;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class represents the Plan-B drive (Fahrwerk) of the robot. It represents a differential-drive.
 */
public final class Drive extends Subsystem {
	private final DifferentialDrive drive;
	private final PowerManagementStrategy powerManagementStrategy;
	
	public Drive(PowerManagementStrategy powerManagementStrategy) {
		super("Drive");
		this.powerManagementStrategy = powerManagementStrategy;
		drive = new DifferentialDrive(
					new Fridolin("Left-Motor", RobotMap.MOTOR.DRIVE_FRONT_LEFT_VELOCITY_PORT), 
					new Fridolin("Right-Motor", RobotMap.MOTOR.DRIVE_FRONT_RIGHT_VELOCITY_PORT));
	}	
	
	public void setVelocity(double angle, double throttle) {
		double p = powerManagementStrategy.calculatePower();
		throttle = p * throttle;
		drive.arcadeDrive(throttle, -angle);
		
		SmartDashboard.putNumber("Drive velocity", throttle);
		SmartDashboard.putNumber("Drive angle", angle);
	}
	
	public void stop() {
		drive.stopMotor();
	}
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new DriveTeleoperated());
	}

}
