package org.usfirst.frc.team6417.robot.subsystems;

import org.usfirst.frc.team6417.robot.MotorController;
import org.usfirst.frc.team6417.robot.MotorControllerFactory;
import org.usfirst.frc.team6417.robot.RobotMap;
import org.usfirst.frc.team6417.robot.Util;
import org.usfirst.frc.team6417.robot.commands.LiftingUnitTeleoperated;
import org.usfirst.frc.team6417.robot.service.powermanagement.PowerManagementStrategy;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class LiftingUnit extends Subsystem {
	private final double MOTOR_ROTATIONS_PER_CHAIN_WHEEL_ROTATION = calculateGearRatio();

	private final MotorController motorA, motorB;
	private final PowerManagementStrategy powerManagementStrategy;

	private int currentPosition;
	private boolean isHoldingPosition;
	
	public LiftingUnit(PowerManagementStrategy powerManagementStrategy) {
		super("LiftingUnit");
		
		this.powerManagementStrategy = powerManagementStrategy;
		
		final MotorControllerFactory factory = new MotorControllerFactory();
		motorA = factory.create777ProWithPositionControl("Motor-A-Master", RobotMap.MOTOR.LIFTING_UNIT_PORT_A);
		motorA.setInverted(true);
		motorA.setSensorPhase(false);
		motorA.setNeutralMode(NeutralMode.Brake);
		motorA.configOpenloopRamp(0, MotorController.kTimeoutMs);
		motorA.configClosedloopRamp(0, MotorController.kTimeoutMs);
		
		motorA.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, MotorController.kPIDLoopIdx ,
				MotorController.kTimeoutMs );
		
		motorA.config_kF(MotorController.kPIDLoopIdx, 0.0, MotorController.kTimeoutMs);
		motorA.config_kP(MotorController.kPIDLoopIdx, 0.2, MotorController.kTimeoutMs);
		motorA.config_kI(MotorController.kPIDLoopIdx, 0.0, MotorController.kTimeoutMs);
		motorA.config_kD(MotorController.kPIDLoopIdx, 0.0, MotorController.kTimeoutMs);
		
		int allowedErrorPercentage = 10;
		int allowedErrorRelative = RobotMap.ENCODER.PULSE_PER_ROTATION_VERSA_PLANETARY / allowedErrorPercentage;
		motorA.configAllowableClosedloopError(0, allowedErrorRelative, MotorController.kTimeoutMs);
		
		motorB = factory.create775Pro("Motor-B-Slave", RobotMap.MOTOR.LIFTING_UNIT_PORT_B);
		motorB.setNeutralMode(NeutralMode.Brake);
		motorB.setInverted(true);
		motorB.follow(motorA);

		reset();
	}
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new LiftingUnitTeleoperated());
	}

	public void moveToPos(double posRelative) {
		posRelative = Util.limit(posRelative, -1.0, 1.0);
		SmartDashboard.putNumber("LU pos rel", posRelative);
		double absolutePos = posRelative * RobotMap.ROBOT.LIFTING_UNIT_SCALE_HIGH_ALTITUDE_IN_TICKS;
		moveToAbsolutePos(absolutePos);
	}
	
	public void moveToAbsolutePos(double posAbsolute) {
		if(isHoldingPosition) {
			return;
		}
		
		SmartDashboard.putNumber("LU pos abs", posAbsolute);
		motorA.set(ControlMode.Position, posAbsolute);
		setHoldPosition(true);
	}

	public void move(double velocity) {
		velocity = Util.limit(velocity, -1.0, 1.0);
		SmartDashboard.putNumber("LU vel", velocity);

		if(isInEndpointTop()) {
			motorA.set(RobotMap.VELOCITY.STOP_VELOCITY);
			holdPosition();
		} else if(isInEndpointBottom()) {
			motorA.set(RobotMap.VELOCITY.STOP_VELOCITY);
			holdPosition();
		} else {
			motorA.set(ControlMode.PercentOutput, velocity);
			setHoldPosition(false);
		}
//		
//		if(velocity > 0) {
//			if(isInEndpointTop()) {
//				motorA.set(RobotMap.VELOCITY.STOP_VELOCITY);
//				holdPosition();
//			}else {
//				motorA.set(ControlMode.PercentOutput, velocity);
//			}
//		}else if(velocity < 0) {
//			if(isInEndpointBottom()) {
//				motorA.set(RobotMap.VELOCITY.STOP_VELOCITY);
//				holdPosition();
//			}else {
//				motorA.set(ControlMode.PercentOutput, velocity);
//			}
//		}else {
//			holdPosition();
//		}
	}
	

	public void reset() {
		// Reset the encoder to 0. 
		// Only motorA has an encoder so only motorA gets the reset:
		motorA.setSelectedSensorPosition(0, MotorController.kPIDLoopIdx, MotorController.kTimeoutMs);
		SmartDashboard.putNumber(motorA.getName()+" curr pos", motorA.getSelectedSensorPosition(0));
	}
		
	private int getCurrentPosition() {
		return motorA.getSelectedSensorPosition(MotorController.kPIDLoopIdx);
	}
	
	private boolean isInEndpointTop() {
		final boolean isInEndpoint = Util.greaterThen(getCurrentPosition(), 
				RobotMap.ROBOT.LIFTING_UNIT_SCALE_HIGH_ALTITUDE_IN_TICKS - RobotMap.ENCODER.PULSE_PER_ROTATION_VERSA_PLANETARY, 
				RobotMap.ENCODER.PULSE_VERSA_PLANETARY_EPSILON);
		SmartDashboard.putBoolean(motorA.getName()+" up", isInEndpoint);
		return isInEndpoint;
	}

	private boolean isInEndpointBottom() {
		final boolean isInEndpoint = Util.smallerThen(getCurrentPosition(), 
				RobotMap.ROBOT.LIFTING_UNIT_GROUND_ALTITUDE_IN_TICKS + RobotMap.ENCODER.PULSE_PER_ROTATION_VERSA_PLANETARY, 
				RobotMap.ENCODER.PULSE_VERSA_PLANETARY_EPSILON);
		SmartDashboard.putBoolean(motorA.getName()+" down", isInEndpoint);
		return isInEndpoint;
	}
	
	public boolean isInEndpoint() {
		return isInEndpointBottom() || isInEndpointTop();
	}

	public void holdPosition() {
		currentPosition = getCurrentPosition();
		moveToAbsolutePos(currentPosition);
	}
	
	private void setHoldPosition(boolean isPosCtrl) {
		isHoldingPosition = isPosCtrl;
		SmartDashboard.putBoolean("LU pos ctrl", isHoldingPosition);
		SmartDashboard.putBoolean("LU vel ctrl", !isHoldingPosition);
	}


	private double calculateGearRatio() {
		// See documentation here:
		// https://docs.google.com/document/d/1cqJp1bNhLM5h0Qkk95_VV4HTZDqJ9aykWqInW393COQ/edit?usp=sharing
		double c = 37.0;
		double d = 12.0;
		double e = 13.0;
		double f = 37.0;
		double iGesamt = d/c * f/e;
//		double divisor = 4.0 * RobotMap.MATH.PI * RobotMap.ROBOT.LIFTING_UNIT_CHAIN_WHEEL_RADIUS_IN_METER;
		return iGesamt;
	}

	public void liftToPosition(double positionInMeters) {
		positionInMeters = positionInMeters / 2.0; // lift is double due to robot construction. Reduce altitude by 1/2.
		double chainWheelRotations = calculateChainWheelRotations(positionInMeters);
		double motorRotations = calculateMotorRotations(chainWheelRotations);		
		double pulsesToTarget = motorRotations * RobotMap.ENCODER.PULSE_PER_ROTATION;
		int ticksToTarget = (int)pulsesToTarget;
		double error = pulsesToTarget - ticksToTarget;
		
		SmartDashboard.putNumber("LU lift to pos in m", positionInMeters);
		SmartDashboard.putNumber("LU chain-wheel rotations", chainWheelRotations);
		SmartDashboard.putNumber("LU motor rotations", motorRotations);
		SmartDashboard.putNumber("LU ticks to target", ticksToTarget);
		SmartDashboard.putNumber("LU error", error);
		
		motorA.set(ControlMode.Position, ticksToTarget);
	}

	private double calculateMotorRotations(double chainWheelRotations) {
		return MOTOR_ROTATIONS_PER_CHAIN_WHEEL_ROTATION * chainWheelRotations;
	}

	private double calculateChainWheelRotations(double positionInMeters) {
		double chainWheelRadiusInMeter = RobotMap.ROBOT.LIFTING_UNIT_CHAIN_WHEEL_RADIUS_IN_METER;//m
		return 2.0 * RobotMap.MATH.PI * chainWheelRadiusInMeter;
	}

}
