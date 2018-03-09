package org.usfirst.frc.team6417.robot.subsystems;

import org.usfirst.frc.team6417.robot.MotorController;
import org.usfirst.frc.team6417.robot.MotorControllerFactory;
import org.usfirst.frc.team6417.robot.RobotMap;
import org.usfirst.frc.team6417.robot.Util;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Implementation of the wheel of a swerve drive wheel.
 * The wheel's angle is controlled by the external PID controller on the motor-controller hardware.
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
		this(name, angleMotorPort, velocityMotorPort, positionSensorPort, true, false);
	}
	
	public SwerveWheelDrive(String name,
							int angleMotorPort, 
							int velocityMotorPort,
							int positionSensorPort,
							boolean isInvertSensor,
							boolean isInvertVelocityMotor) {
		super(name);

		MotorControllerFactory factory = new MotorControllerFactory();
		angleMotor = factory.create775ProWithEncoder(name+RobotMap.ROBOT.DRIVE_ANGLE+"/"+angleMotorPort, angleMotorPort);
		angleMotor.setSensorPhase(isInvertSensor);
		
		angleMotor.config_kP(MotorController.kPIDLoopIdx, 0.2, MotorController.kTimeoutMs);
		angleMotor.config_kD(MotorController.kPIDLoopIdx, 1.0, MotorController.kTimeoutMs);
		// TODO Use when kI parameter is neccessary
		angleMotor.config_kI(MotorController.kPIDLoopIdx, 0.0, MotorController.kTimeoutMs);
		
		angleMotor.configAllowableClosedloopError(MotorController.kPIDLoopIdx, 100, MotorController.kTimeoutMs);
		velocityMotor = factory.createCIM(name+RobotMap.ROBOT.DRIVE_VELOCITY+"/"+velocityMotorPort, velocityMotorPort);
		velocityMotor.configOpenloopRamp(1, 0);
		velocityMotor.setInverted(isInvertVelocityMotor);
		positionSensor0 = new AnalogInput(positionSensorPort);
		
//		velocityMotor.setInverted(true);
		currentTarget = getAngleTicks();

		SmartDashboard.putNumber(velocityMotor.getName(), 0);
		SmartDashboard.putNumber(angleMotor.getName(), 0);
		SmartDashboard.putNumber(angleMotor.getName()+" target", currentTarget);
		SmartDashboard.putNumber(angleMotor.getName()+" current", getAngleTicks());
	}

	@Override
	protected void initDefaultCommand() {;}

	public void gotoAngle(double absoluteAngleInRadians) {
		int position = calculateEncoderTicksForWormGearByAngleOfAngleGear(absoluteAngleInRadians);
		SmartDashboard.putNumber(angleMotor.getName()+" to-pos", position);
		SmartDashboard.putNumber(angleMotor.getName()+" curr-pos", getAngleTicks());
		
		angleMotor.set(ControlMode.Position, position);
	}
	
	private int calculateEncoderTicksForWormGearByAngleOfAngleGear(double angleInRadians) {
		double rotationsToTarget = angleInRadians / RobotMap.ROBOT.SWERVE_ANGLE_PER_WORM_GEAR_ROTATION_IN_RADIANS;
		double rotationsToTargetInPulses = rotationsToTarget * RobotMap.ENCODER.PULSE_PER_ROTATION;
		int pulsesToGo = (int) (rotationsToTargetInPulses);

		SmartDashboard.putNumber(angleMotor.getName()+" pttp", rotationsToTargetInPulses);
		SmartDashboard.putNumber(angleMotor.getName()+" angle-error", rotationsToTargetInPulses - (double)pulsesToGo);

		return pulsesToGo;
	}

	public void drive(double speed, double angle) {
		velocityMotor.set(speed);
		SmartDashboard.putNumber(velocityMotor.getName(), speed);
		SmartDashboard.putNumber(angleMotor.getName(), angle);
		gotoAngle(angle);
	}

	public void tick() {;}
	
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
		SmartDashboard.putNumber(getName()+" zero-point", positionSensor0.getValue());
//		if(positionSensor0.getValue() > 2600) {
//			return false;
//		}
		final boolean isOnZeroPoint = positionSensor0.getValue() > RobotMap.SENSOR.DRIVE_WHEEL_ZEROPOINT_UPPER_THRESHOLD;
		SmartDashboard.putNumber(getName()+" logic zero-point", isOnZeroPoint?1:0);
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

	public void resetEncoder() {
		angleMotor.setSelectedSensorPosition(0, 0, 10);
	}

}
