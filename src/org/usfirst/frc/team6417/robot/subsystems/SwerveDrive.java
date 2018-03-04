package org.usfirst.frc.team6417.robot.subsystems;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team6417.robot.Robot;
import org.usfirst.frc.team6417.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class SwerveDrive extends Subsystem {
	public final double L = RobotMap.ROBOT.WHEEL_DISTANCE_FRONT_TO_BACK;
	public final double W = RobotMap.ROBOT.WHEEL_DISTANCE_LEFT_TO_RIGHT;
	
	private final List<SwerveWheelDrive> wheelsToCalibrate = new ArrayList<>();
	private final List<SwerveWheelDrive> wheelsToCalibrateParallel = new ArrayList<>();
		
	public final SwerveWheelDrive frontLeft, frontRight, backLeft, backRight;
	
	public SwerveDrive() {
		super("SwerveDrive");
		frontLeft = new SwerveWheelDrive(RobotMap.ROBOT.DRIVE_FRONT_LEFT_NAME, 
										 RobotMap.MOTOR.DRIVE_FRONT_LEFT_ANGLE_PORT, 
										 RobotMap.MOTOR.DRIVE_FRONT_LEFT_VELOCITY_PORT,
										 RobotMap.AIO.DRIVE_FRONT_LEFT_POSITION_SENSOR_PORT);
		frontRight = new SwerveWheelDrive(RobotMap.ROBOT.DRIVE_FRONT_RIGHT_NAME, 
				 						  RobotMap.MOTOR.DRIVE_FRONT_RIGHT_ANGLE_PORT, 
										  RobotMap.MOTOR.DRIVE_FRONT_RIGHT_VELOCITY_PORT, 
										  RobotMap.AIO.DRIVE_FRONT_RIGHT_POSITION_SENSOR_PORT);
		backLeft = new SwerveWheelDrive(RobotMap.ROBOT.DRIVE_BACK_LEFT_NAME, 
				 						 RobotMap.MOTOR.DRIVE_BACK_LEFT_ANGLE_PORT, 
										 RobotMap.MOTOR.DRIVE_BACK_LEFT_VELOCITY_PORT, 
										 RobotMap.AIO.DRIVE_BACK_LEFT_POSITION_SENSOR_PORT);
		backRight = new SwerveWheelDrive(RobotMap.ROBOT.DRIVE_BACK_RIGHT_NAME, 
				 						 RobotMap.MOTOR.DRIVE_BACK_RIGHT_ANGLE_PORT, 
										 RobotMap.MOTOR.DRIVE_BACK_RIGHT_VELOCITY_PORT, 
										 RobotMap.AIO.DRIVE_BACK_RIGHT_POSITION_SENSOR_PORT);
	}
	
	@Override
	protected void initDefaultCommand() {
//		setDefaultCommand(new SwerveDriveShowZeroPointSensors());
//		setDefaultCommand(new SwerveDriveTeleoperated());
//		setDefaultCommand(new A());
//		setDefaultCommand(new SwerveDriveRotateAll());
//		setDefaultCommand(new SwerveDriveWheelTeleoperated());
	}
	
	private class A extends Command {
		
		public A() {
			requires(Robot.swerveDrive);
		}
		@Override
		protected void execute() {
			SmartDashboard.putNumber("Sensor FL "+frontLeft.positionSensor0.getChannel(), frontLeft.positionSensor0.getValue());
			SmartDashboard.putNumber("Sensor FR "+frontRight.positionSensor0.getChannel(), frontRight.positionSensor0.getValue());
			SmartDashboard.putNumber("Sensor BL "+backLeft.positionSensor0.getChannel(), backLeft.positionSensor0.getValue());
			SmartDashboard.putNumber("Sensor BR "+backRight.positionSensor0.getChannel(), backRight.positionSensor0.getValue());
		}
		
		@Override
		protected boolean isFinished() {
			return false;
		}		
	}
	
	public void drive (double v, double y, double omega) {
	    double r = Math.sqrt ((L * L) + (W * W));
	    y *= -1.0;

	    double a = v - omega * (L / r);
	    double b = v + omega * (L / r);
	    double c = y - omega * (W / r);
	    double d = y + omega * (W / r);

	    double backRightSpeed = Math.sqrt ((a * a) + (d * d));
	    double backLeftSpeed = Math.sqrt ((a * a) + (c * c));
	    double frontRightSpeed = Math.sqrt ((b * b) + (d * d));
	    double frontLeftSpeed = Math.sqrt ((b * b) + (c * c));

	    double backRightAngle = Math.atan2 (a, d) / Math.PI;
	    double backLeftAngle = Math.atan2 (a, c) / Math.PI;
	    double frontRightAngle = Math.atan2 (b, d) / Math.PI;
	    double frontLeftAngle = Math.atan2 (b, c) / Math.PI;
	    
	    backRight.drive (backRightSpeed, backRightAngle);
	    backLeft.drive (backLeftSpeed, backLeftAngle);
	    frontRight.drive (frontRightSpeed, frontRightAngle);
	    frontLeft.drive (frontLeftSpeed, frontLeftAngle);	    
	}
	
	public void checkAnglesOnTarget() {
	    frontLeft.onTarget();	    
	    frontRight.onTarget();
	    backLeft.onTarget();
	    backRight.onTarget();
	}

	public void startZeroPointCalibration() {
	    backRight.startZeroPointCalibration();
	    backLeft.startZeroPointCalibration();
	    frontRight.startZeroPointCalibration();
	    frontLeft.startZeroPointCalibration();	    
	    
	    wheelsToCalibrate.clear();
	    wheelsToCalibrate.add(frontLeft);
	    wheelsToCalibrate.add(frontRight);
	    wheelsToCalibrate.add(backLeft);
	    wheelsToCalibrate.add(backRight);
	}
	
	public boolean isZeroPointCalibrationFinished() {
		List<SwerveWheelDrive> dd = new ArrayList<>(wheelsToCalibrate);
		for(SwerveWheelDrive d : dd) {
			if(d.isOnZeroPoint()) {
				wheelsToCalibrate.remove(d);
			}
		}
		
		return wheelsToCalibrate.isEmpty();
	}

	public void startParallelCalibration() {
//	    frontLeft.startParallelCalibration(false, false);	    
//	    frontRight.startParallelCalibration(true, true);
//	    backLeft.startParallelCalibration(true, false);
//	    backRight.startParallelCalibration(false, true);
		
	    frontLeft.startParallelCalibration(-RobotMap.MATH.PI * 0.25);	    
	    frontRight.startParallelCalibration(RobotMap.MATH.PI * 0.25);
	    backLeft.startParallelCalibration(RobotMap.MATH.PI * 0.25);
	    backRight.startParallelCalibration(-RobotMap.MATH.PI * 0.25);
	    
	    wheelsToCalibrateParallel.clear();
	    wheelsToCalibrateParallel.add(frontLeft);
	    wheelsToCalibrateParallel.add(frontRight);
	    wheelsToCalibrateParallel.add(backLeft);
	    wheelsToCalibrateParallel.add(backRight);
	}

	public boolean isParallelCalibration() {
		List<SwerveWheelDrive> dd = new ArrayList<>(wheelsToCalibrateParallel);
		for(SwerveWheelDrive d : dd) {
			if(d.isParallel()) {
				wheelsToCalibrateParallel.remove(d);
			}
		}
		
		return wheelsToCalibrateParallel.isEmpty();
	}

}
