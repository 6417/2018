package org.usfirst.frc.team6417.robot.subsystems;

import org.usfirst.frc.team6417.robot.MotorController;
import org.usfirst.frc.team6417.robot.MotorControllerFactory;
import org.usfirst.frc.team6417.robot.RobotMap;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogTrigger;
import edu.wpi.first.wpilibj.Counter;
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
	private final AnalogInput positionSensor0;
	private final Counter zeroPointCounter;

	private int currentTarget;

	public SwerveWheelDrive(int angleMotorPort, 
							int velocityMotorPort,
							int positionSensorPort) {
		super("SwerveWheelDrive");

		MotorControllerFactory factory = new MotorControllerFactory();
		angleMotor = factory.createSmall("Angle-Motor", angleMotorPort);
		velocityMotor = factory.createCIM("Speed-Motor", velocityMotorPort);
		velocityMotor.configOpenloopRamp(1, 0);
		
		positionSensor0 = new AnalogInput(positionSensorPort); // DRIVE_FRONT_LEFT_POSITION_SENSOR_PORT

		AnalogTrigger analogTrigger = new AnalogTrigger(positionSensor0);
		analogTrigger.setLimitsRaw(RobotMap.SENSOR.DRIVE_WHEEL_ZEROPOINT_LOWER_THRESHOLD, 
				                   RobotMap.SENSOR.DRIVE_WHEEL_ZEROPOINT_UPPER_THRESHOLD);
		zeroPointCounter = new Counter(analogTrigger);
		
		velocityMotor.setInverted(true);
		currentTarget = 0;
		zeroPointCounter.reset();

		SmartDashboard.putNumber("Swerve Velocity", 0);
		SmartDashboard.putNumber("Swerve Angle Nominal", 0);
		SmartDashboard.putNumber("Swerve Angle Actual", getAngleTicks());

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
		int currentTick = getAngleTicks();
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
		SmartDashboard.putNumber("Swerve Angle Actual", getAngleTicks());
		SmartDashboard.putNumber("Swerve Position Sensor", positionSensor0.getValue());

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
		SmartDashboard.putNumber("Angle encoder actual", getAngleTicks());
		SmartDashboard.putNumber("Worm-gear pulses to go", currentTarget - getAngleTicks());
	}
	
	private int getAngleTicks() {
		return angleMotor.getSelectedSensorPosition(MotorController.kSlotIdx);
	}

	public boolean onTarget() {
		boolean isOnTarget = eq(getAngleTicks(), currentTarget);
		if (isOnTarget) {
			angleMotor.set(RobotMap.VELOCITY.STOP_VELOCITY);
		}
		return isOnTarget;
	}

	public void startZeroPointCalibration() {
		zeroPointCounter.reset();
		angleMotor.set(RobotMap.VELOCITY.SWERVE_DRIVE_ANGLE_MOTOR_ZEROPOINT_CALIBRATION_VELOCITY);
	}
	
	public boolean isOnZeroPoint() {
		final boolean isOnZeroPoint =  zeroPointCounter.get() > 0;
		if(isOnZeroPoint) {
			angleMotor.set(RobotMap.VELOCITY.STOP_VELOCITY);
			angleMotor.setSelectedSensorPosition(RobotMap.ENCODER.INITIAL_VALUE, 
												 MotorController.kSlotIdx, 
												 MotorController.kTimeoutMs);
			zeroPointCounter.reset();
		}
		return isOnZeroPoint;
	}

}
