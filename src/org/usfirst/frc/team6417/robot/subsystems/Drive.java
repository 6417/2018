package org.usfirst.frc.team6417.robot.subsystems;

import org.usfirst.frc.team6417.robot.MotorController;
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
	
	// TODO nils Remove if all 4 motors are connected to the robot
	private boolean isAll4MotorsConnected = false;
	
	public Drive(PowerManagementStrategy powerManagementStrategy) {
		super("Drive");
		this.powerManagementStrategy = powerManagementStrategy;
		
		MotorController leftFrontMotor = new MotorController("Left-Front-Motor", RobotMap.MOTOR.DRIVE_FRONT_LEFT_VELOCITY_PORT); 
		MotorController rightFrontMotor = new MotorController("Right-Front-Motor", RobotMap.MOTOR.DRIVE_FRONT_RIGHT_VELOCITY_PORT);
		
		drive = new DifferentialDrive(leftFrontMotor, rightFrontMotor);
		
		if(isAll4MotorsConnected) {
			MotorController leftRearMotor = new MotorController("Left-Rear-Motor-Slave", RobotMap.MOTOR.DRIVE_BACK_LEFT_VELOCITY_PORT); 
			MotorController rightRearMotor = new MotorController("Right-Rear-Motor-Slave", RobotMap.MOTOR.DRIVE_BACK_RIGHT_VELOCITY_PORT);
			leftRearMotor.follow(leftFrontMotor);
			rightRearMotor.follow(rightFrontMotor);
		}
	}	
	
	public void arcadeDrive(double speed, double turn) {
		drive.arcadeDrive(powerManagementStrategy.calculatePower() * speed, turn);
		SmartDashboard.putNumber("Drive velocity", speed);
		SmartDashboard.putNumber("Drive angle", turn);
	}
	
	public void stop() {
		drive.stopMotor();
	}
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new DriveTeleoperated());
	}

}
