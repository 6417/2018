package org.usfirst.frc.team6417.robot.subsystems;

import org.usfirst.frc.team6417.robot.MotorController;
import org.usfirst.frc.team6417.robot.MotorControllerFactory;
import org.usfirst.frc.team6417.robot.OI;
import org.usfirst.frc.team6417.robot.RobotMap;
import org.usfirst.frc.team6417.robot.Util;
import org.usfirst.frc.team6417.robot.commands.LiftingUnitTeleoperated;
import org.usfirst.frc.team6417.robot.model.State;
import org.usfirst.frc.team6417.robot.model.velocitymanagement.MotionPathVelocityCalculator;
import org.usfirst.frc.team6417.robot.service.powermanagement.PowerManagementStrategy;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class LiftingUnit extends Subsystem {
	private final double MOTOR_ROTATIONS_PER_CHAIN_WHEEL_ROTATION = calculateGearRatio();

	private final MotorController motorA, motorB;
	private final PowerManagementStrategy powerManagementStrategy;

	private boolean isCalibrated = false;
	
	private int currentPosition;
	private double targetPosition;
	private boolean isHoldingPosition = false;

	private DigitalInput endpointFrontSensor;
	
	private final MotionPathVelocityCalculator motionPathVelocityCalculator = new MotionPathVelocityCalculator(
			RobotMap.ROBOT.LIFTING_UNIT_SCALE_HIGH_ALTITUDE_IN_TICKS,
			RobotMap.VELOCITY.STOP_VELOCITY,
			RobotMap.ROBOT.LIFTING_UNIT_SCALE_HIGH_ALTITUDE_BREAK_IN_TICKS,
			RobotMap.VELOCITY.LIFTING_UNIT_MOTOR_UP_VELOCITY,
			RobotMap.ROBOT.LIFTING_UNIT_GROUND_ALTITUDE_BREAK_IN_TICKS,
			RobotMap.VELOCITY.LIFTING_UNIT_MOTOR_DOWN_VELOCITY,
			RobotMap.ROBOT.LIFTING_UNIT_GROUND_ALTITUDE_IN_TICKS,
			RobotMap.VELOCITY.STOP_VELOCITY
			);

	public LiftingUnit(PowerManagementStrategy powerManagementStrategy) {
		super(RobotMap.ROBOT.LIFTING_UNIT_NAME);
		
		this.powerManagementStrategy = powerManagementStrategy;
		
		final MotorControllerFactory factory = new MotorControllerFactory();
		motorA = factory.create777ProWithPositionControl(
				RobotMap.ROBOT.LIFTING_UNIT_MOTOR_A_NAME+"/"+RobotMap.MOTOR.LIFTING_UNIT_PORT_A, 
				RobotMap.MOTOR.LIFTING_UNIT_PORT_A);
//		motorA.setInverted(true); // TODO Remove when direction is correct
//		motorA.setSensorPhase(false); // TODO Remove when encoder-direction is correct
		motorA.setInverted(false);
		motorA.setSensorPhase(false);
		
		motorA.setNeutralMode(NeutralMode.Brake);
		motorA.configOpenloopRamp(0, MotorController.kTimeoutMs);
		motorA.configClosedloopRamp(0, MotorController.kTimeoutMs);
		
		motorA.configSelectedFeedbackSensor(
				FeedbackDevice.QuadEncoder, 
				MotorController.kPIDLoopIdx ,
				MotorController.kTimeoutMs );
		
//		motorA.config_kF(MotorController.kPIDLoopIdx, 0.0, MotorController.kTimeoutMs);
//		motorA.config_kP(MotorController.kPIDLoopIdx, 0.02, MotorController.kTimeoutMs);
//		motorA.config_kI(MotorController.kPIDLoopIdx, 0.0, MotorController.kTimeoutMs);
//		motorA.config_kD(MotorController.kPIDLoopIdx, 0.8, MotorController.kTimeoutMs);
		motorA.config_kF(MotorController.kPIDLoopIdx, 0.0, MotorController.kTimeoutMs);
		motorA.config_kP(MotorController.kPIDLoopIdx, 0.02, MotorController.kTimeoutMs);
		motorA.config_kI(MotorController.kPIDLoopIdx, 0.0, MotorController.kTimeoutMs);
		motorA.config_kD(MotorController.kPIDLoopIdx, 0.8, MotorController.kTimeoutMs);
		
		int allowedErrorPercentage = 10;
		int allowedErrorRelative = RobotMap.ENCODER.PULSE_PER_ROTATION_VERSA_PLANETARY / allowedErrorPercentage;
		motorA.configAllowableClosedloopError(0, allowedErrorRelative, MotorController.kTimeoutMs);
		
		motorB = factory.create775Pro(
				RobotMap.ROBOT.LIFTING_UNIT_MOTOR_B_NAME+"/"+RobotMap.MOTOR.LIFTING_UNIT_PORT_B, 
				RobotMap.MOTOR.LIFTING_UNIT_PORT_B);
		motorB.setNeutralMode(NeutralMode.Brake);
//		motorB.setInverted(true); // TODO Remove when direction is correct
		motorB.setInverted(false);
		motorB.follow(motorA);

		resetEncoder();
		isCalibrated = true;
		
		endpointFrontSensor = new DigitalInput(RobotMap.DIO.LIFTING_UNIT_POSITION_DOWN_PORT);
		endpointFrontSensor.setName(RobotMap.ROBOT.LIFTING_UNIT_POSITION_DOWN_SENSOR_NAME+"/"+RobotMap.DIO.LIFTING_UNIT_POSITION_DOWN_PORT);
	}
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new LiftingUnitTeleoperated());
	}

	public void moveToPos(double posRelative) {
		if(!isCalibrated) {
			return;
		}
		
		posRelative = Util.limit(posRelative, -1.0, 1.0);
		SmartDashboard.putNumber(RobotMap.ROBOT.LIFTING_UNIT_NAME+" pos rel", posRelative);
		double absolutePos = posRelative * RobotMap.ROBOT.LIFTING_UNIT_SCALE_HIGH_ALTITUDE_IN_TICKS;
		moveToAbsolutePos(absolutePos);
	}
	
	public void moveToAbsolutePos(double posAbsolute) {
		SmartDashboard.putNumber(RobotMap.ROBOT.LIFTING_UNIT_NAME+" pos abs", posAbsolute);

		 if(!isCalibrated) {
			System.out.println(RobotMap.ROBOT.LIFTING_UNIT_NAME+" not calibrated");
			return;
		}
		if(isHoldingPosition) {
//			System.out.println(RobotMap.ROBOT.LIFTING_UNIT_NAME+" already holding pos");
			return;
		}
		
		int currPos = getCurrentPosition();
		if(Util.eq((int)posAbsolute, currPos, RobotMap.ROBOT.LIFTING_UNIT_ALTITUDE_TOLERANCE)) {
			System.out.println("LU in tolerance. Using current pos. Not move. Pos: "+currPos+", Target: "+posAbsolute);
			targetPosition = currPos;
		}else {
			targetPosition = posAbsolute;
		}

//		System.out.println("LiftingUnit.moveToAbsolutePos("+targetPosition+")");
		setHoldPosition(true);
		motorA.set(ControlMode.Position, targetPosition);
//		motorA.set(ControlMode.MotionMagic, targetPosition);
		
	}

	public void move(double velocity) {
		if(!isCalibrated) {
			System.out.println("LiftingUnit not calibrated. Can not move with velocity "+velocity);
			return;
		}
		
		internalMove(velocity);
	}
	
	double calcVel(double vel, int pos) {
		if(vel < 0) {
			if(pos < RobotMap.ROBOT.LIFTING_UNIT_SCALE_HIGH_ALTITUDE_BREAK_IN_TICKS) {
				double m = (RobotMap.VELOCITY.LIFTING_UNIT_MOTOR_UP_VELOCITY - RobotMap.VELOCITY.STOP_VELOCITY) / (RobotMap.ROBOT.LIFTING_UNIT_SCALE_HIGH_ALTITUDE_BREAK_IN_TICKS - RobotMap.ROBOT.LIFTING_UNIT_SCALE_HIGH_ALTITUDE_IN_TICKS);
				double q = RobotMap.VELOCITY.STOP_VELOCITY - m * RobotMap.ROBOT.LIFTING_UNIT_SCALE_HIGH_ALTITUDE_IN_TICKS;
				double v = m * pos + q;
				if(vel > v) {
					return vel;
				}
				return v;
			}
		} else if(vel > 0) {
			if(pos > RobotMap.ROBOT.LIFTING_UNIT_GROUND_ALTITUDE_BREAK_IN_TICKS) {
				double m = (RobotMap.VELOCITY.STOP_VELOCITY - RobotMap.VELOCITY.LIFTING_UNIT_MOTOR_DOWN_VELOCITY) / (RobotMap.ROBOT.LIFTING_UNIT_GROUND_ALTITUDE_IN_TICKS - RobotMap.ROBOT.LIFTING_UNIT_GROUND_ALTITUDE_BREAK_IN_TICKS);
				double q = RobotMap.VELOCITY.STOP_VELOCITY - m * RobotMap.ROBOT.LIFTING_UNIT_GROUND_ALTITUDE_IN_TICKS;
				double v = m * pos + q;
				if(vel < v) {
					return vel;
				}
				return v;
			}
		}
		return vel;
	}
	
	private void internalMove(double velocity) {
//		System.out.println("LiftingUnit.internalMove("+velocity+")");
		velocity = Util.limit(velocity, RobotMap.VELOCITY.LIFTING_UNIT_MOTOR_UP_VELOCITY, RobotMap.VELOCITY.LIFTING_UNIT_MOTOR_DOWN_VELOCITY);
		SmartDashboard.putNumber(RobotMap.ROBOT.LIFTING_UNIT_NAME+" vel given", velocity);

		double velocity2 = motionPathVelocityCalculator.calculateVelocity(velocity, getCurrentPosition());
		double calcVel = calcVel(velocity, getCurrentPosition());
		
//		velocity = this.powerManagementStrategy.calculatePower() * velocity;
		SmartDashboard.putNumber(RobotMap.ROBOT.LIFTING_UNIT_NAME+" vel bound", velocity2);
		SmartDashboard.putNumber(RobotMap.ROBOT.LIFTING_UNIT_NAME+" vel bound x", calcVel);
		
		SmartDashboard.putBoolean(endpointFrontSensor.getName(), !endpointFrontSensor.get());
		SmartDashboard.putNumber(RobotMap.ROBOT.LIFTING_UNIT_NAME+" pos", getCurrentPosition());
		
		if(velocity < 0) {
			if(isInEndpointTop()) {
				motorA.set(RobotMap.VELOCITY.STOP_VELOCITY);
//				moveToAbsolutePos(RobotMap.ROBOT.LIFTING_UNIT_SCALE_HIGH_ALTITUDE_IN_TICKS);
				holdPosition();
//			} else if(Util.inRange(getCurrentPosition(), RobotMap.ROBOT.LIFTING_UNIT_SCALE_HIGH_ALTITUDE_BREAK_IN_TICKS, RobotMap.ROBOT.LIFTING_UNIT_SCALE_HIGH_ALTITUDE_IN_TICKS)) {
//				setHoldPosition(false);
//				motorA.set(ControlMode.PercentOutput, velocity);
			}else {
				setHoldPosition(false);
				motorA.set(ControlMode.PercentOutput, calcVel);
			}
		}else if(velocity > 0) {
			if(isInEndpointBottom()) {
				motorA.set(RobotMap.VELOCITY.STOP_VELOCITY);
				holdPosition();
//			} else if (Util.inRange(getCurrentPosition(), RobotMap.ROBOT.LIFTING_UNIT_GROUND_ALTITUDE_IN_TICKS, RobotMap.ROBOT.LIFTING_UNIT_GROUND_ALTITUDE_BREAK_IN_TICKS)) {
//				setHoldPosition(false);
//				motorA.set(ControlMode.PercentOutput, velocity);
			}else {
				setHoldPosition(false);
				motorA.set(ControlMode.PercentOutput, calcVel);
			}
		}else {
			//holdPosition();
			setHoldPosition(false);
			motorA.set(RobotMap.VELOCITY.STOP_VELOCITY);
		}
	}

	public void resetEncoder() {
		// Reset the encoder to 0. 
		// Only motorA has an encoder so only motorA gets the reset:
		motorA.setSelectedSensorPosition(RobotMap.ENCODER.INITIAL_VALUE, MotorController.kPIDLoopIdx, MotorController.kTimeoutMs);
		SmartDashboard.putNumber(motorA.getName()+" curr pos", motorA.getSelectedSensorPosition(0));
	}
		
	private int getCurrentPosition() {
		return motorA.getSelectedSensorPosition(MotorController.kPIDLoopIdx);
	}
	
	private boolean isInEndpointTop() {
		final boolean isInEndpoint = Util.smallerThen(getCurrentPosition(),
				RobotMap.ROBOT.LIFTING_UNIT_SCALE_HIGH_ALTITUDE_IN_TICKS,
				RobotMap.ENCODER.PULSE_VERSA_PLANETARY_EPSILON);
		SmartDashboard.putBoolean(motorA.getName()+" up", isInEndpoint);
		return isInEndpoint;
	}

	public boolean isInEndpointBottom() {
//		final boolean isInEndpoint = Util.eq(getCurrentPosition(), 
//				RobotMap.ROBOT.LIFTING_UNIT_GROUND_ALTITUDE_IN_TICKS, 
//				RobotMap.ENCODER.PULSE_VERSA_PLANETARY_EPSILON);
//		if(getCurrentPosition() > RobotMap.ROBOT.LIFTING_UNIT_GROUND_ALTITUDE_IN_TICKS + RobotMap.ENCODER.PULSE_VERSA_PLANETARY_EPSILON) {
//			SmartDashboard.putBoolean(motorA.getName()+" down", false);
//			return false;
//		}
//		if(getCurrentPosition() >= RobotMap.ROBOT.LIFTING_UNIT_GROUND_ALTITUDE_IN_TICKS) {
//			SmartDashboard.putBoolean(motorA.getName()+" down", true);
//			return true;
//		}
//		SmartDashboard.putBoolean(motorA.getName()+" down", false);
//		return false;
		SmartDashboard.putBoolean(motorA.getName()+" down", (!endpointFrontSensor.get()));
		return !endpointFrontSensor.get();
	}
	
	public boolean isInEndpoint() {
		return isInEndpointBottom() || isInEndpointTop();
	}

	public void holdPosition() {
		currentPosition = getCurrentPosition();
		moveToAbsolutePos(currentPosition);
//		motorA.set(0);
	}
	
	public void setHoldPosition(boolean isPosCtrl) {
		isHoldingPosition = isPosCtrl;
		SmartDashboard.putBoolean(RobotMap.ROBOT.LIFTING_UNIT_NAME+" pos ctrl", isHoldingPosition);
		SmartDashboard.putBoolean(RobotMap.ROBOT.LIFTING_UNIT_NAME+" vel ctrl", !isHoldingPosition);
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
		if(!isCalibrated) {
			return;
		}
		
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

	public void startMoveToEndpointDown() {
		if(isInEndpointBottom()) {
			System.out.println(getName()+" startMoveToEndpointDown is in endpoint");
			return;
		}
		
		System.out.println(getName()+" startMoveToEndpointDown with v="+RobotMap.VELOCITY.LIFTING_UNIT_MOTOR_VERY_SLOW_DOWN_VELOCITY+" ...");
		motorA.set(RobotMap.VELOCITY.LIFTING_UNIT_MOTOR_VERY_SLOW_DOWN_VELOCITY);
	}
	
	public void stopMoveToEndpointDown() {
		motorA.set(RobotMap.VELOCITY.STOP_VELOCITY);
		resetEncoder();
		System.out.println(getName()+" startMoveToEndpointDown DONE");
		isCalibrated = true;
		System.out.println(getName()+" startMoveToEndpointDown RESET");
		//moveToAbsolutePos(RobotMap.ROBOT.LIFTING_UNIT_GROUND_ALTITUDE_IN_TICKS);
		System.out.println(getName()+" startMoveToEndpointDown HOLD POS");
	}
	
	public boolean isOnTarget() {
		System.out.println("LiftingUnit.isOnTarget: curr:"+(getCurrentPosition()+", target: "+(int)targetPosition+", tol: "+5*RobotMap.ROBOT.LIFTING_UNIT_ALTITUDE_TOLERANCE));
		if(Util.eq(getCurrentPosition(), (int)targetPosition, 5*RobotMap.ROBOT.LIFTING_UNIT_ALTITUDE_TOLERANCE)) {
			return true;
		}
		return false;
	}

	public boolean isAboveSafetyAltitude() {
//		System.out.println("LiftingUnit.isAboveSafetyAltitude Pos: "+getCurrentPosition()+", Target: "+RobotMap.ROBOT.LIFTING_UNIT_SAFETY_ALTITUDE_IN_TICKS+", eps: "+RobotMap.ENCODER.PULSE_VERSA_PLANETARY_EPSILON+" -> "+((getCurrentPosition() <= RobotMap.ROBOT.LIFTING_UNIT_SAFETY_ALTITUDE_IN_TICKS + RobotMap.ENCODER.PULSE_VERSA_PLANETARY_EPSILON)));
//		return Util.smallerThen(getCurrentPosition(), 
//								RobotMap.ROBOT.LIFTING_UNIT_SAFETY_ALTITUDE_IN_TICKS, 
//								RobotMap.ENCODER.PULSE_VERSA_PLANETARY_EPSILON);
		return (getCurrentPosition() <= RobotMap.ROBOT.LIFTING_UNIT_SAFETY_ALTITUDE_IN_TICKS + RobotMap.ENCODER.PULSE_VERSA_PLANETARY_EPSILON);
	}

	public void moveToAboveSafetyAltitude() {
		if(isAboveSafetyAltitude()) {
			System.out.println(getName()+" is above safety-altitude");
			return;
		}
		
		setHoldPosition(false);
		
//		if(isInEndpointBottom()) {
//			return;
//		}
//		
//		System.out.println(getName()+" moveToAboveSafetyAltitude from pos "+getCurrentPosition()+" to pos "+RobotMap.ROBOT.LIFTING_UNIT_SAFETY_ALTITUDE_IN_TICKS+" ...");
//		moveToAbsolutePos(RobotMap.ROBOT.LIFTING_UNIT_SAFETY_ALTITUDE_IN_TICKS);
		move(RobotMap.VELOCITY.LIFTING_UNIT_MOTOR_UP_VELOCITY / 8.0);
	}
	
	class MoveToSafetyAltitude extends State {
		private boolean isAboveSafetyAltitude = false;
		
		@Override
		public void init() {
			final int currPos = getCurrentPosition();
			if(currPos <= RobotMap.ROBOT.LIFTING_UNIT_SAFETY_ALTITUDE_IN_TICKS) {
				isAboveSafetyAltitude = true;
			}else if(currPos > RobotMap.ROBOT.LIFTING_UNIT_SAFETY_ALTITUDE_IN_TICKS) {
				move(RobotMap.VELOCITY.LIFTING_UNIT_MOTOR_SLOW_UP_VELOCITY);
			}			
		}
		
		@Override
		public boolean isFinished() {
			return isAboveSafetyAltitude || isAboveSafetyAltitude();
		}
		
	}
	
	class HoldPosition extends State {
		
		@Override
		public void init() {
			holdPosition();
		}
		
		@Override
		public boolean isFinished() {
			return false;
		}
		
	}
	
	class MoveTeleoperated extends State {
		@Override
		public void tick() {
			move(-OI.getInstance().liftingUnitController.getY());
		}
	}
	
	class Calibrate extends State {
		private boolean isCalibrated = false;
		
		@Override
		public void init() {
			if(isInEndpointBottom()) {
				isCalibrated = true;
			}

			internalMove(RobotMap.VELOCITY.LIFTING_UNIT_MOTOR_VERY_SLOW_DOWN_VELOCITY);
		}
		
		@Override
		public boolean isFinished() {
			if(isInEndpointBottom()) {
				stopMoveToEndpointDown();
			}
			return isCalibrated;
		}
	}

}
