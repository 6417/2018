package org.usfirst.frc.team6417.robot.subsystems;

import org.usfirst.frc.team6417.robot.MotorController;
import org.usfirst.frc.team6417.robot.MotorControllerFactory;
import org.usfirst.frc.team6417.robot.RobotMap;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Implementation of the wheel of a swerve drive
 * 
 * @author https://jacobmisirian.gitbooks.io/frc-swerve-drive-programming/content/part-2-driving.html
 */
public final class SwerveWheelDrive extends Subsystem {
	private final MotorController angleMotor;
	private final MotorController velocityMotor;
	private final Encoder angleEncoder;
	private int currentTarget;

	public SwerveWheelDrive(int angleMotorPort, int velocityMotorPort, int encoderPort) {
		super("SwerveWheelDrive");

		MotorControllerFactory factory = new MotorControllerFactory();
		angleMotor = factory.createSmall("Angle-Motor", angleMotorPort);
		velocityMotor = factory.createCIM("Speed-Motor", velocityMotorPort);
		angleEncoder = new Encoder(encoderPort, encoderPort+1);
		
		velocityMotor.setInverted(true);
		angleEncoder.reset();
		currentTarget = 0;

		SmartDashboard.putNumber("Swerve Velocity", 0);
		SmartDashboard.putNumber("Swerve Angle Nominal", 0);
		SmartDashboard.putNumber("Swerve Angle Actual", angleEncoder.get());

		// pidController = new PIDController (1, 0, 0, new AnalogInput (encoder),
		// this.angleMotor);
		// pidController.setName("Angle-Motor-Controller");

		// pidController.setOutputRange (-1, 1);
		// pidController.setContinuous ();
		// pidController.enable ();
	}

	@Override
	protected void initDefaultCommand() {;}

	public void gotoAngle(double angleInRadians) {
		int delta = calculateEncoderTicksForWormGearByAngleOfAngleGear(angleInRadians);
		int currentTick = angleEncoder.get();
		currentTarget = currentTick + delta;
		
		if (greaterThen(currentTarget, currentTick)) {
			angleMotor.set(RobotMap.VELOCITY.SWERVE_DRIVE_ANGLE_MOTOR_FORWARD_VELOCITY);
		} else if (smallerThen(currentTarget, currentTick)) {
			angleMotor.set(RobotMap.VELOCITY.SWERVE_DRIVE_ANGLE_MOTOR_BACKWARD_VELOCITY);
		} else {
			angleMotor.set(RobotMap.VELOCITY.STOP_VELOCITY);
		}
		
		SmartDashboard.putNumber("Worm-gear target", currentTarget);
	}
	
	private int calculateEncoderTicksForWormGearByAngleOfAngleGear(double angleInRadians) {
		double rotationsToTarget = angleInRadians / RobotMap.ROBOT.SWERVE_ANGLE_PER_WORM_GEAR_ROTATION_IN_RADIANS;
		double rotationsToTargetInPulses = rotationsToTarget * RobotMap.ENCODER.PULSE_PER_ROTATION;
		int pulsesToGo = (int) (rotationsToTargetInPulses);

		SmartDashboard.putNumber("Angle-gear angle nominal", angleInRadians);
		SmartDashboard.putNumber("Worm-gear rotations to target", rotationsToTarget);
		SmartDashboard.putNumber("Worm-gear rotations", rotationsToTargetInPulses);
		SmartDashboard.putNumber("Worm-gear pulses to go", pulsesToGo);

		return pulsesToGo;
	}
//	
//	
//	private void toAngleInRadians(double angleInRadians) {
//		// theEncoder.reset();
//		double rotation = angleInRadians * (125.0 / 360.0);// / RobotMap.ROBOT.SWERVE_ANGLE_GEAR_RATIO * angle;
//		// double rotation = angleInRadians /
//		// RobotMap.ROBOT.SWERVE_ANGLE_PER_WORM_GEAR_ROTATION;// /
//		// RobotMap.ROBOT.SWERVE_ANGLE_GEAR_RATIO * angle;
//		double rotationInPulses = rotation * RobotMap.ENCODER.PULSE_PER_ROTATION;
//		int pulsesToCount = (int) (rotationInPulses);
//
////		error += rotationInPulses - (double) pulsesToCount;
////
////		SmartDashboard.putNumber("Expected Error", error);
//		SmartDashboard.putNumber("Angle encoder actual", angleEncoder.get());
//		SmartDashboard.putNumber("Worm-gear rotations", pulsesToCount);
//
//		angleMotor.set(0.6);
//		while (angleEncoder.get() <= pulsesToCount) {
//			// ;
//			SmartDashboard.putNumber("Angle encoder actual", angleEncoder.get());
//			SmartDashboard.putNumber("Worm-gear rotations", pulsesToCount);
//		}
//		angleMotor.set(0.0);
//	}
//
//	public void toAngle(double angleInDegrees) {
//		// toAngleInRadians(angleInDegrees * RobotMap.MATH.PI / 180.0);
//		toAngleInRadians(angleInDegrees);
//	}

	

	public void drive(double speed, double angle) {
		SmartDashboard.putNumber("Swerve Velocity", speed);
		SmartDashboard.putNumber("Swerve Angle Nominal", angle);
		SmartDashboard.putNumber("Swerve Angle Actual", angleEncoder.get());

		velocityMotor.set(speed);
		gotoAngle(angle * RobotMap.MATH.PI);
		// pidController.setSetpoint (setpoint);
	}



	private boolean smallerThen(int a, int b) {
		return (b - a > RobotMap.ENCODER.EPSILON);
	}

	private boolean greaterThen(int a, int b) {
		return (a - b > RobotMap.ENCODER.EPSILON);
	}

	private boolean eq(int a, int b) {
		return (a == b ? true : Math.abs(a - b) < RobotMap.ENCODER.EPSILON);
	}

	public void tick() {
		SmartDashboard.putNumber("Angle encoder actual", angleEncoder.get());
		SmartDashboard.putNumber("Worm-gear pulses to go", currentTarget - angleEncoder.get());
	}

	public boolean onTarget() {
		boolean isOnTarget = eq(angleEncoder.get(), currentTarget);
		if (isOnTarget) {
			angleMotor.set(RobotMap.VELOCITY.STOP_VELOCITY);
		}
		return isOnTarget;
	}
}
