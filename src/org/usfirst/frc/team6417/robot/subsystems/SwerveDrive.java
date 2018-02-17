package org.usfirst.frc.team6417.robot.subsystems;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team6417.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;

public final class SwerveDrive extends Subsystem {
	public final double L = RobotMap.ROBOT.WHEEL_DISTANCE_FRONT_TO_BACK;
	public final double W = RobotMap.ROBOT.WHEEL_DISTANCE_LEFT_TO_RIGHT;
	
	private final List<SwerveWheelDrive> wheelsToCalibrate = new ArrayList<>();
	
	private final SwerveWheelDrive frontLeft, frontRight, backLeft, backRight;
	
	public SwerveDrive() {
		super("SwerveDrive");
		frontLeft = new SwerveWheelDrive(RobotMap.MOTOR.DRIVE_FRONT_LEFT_ANGLE_PORT, 
										 RobotMap.MOTOR.DRIVE_FRONT_LEFT_VELOCITY_PORT, 
										 RobotMap.ENCODER.DRIVE_FRONT_LEFT_PORT_A,
										 RobotMap.ENCODER.DRIVE_FRONT_LEFT_PORT_B,
										 RobotMap.AIO.DRIVE_FRONT_LEFT_POSITION_SENSOR_PORT);
		frontRight = new SwerveWheelDrive(RobotMap.MOTOR.DRIVE_FRONT_RIGHT_ANGLE_PORT, 
										  RobotMap.MOTOR.DRIVE_FRONT_RIGHT_VELOCITY_PORT, 
											 RobotMap.ENCODER.DRIVE_FRONT_RIGHT_PORT_A,
											 RobotMap.ENCODER.DRIVE_FRONT_RIGHT_PORT_B,
											 RobotMap.AIO.DRIVE_FRONT_RIGHT_POSITION_SENSOR_PORT);
		backLeft = new SwerveWheelDrive(RobotMap.MOTOR.DRIVE_BACK_LEFT_ANGLE_PORT, 
										RobotMap.MOTOR.DRIVE_BACK_LEFT_VELOCITY_PORT, 
										 RobotMap.ENCODER.DRIVE_BACK_LEFT_PORT_A,
										 RobotMap.ENCODER.DRIVE_BACK_LEFT_PORT_B,
										 RobotMap.AIO.DRIVE_BACK_LEFT_POSITION_SENSOR_PORT);
		backRight = new SwerveWheelDrive(RobotMap.MOTOR.DRIVE_BACK_RIGHT_ANGLE_PORT, 
										RobotMap.MOTOR.DRIVE_BACK_RIGHT_VELOCITY_PORT, 
										 RobotMap.ENCODER.DRIVE_BACK_RIGHT_PORT_A,
										 RobotMap.ENCODER.DRIVE_BACK_RIGHT_PORT_B,
										 RobotMap.AIO.DRIVE_BACK_RIGHT_POSITION_SENSOR_PORT);
	}
	
	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}
	
	public void drive (double x1, double y1, double x2) {
	    double r = Math.sqrt ((L * L) + (W * W));
	    y1 *= -1;

	    double a = x1 - x2 * (L / r);
	    double b = x1 + x2 * (L / r);
	    double c = y1 - x2 * (W / r);
	    double d = y1 + x2 * (W / r);

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

	public void startZeroPointCalibration() {
	    backRight.startZeroPointCalibration();
	    backLeft.startZeroPointCalibration();
	    frontRight.startZeroPointCalibration();
	    frontLeft.startZeroPointCalibration();	    
	    
	    wheelsToCalibrate.clear();
	    wheelsToCalibrate.add(frontLeft);
	    wheelsToCalibrate.add(frontRight);
	    wheelsToCalibrate.add(backLeft);
	    wheelsToCalibrate.add(frontRight);
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


}
