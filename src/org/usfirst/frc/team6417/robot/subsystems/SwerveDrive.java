package org.usfirst.frc.team6417.robot.subsystems;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team6417.robot.RobotMap;
import org.usfirst.frc.team6417.robot.commands.SwerveDriveTeleoperated;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class SwerveDrive extends Subsystem {
	public final double L = RobotMap.ROBOT.WHEEL_DISTANCE_FRONT_TO_BACK;
	public final double W = RobotMap.ROBOT.WHEEL_DISTANCE_LEFT_TO_RIGHT;
	
	private final List<SwerveWheelDrive> wheelsToCalibrate = new ArrayList<>();
	private final List<SwerveWheelDrive> wheelsToCalibrateParallel = new ArrayList<>();
		
	public final SwerveWheelDrive frontLeft,frontRight,backLeft, backRight;
	
	/**
	 * Ratio of L and W used as the r-vector.
	 */
	private double r;
	
	public SwerveDrive() {
		super("SwerveDrive");
	    r = Math.sqrt ((L * L) + (W * W));

		frontLeft = new SwerveWheelDrive(RobotMap.ROBOT.DRIVE_FRONT_LEFT_NAME, 
										 RobotMap.MOTOR.DRIVE_FRONT_LEFT_ANGLE_PORT, 
										 RobotMap.MOTOR.DRIVE_FRONT_LEFT_VELOCITY_PORT,
										 RobotMap.AIO.DRIVE_FRONT_LEFT_POSITION_SENSOR_PORT,
										 false,
										 false);
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
										 RobotMap.AIO.DRIVE_BACK_RIGHT_POSITION_SENSOR_PORT,
										 true,
										 true);
	}
	
	@Override
	protected void initDefaultCommand() {
//		setDefaultCommand(new SwerveDriveShowZeroPointSensors());
//		setDefaultCommand(new SwerveDriveAngleOnSingleWheel());
		setDefaultCommand(new SwerveDriveTeleoperated());
//		setDefaultCommand(new SwerveDriveRotateAll());
//		setDefaultCommand(new SwerveDriveWheelTeleoperated());
	}
	
	public void drive (double vy, double vx, double rotationClockwise) {
	    double a = vx - rotationClockwise * (L / r);
	    double b = vx + rotationClockwise * (L / r);
	    double c = vy - rotationClockwise * (W / r);
	    double d = vy + rotationClockwise * (W / r);

	    double backRightSpeed = Math.sqrt ((a * a) + (c * c));
	    double backLeftSpeed = Math.sqrt ((a * a) + (d * d));
	    double frontRightSpeed = Math.sqrt ((b * b) + (c * c));
	    double frontLeftSpeed = Math.sqrt ((b * b) + (d * d));

	    double backRightAngle = Math.atan2 (a, c) * RobotMap.MATH.PI;
	    double backLeftAngle = Math.atan2 (a, d) * RobotMap.MATH.PI;
	    double frontRightAngle = Math.atan2 (b, c) * RobotMap.MATH.PI;
	    double frontLeftAngle = Math.atan2 (b, d) * RobotMap.MATH.PI;

	    SmartDashboard.putNumber("FL-V", frontLeftSpeed);
	    SmartDashboard.putNumber("FR-V", frontRightSpeed);
	    SmartDashboard.putNumber("BL-V", backLeftSpeed);
	    SmartDashboard.putNumber("BR-V", -backRightSpeed);
	    SmartDashboard.putNumber("FL-A", frontLeftAngle);
	    SmartDashboard.putNumber("FR-A", frontRightAngle);
	    SmartDashboard.putNumber("BL-A", backLeftAngle);
	    SmartDashboard.putNumber("BR-A", -backRightAngle);
	    
//	    backRight.drive (-backRightSpeed, -backRightAngle);
	    backRight.drive (-backRightSpeed, -backRightAngle);
	    backLeft.drive (backLeftSpeed, backLeftAngle);
	    frontRight.drive (frontRightSpeed, frontRightAngle);
	    frontLeft.drive (frontLeftSpeed, frontLeftAngle);	    
	}
	
	public void driveParallel(double velocity, double angle) {
		angle *= (RobotMap.MATH.PI);
		
		SmartDashboard.putNumber(getName()+"-V", velocity);
		SmartDashboard.putNumber(getName()+"-A", angle);
		
		frontLeft.drive(velocity, angle);
		frontRight.drive(velocity, angle);
		backLeft.drive(velocity, angle);
		backRight.drive(-velocity, -angle);
	}

	public void driveMecanumSimilar(double velocity, double angle) {
		angle *= (RobotMap.MATH.PI);
		
		SmartDashboard.putNumber(getName()+"-V", velocity);
		SmartDashboard.putNumber(getName()+"-A", angle);
		
		frontLeft.drive(velocity, angle);
		frontRight.drive(velocity, angle);
		backLeft.drive(velocity, angle);
		backRight.drive(velocity, angle);
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
	    frontLeft.startParallelCalibration(RobotMap.ROBOT.DRIVE_FRONT_LEFT_ZERO_POINT_CORRECTION_IN_RADIANS);	    
	    frontRight.startParallelCalibration(RobotMap.ROBOT.DRIVE_FRONT_RIGHT_ZERO_POINT_CORRECTION_IN_RADIANS);
	    backLeft.startParallelCalibration(RobotMap.ROBOT.DRIVE_BACK_LEFT_ZERO_POINT_CORRECTION_IN_RADIANS);
	    backRight.startParallelCalibration(RobotMap.ROBOT.DRIVE_BACK_RIGHT_ZERO_POINT_CORRECTION_IN_RADIANS);
	    
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

	public void resetEncoders() {
		frontLeft.resetEncoder();
		frontRight.resetEncoder();
		backLeft.resetEncoder();
		backRight.resetEncoder();
	}

}