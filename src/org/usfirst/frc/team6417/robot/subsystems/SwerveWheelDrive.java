package org.usfirst.frc.team6417.robot.subsystems;

import org.usfirst.frc.team6417.robot.MotorController;
import org.usfirst.frc.team6417.robot.MotorControllerFactory;
import org.usfirst.frc.team6417.robot.RobotMap;
import org.usfirst.frc.team6417.robot.Util;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Implementation of the wheel of a swerve drive
 * 
 * @author https://jacobmisirian.gitbooks.io/frc-swerve-drive-programming/content/part-2-driving.html
 */
public final class SwerveWheelDrive extends Subsystem {
	private enum ANGLE_ROTATION_DIRECTION {
		STOP,
		CLOCKWISE,
		COUNTER_CLOCKWISE
	}

	public final MotorController angleMotor;
	public final MotorController velocityMotor;
	public final AnalogInput positionSensor0;

	private int currentTarget;
	private int correctionAngleInTicks = 1000;
	private ANGLE_ROTATION_DIRECTION currentAngleMotorRotationDirection = ANGLE_ROTATION_DIRECTION.STOP;

	public SwerveWheelDrive(String name,
							int angleMotorPort, 
							int velocityMotorPort,
							int positionSensorPort) {
		super(name);

		MotorControllerFactory factory = new MotorControllerFactory();
		angleMotor = factory.createSmall(name+RobotMap.ROBOT.DRIVE_ANGLE+"/"+angleMotorPort, angleMotorPort);
		velocityMotor = factory.createCIM(name+RobotMap.ROBOT.DRIVE_VELOCITY+"/"+velocityMotorPort, velocityMotorPort);
		velocityMotor.configOpenloopRamp(1, 0);
		
		positionSensor0 = new AnalogInput(positionSensorPort); // DRIVE_FRONT_LEFT_POSITION_SENSOR_PORT
		
		velocityMotor.setInverted(true);
		currentTarget = 0;

		SmartDashboard.putNumber(velocityMotor.getName(), 0);
		SmartDashboard.putNumber(angleMotor.getName(), 0);
		SmartDashboard.putNumber(angleMotor.getName()+" target", currentTarget);
		SmartDashboard.putNumber(angleMotor.getName()+" current", getAngleTicks());

		// pidController = new PIDController (1, 0, 0, new AnalogInput (encoder),
		// this.angleMotor);
		// pidController.setName("Angle-Motor-Controller");

		// pidController.setOutputRange (-1, 1);
		// pidController.setContinuous ();
		// pidController.enable ();
	}

	@Override
	protected void initDefaultCommand() {;}

	public void gotoAngle(double absoluteAngleInRadians) {
		int delta = calculateEncoderTicksForWormGearByAngleOfAngleGear(absoluteAngleInRadians);
		int currentTick = getAngleTicks();
		currentTarget = currentTick + delta;		
		SmartDashboard.putNumber(angleMotor.getName()+" target", currentTarget);
		SmartDashboard.putNumber(angleMotor.getName()+" current", currentTick);
		
		if(Util.eq(currentTarget, currentTick, RobotMap.ENCODER.PULSE_EPSILON)) {
			// angle already reached. do nothing.
			return;
		}
		
//		angleMotor.set(ControlMode.Position, currentTarget);

		if(Util.greaterThen(currentTarget, currentTick, RobotMap.ENCODER.PULSE_EPSILON)) {
			currentAngleMotorRotationDirection = ANGLE_ROTATION_DIRECTION.CLOCKWISE;
		} else if(Util.smallerThen(currentTarget, currentTick, RobotMap.ENCODER.PULSE_EPSILON)) {
			currentAngleMotorRotationDirection = ANGLE_ROTATION_DIRECTION.COUNTER_CLOCKWISE;
		} else if(Util.eq(currentTarget, currentTick, RobotMap.ENCODER.PULSE_EPSILON)) {
			currentAngleMotorRotationDirection = ANGLE_ROTATION_DIRECTION.STOP;
		}

		switch(currentAngleMotorRotationDirection){
		case CLOCKWISE:
			angleMotor.set(RobotMap.VELOCITY.SWERVE_DRIVE_ANGLE_MOTOR_FORWARD_VELOCITY);
			SmartDashboard.putNumber(angleMotor.getName(), RobotMap.VELOCITY.SWERVE_DRIVE_ANGLE_MOTOR_FORWARD_VELOCITY);
			break;
		case COUNTER_CLOCKWISE:
			angleMotor.set(RobotMap.VELOCITY.SWERVE_DRIVE_ANGLE_MOTOR_BACKWARD_VELOCITY);
			SmartDashboard.putNumber(angleMotor.getName(), RobotMap.VELOCITY.SWERVE_DRIVE_ANGLE_MOTOR_BACKWARD_VELOCITY);
			break;
		case STOP:
			default:				
				angleMotor.set(RobotMap.VELOCITY.STOP_VELOCITY);
				SmartDashboard.putNumber(angleMotor.getName(), RobotMap.VELOCITY.STOP_VELOCITY);
		}
	}
	
	private int calculateEncoderTicksForWormGearByAngleOfAngleGear(double angleInRadians) {
		double rotationsToTarget = angleInRadians / RobotMap.ROBOT.SWERVE_ANGLE_PER_WORM_GEAR_ROTATION_IN_RADIANS;
		double rotationsToTargetInPulses = rotationsToTarget * RobotMap.ENCODER.PULSE_PER_ROTATION;
		int pulsesToGo = (int) (rotationsToTargetInPulses);

		SmartDashboard.putNumber(getName()+" angle target", angleInRadians);
		SmartDashboard.putNumber(angleMotor.getName()+" rotations to target", rotationsToTarget);
		SmartDashboard.putNumber(angleMotor.getName()+" pulses target", pulsesToGo);
		SmartDashboard.putNumber(angleMotor.getName()+" pulses target in pulses", rotationsToTargetInPulses);
		SmartDashboard.putNumber(angleMotor.getName()+" angle-error", rotationsToTargetInPulses - (double)pulsesToGo);

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
		velocityMotor.set(speed);
		SmartDashboard.putNumber(velocityMotor.getName(), speed);

		SmartDashboard.putNumber(getName()+" angle", angle);
		gotoAngle(angle * RobotMap.MATH.PI);
	}

	public void tick() {
//		SmartDashboard.putNumber("Swerve Position Sensor", positionSensor0.getValue());
//		SmartDashboard.putNumber("Angle encoder actual", getAngleTicks());
//		SmartDashboard.putNumber("Worm-gear pulses to go", currentTarget - getAngleTicks());
	}
	
	private int getAngleTicks() {
		return angleMotor.getSelectedSensorPosition(MotorController.kSlotIdx);
	}

	public boolean onTarget() {
		boolean isOnTarget = false;
		switch(currentAngleMotorRotationDirection) {
		case CLOCKWISE:{
			if(Util.greaterThen(getAngleTicks(), currentTarget, RobotMap.ENCODER.PULSE_EPSILON)) {
				isOnTarget = true;
			}
		}
		case COUNTER_CLOCKWISE:{
			if(Util.smallerThen(getAngleTicks(), currentTarget, RobotMap.ENCODER.PULSE_EPSILON)) {
				isOnTarget = true;
			}
		}
		case STOP:
		default:
			isOnTarget = true;
		}
		
		if(isOnTarget) {
			angleMotor.set(RobotMap.VELOCITY.STOP_VELOCITY);
			currentAngleMotorRotationDirection = ANGLE_ROTATION_DIRECTION.STOP;
		}

		return isOnTarget;
	}

	public void startZeroPointCalibration() {
		angleMotor.set(RobotMap.VELOCITY.SWERVE_DRIVE_ANGLE_MOTOR_ZEROPOINT_CALIBRATION_VELOCITY);
	}
	
	public boolean isOnZeroPoint() {
		SmartDashboard.putNumber("0-P Wheel "+getName(), positionSensor0.getValue());
//		if(positionSensor0.getValue() > 2600) {
//			return false;
//		}
		final boolean isOnZeroPoint = positionSensor0.getValue() > RobotMap.SENSOR.DRIVE_WHEEL_ZEROPOINT_UPPER_THRESHOLD;
		if(isOnZeroPoint) {
			angleMotor.set(RobotMap.VELOCITY.STOP_VELOCITY);
			angleMotor.setSelectedSensorPosition(RobotMap.ENCODER.INITIAL_VALUE, 
												 MotorController.kSlotIdx, 
												 MotorController.kTimeoutMs);
		}
//		SmartDashboard.putBoolean("Is "+angleMotor.getDeviceID()+" on zero-point", isOnZeroPoint);
//		SmartDashboard.putNumber("Zero-point of "+angleMotor.getDeviceID(), positionSensor0.getValue());
//		SmartDashboard.putNumber("Zero-point voltage of "+angleMotor.getDeviceID(), positionSensor0.getVoltage());
//		SmartDashboard.putNumber("Ticks of "+angleMotor.getDeviceID(), getAngleTicks());
		return isOnZeroPoint;
	}

	public void startParallelCalibration(double angleInRadians) {
		SmartDashboard.putNumber(angleMotor.getName()+" angle", angleInRadians);
		gotoAngle(angleInRadians);
//		angleMotor.set(0.22);
//		
//		if(isRotateClockwise) {
//			angleMotor.set(0.22);
////			correctionAngleInTicks = getAngleTicks() + 40000;
//		}else {
//			angleMotor.set(-0.22);
////			correctionAngleInTicks = getAngleTicks() - 40000;
//		}
//		
//		if(isForward) {
//			correctionAngleInTicks = getAngleTicks() + 40000;
//		}else {
//			correctionAngleInTicks = getAngleTicks() - 40000;
//		}		
	}
	
	public boolean isParallel() {
		SmartDashboard.putNumber("Correction target "+angleMotor.getDeviceID(), correctionAngleInTicks);
		SmartDashboard.putNumber("Current pos "+angleMotor.getDeviceID(), getAngleTicks());
		if(Util.eq(correctionAngleInTicks, getAngleTicks(), RobotMap.ENCODER.PULSE_EPSILON)) {
			angleMotor.set(RobotMap.VELOCITY.STOP_VELOCITY);
			return true;
		}
		return false;
	}

}
