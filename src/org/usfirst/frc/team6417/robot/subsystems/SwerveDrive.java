package org.usfirst.frc.team6417.robot.subsystems;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team6417.robot.RobotMap;
import org.usfirst.frc.team6417.robot.Util;

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
										 false,
										 RobotMap.SENSOR.DRIVE_FRONT_LEFT_WHEEL_ZEROPOINT_UPPER_THRESHOLD,
										 false);
		frontRight = new SwerveWheelDrive(RobotMap.ROBOT.DRIVE_FRONT_RIGHT_NAME, 
				 						  RobotMap.MOTOR.DRIVE_FRONT_RIGHT_ANGLE_PORT, 
										  RobotMap.MOTOR.DRIVE_FRONT_RIGHT_VELOCITY_PORT, 
										  RobotMap.AIO.DRIVE_FRONT_RIGHT_POSITION_SENSOR_PORT,
										  RobotMap.SENSOR.DRIVE_FRONT_RIGHT_WHEEL_ZEROPOINT_UPPER_THRESHOLD,
										  true);
		backLeft = new SwerveWheelDrive(RobotMap.ROBOT.DRIVE_BACK_LEFT_NAME, 
				 						 RobotMap.MOTOR.DRIVE_BACK_LEFT_ANGLE_PORT, 
										 RobotMap.MOTOR.DRIVE_BACK_LEFT_VELOCITY_PORT, 
										 RobotMap.AIO.DRIVE_BACK_LEFT_POSITION_SENSOR_PORT,
										 RobotMap.SENSOR.DRIVE_BACK_LEFT_WHEEL_ZEROPOINT_UPPER_THRESHOLD,
										 false);
		backRight = new SwerveWheelDrive(RobotMap.ROBOT.DRIVE_BACK_RIGHT_NAME, 
				 						 RobotMap.MOTOR.DRIVE_BACK_RIGHT_ANGLE_PORT, 
										 RobotMap.MOTOR.DRIVE_BACK_RIGHT_VELOCITY_PORT, 
										 RobotMap.AIO.DRIVE_BACK_RIGHT_POSITION_SENSOR_PORT,
										 true,
										 false,
										 RobotMap.SENSOR.DRIVE_BACK_RIGHT_WHEEL_ZEROPOINT_UPPER_THRESHOLD,
										 true);
	}
	
	@Override
	protected void initDefaultCommand() {
//		setDefaultCommand(new SwerveDriveShowZeroPointSensors());
//		setDefaultCommand(new SwerveDriveAngleOnSingleWheel());
//		setDefaultCommand(new SwerveDriveTeleoperated());
//		setDefaultCommand(new SwerveDriveRotateAll());
//		setDefaultCommand(new SwerveDriveWheelTeleoperated());
	}
	
	
	public void drive(double vy, double vx, double rotationClockwise) {
		if(Util.eq(vy, 0.0, 0.01) && Util.eq(vx, 0.0, 0.01) && Util.eq(rotationClockwise, 0.0, 0.01)) {
//			System.out.println("SwerveDrive.drive with vy=0");
		    backRight.drive (0, 0);
		    backLeft.drive (0, 0);
		    frontRight.drive (0, 0);
		    frontLeft.drive (0, 0);	    
			return;
		}
//		System.out.println("SwerveDrive.drive()");
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

	    // Scale all velocities to the 0..1 range
	    double max=frontLeftSpeed; 
	    if(frontRightSpeed>max) {max=frontRightSpeed;} 
	    if(backLeftSpeed>max) {max=backLeftSpeed;}
	    if(backRightSpeed>max) {max=backRightSpeed;}
	    if(max>1.0){
	    	frontLeftSpeed/=max; 
	    	frontRightSpeed/=max; 
	    	backLeftSpeed/=max; 
	    	backRightSpeed/=max;
	    }
	    
	    SmartDashboard.putNumber("FL-V vel", frontLeftSpeed);
	    SmartDashboard.putNumber("FR-V vel", frontRightSpeed);
	    SmartDashboard.putNumber("BL-V vel", backLeftSpeed);
	    SmartDashboard.putNumber("BR-V vel", backRightSpeed);
	    SmartDashboard.putNumber("FL-A angle", frontLeftAngle);
	    SmartDashboard.putNumber("FR-A angle", frontRightAngle);
	    SmartDashboard.putNumber("BL-A angle", backLeftAngle);
	    SmartDashboard.putNumber("BR-A angle", -backRightAngle);
	    
	    backRight.drive (backRightSpeed, -backRightAngle);
	    backLeft.drive (backLeftSpeed, backLeftAngle);
	    frontRight.drive (frontRightSpeed, frontRightAngle);
	    frontLeft.drive (frontLeftSpeed, frontLeftAngle);	    
	}
	
	public void driveParallel(double velocity, double angle) {
//		System.out.println("SwerveDrive.driveParallel()");
		angle *= (RobotMap.MATH.PI);
		
		SmartDashboard.putNumber(getName()+"-V", velocity);
		SmartDashboard.putNumber(getName()+"-A", angle);
		
		frontLeft.drive(velocity, angle);
		frontRight.drive(velocity, angle);
		backLeft.drive(velocity, angle);
		backRight.drive(velocity, -angle);
	}
	
	double oldAngle = 0;
	
	public void driveJulian(double x, double y) {
		double steps = frontRight.angleMotor.getSelectedSensorPosition(0);
		double q = 360 / 512 * steps;
		double winkel = 60 + q;//Math.atan2(y, x) / 180 * Math.PI;
		double a = winkel - Math.floor(winkel / 180.0)*180.0;
		double b = a / Math.floor(a);
		double c = Math.floor(winkel / 180.0);
		
		double rotation = b * 2.0 - 1.0;
		double velocity = (((c+b) % 2) * 2) - 1;

		System.out.println("SwerveDrive.driveJulian(angle: "+rotation+", vel: "+velocity+")");
//		frontLeft.drive(velocity, rotation);
		frontRight.drive(velocity, rotation);
//		backLeft.drive(velocity, rotation);
//		backRight.drive(velocity, rotation);
	}

	public void driveChristian(double x, double y, double twist){
		double maxDriveSpeed = 1;
		double maxTurnSpeed = 1;
		
		double angle = ((Math.atan2(y, x) + ((3 * Math.PI)/4)) % 360);
		double radius = (Math.sqrt((x*x)+(y*y)));
		
		double velocityLeft = (((radius*maxDriveSpeed)+(twist*maxTurnSpeed))/(maxTurnSpeed+maxDriveSpeed));
		double velocityRight = (((radius*maxDriveSpeed)-(twist*maxTurnSpeed))/(maxTurnSpeed+maxDriveSpeed));
		
		frontLeft.drive(velocityLeft, angle);
		frontRight.drive(velocityRight, angle);
		backLeft.drive(velocityLeft, angle);
		backRight.drive(velocityRight, -angle);
	}
	
	public void driveJKCG() {}
	
	public void driveMecanumSimilar(double velocity, double angle) {
		angle *= (RobotMap.MATH.PI);
		
		
		SmartDashboard.putNumber(getName()+"-V", velocity);
		SmartDashboard.putNumber(getName()+"-A", angle);
		
		frontLeft.drive(velocity, angle);
		frontRight.drive(velocity, angle);
		backLeft.drive(velocity, angle);
		backRight.drive(velocity, angle);
	}

	public void gotoAngle(double angle) {
		frontLeft.gotoAngle(angle);
		frontRight.gotoAngle(angle);
		backLeft.gotoAngle(angle);
		backRight.gotoAngle(angle);
		
	}
	public boolean isAnglesOnTarget() {
	    return (frontLeft.onTarget() && 
	    		frontRight.onTarget() && 
	    		backLeft.onTarget() && 
	    		backRight.onTarget());
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

	public void resetAngleEncoders() {
		frontLeft.resetAngleEncoder();
		frontRight.resetAngleEncoder();
		backLeft.resetAngleEncoder();
		backRight.resetAngleEncoder();
	}
	public void resetVelocityEncoders() {
		frontLeft.resetVelocityEncoder();
		frontRight.resetVelocityEncoder();		
		backLeft.resetVelocityEncoder();
		backRight.resetVelocityEncoder();
	}

}