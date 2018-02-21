package org.usfirst.frc.team6417.robot.subsystems;

import java.util.Arrays;

import org.usfirst.frc.team6417.robot.MotorController;
import org.usfirst.frc.team6417.robot.MotorControllerFactory;
import org.usfirst.frc.team6417.robot.RobotMap;
import org.usfirst.frc.team6417.robot.Util;
import org.usfirst.frc.team6417.robot.model.Event;
import org.usfirst.frc.team6417.robot.model.State;
import org.usfirst.frc.team6417.robot.service.powermanagement.PowerManagementStrategy;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class LiftingUnit extends Subsystem {
	public static final Event TO_GROUND = new Event("TO_GROUND");
	public static final Event TO_SWITCH = new Event("TO_SWITCH");
	public static final Event TO_EXCHANGE = new Event("TO_EXCHANGE");
	public static final Event TO_SCALE_LOW = new Event("TO_SCALE_LOW");
	public static final Event TO_SCALE_MIDDLE = new Event("TO_SCALE_MIDDLE");
	public static final Event TO_SCALE_HIGH = new Event("TO_SCALE_HIGH");
	public static final Event TO_POSITION = new Event("TO_POSITION");

	private final double MOTOR_ROTATIONS_PER_CHAIN_WHEEL_ROTATION = calculateGearRatio();

	private final MotorController motorA, motorB;
	private State currentState;
	private final PowerManagementStrategy powerManagementStrategy;
	private int allowedErrorRelative;
	
	public LiftingUnit(PowerManagementStrategy powerManagementStrategy) {
		super("LiftingUnit");
		
		this.powerManagementStrategy = powerManagementStrategy;
		
		final MotorControllerFactory factory = new MotorControllerFactory();
		motorA = factory.create777ProWithPositionControl("Motor-A-Master", RobotMap.MOTOR.LIFTING_UNIT_PORT_A);
		motorB = factory.create775Pro("Motor-B-Slave", RobotMap.MOTOR.LIFTING_UNIT_PORT_B);
		motorB.follow(motorA);

//		motorA.configOpenloopRamp(1, 0);
		motorA.configOpenloopRamp(1, 0);
		
		// TODO nils: Move these 3 lines to the factory after testing
		int allowedErrorPercentage = 10;
//		Use this: 
		allowedErrorRelative = RobotMap.ENCODER.PULSE_PER_ROTATION / allowedErrorPercentage;
//		motorA.configAllowableClosedloopError(MotorController.kSlotIdx, allowedErrorRelative, MotorController.kTimeoutMs);

		State ground = currentState = new TargetPositionState(RobotMap.ROBOT.LIFTING_UNIT_GROUND_ALTITUDE_IN_TICKS);
		State theSwitch = new TargetPositionState(RobotMap.ROBOT.LIFTING_UNIT_SWITCH_ALTITUDE_IN_TICKS);
		State exchange = new TargetPositionState(RobotMap.ROBOT.LIFTING_UNIT_EXCHANGE_LOW_ALTITUDE_IN_TICKS);
		State scaleLow = new TargetPositionState(RobotMap.ROBOT.LIFTING_UNIT_SCALE_LOW_ALTITUDE_IN_TICKS);
		State scaleMiddle = new TargetPositionState(RobotMap.ROBOT.LIFTING_UNIT_SCALE_MIDDLE_ALTITUDE_IN_TICKS);
		State scaleHigh = new TargetPositionState(RobotMap.ROBOT.LIFTING_UNIT_SCALE_HIGH_ALTITUDE_IN_TICKS);

//		State metricPosition = new MetricPositionState(0.3/*meter*/);

		Arrays.asList(ground,theSwitch,exchange,scaleLow,scaleMiddle,scaleHigh /* ,metricPosition */).stream().forEach( state -> {
			state.addTransition(TO_GROUND, ground)
			     .addTransition(TO_SWITCH, theSwitch)
			     .addTransition(TO_EXCHANGE, exchange)
			     .addTransition(TO_SCALE_LOW, scaleLow)
			     .addTransition(TO_SCALE_MIDDLE, scaleMiddle)
			     .addTransition(TO_SCALE_HIGH, scaleHigh)
			     /*.addTransition(TO_POSITION, metricPosition)*/;
		} );
		
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
		
		SmartDashboard.putNumber("LiftingUnit lift to pos in m", positionInMeters);
		SmartDashboard.putNumber("LiftingUnit chain-wheel rotations", chainWheelRotations);
		SmartDashboard.putNumber("LiftingUnit motor rotations", motorRotations);
		SmartDashboard.putNumber("LiftingUnit ticks to target", ticksToTarget);
		SmartDashboard.putNumber("LiftingUnit error", error);
		
//		motorB.set(ControlMode.MotionMagic, ticksToTarget);
	}

	private double calculateMotorRotations(double chainWheelRotations) {
		return MOTOR_ROTATIONS_PER_CHAIN_WHEEL_ROTATION * chainWheelRotations;
	}

	private double calculateChainWheelRotations(double positionInMeters) {
		double chainWheelRadiusInMeter = RobotMap.ROBOT.LIFTING_UNIT_CHAIN_WHEEL_RADIUS_IN_METER;//m
		return 2.0 * RobotMap.MATH.PI * chainWheelRadiusInMeter;
	}

	public void onEvent(Event event) {
		SmartDashboard.putString("LiftingUnit event", event.toString());		
		SmartDashboard.putString("LiftingUnit state (prev)", currentState.toString());		
		currentState = currentState.transition(event);
		currentState.init();
		SmartDashboard.putString("LiftingUnit state (current)", currentState.toString());		
	}
	
	public void reset() {
		// Reset the encoder to 0. 
		// Only motorB has an encoder so only motorB gets the reset:
		motorA.setSelectedSensorPosition(0, MotorController.kPIDLoopIdx, MotorController.kTimeoutMs);
	}
	
	public void tick() {
		currentState.tick();
	}
	
	public boolean onTarget() {
//		return allowedErrorRelative <= motorB.getClosedLoopError(MotorController.kPIDLoopIdx);
		return currentState.isFinished();
	}
	
	@Override
	protected void initDefaultCommand() {;}
	
	private int getCurrentPosition() {
		return motorA.getSelectedSensorPosition(MotorController.kPIDLoopIdx);
	}
	
	private final class TargetPositionState extends State {
		private final int targetPosition;
		
		TargetPositionState(int targetPosition){
			this.targetPosition = targetPosition;
		}
		
		@Override
		public void init() {
			SmartDashboard.putNumber("LiftingUnit nominal position", targetPosition);
			SmartDashboard.putNumber("LiftingUnit actual position", getCurrentPosition());
//			motorB.set(ControlMode.Position, position);
			int currPos = getCurrentPosition();
			if(Util.greaterThen(currPos, targetPosition)) {
				motorA.set(RobotMap.VELOCITY.LIFTING_UNIT_MOTOR_UP_VELOCITY);
				SmartDashboard.putNumber("LiftingUnit motor", RobotMap.VELOCITY.LIFTING_UNIT_MOTOR_UP_VELOCITY);
			}else if(Util.smallerThen(currPos, targetPosition)) {
				motorA.set(RobotMap.VELOCITY.LIFTING_UNIT_MOTOR_DOWN_VELOCITY);
				SmartDashboard.putNumber("LiftingUnit motor", RobotMap.VELOCITY.LIFTING_UNIT_MOTOR_DOWN_VELOCITY);
			}else {
				motorA.set(RobotMap.VELOCITY.STOP_VELOCITY);
				System.out.println("LiftingUnit "+currPos+" to "+targetPosition);
			}
		}
		
		@Override
		public void tick() {
			SmartDashboard.putNumber("LiftingUnit actual position", getCurrentPosition());
//			motorB.set(ControlMode.Position, position);
		}
		
		@Override
		public boolean isFinished() {
//			return Util.eq(targetPosition, getCurrentPosition());
			boolean isFinished = Util.eq(getCurrentPosition(), targetPosition);
			if(isFinished) {
				motorA.set(RobotMap.VELOCITY.STOP_VELOCITY);
			}
			return isFinished;
		}
		
		public String toString() {
			return "state("+targetPosition+")";
		}
	}
	

//	private final class MetricPositionState extends State {
//		private final double positionInMeter;
//		
//		MetricPositionState(double positionInMeter){
//			this.positionInMeter = positionInMeter;
//		}
//		
//		@Override
//		public void init() {
//			SmartDashboard.putNumber("LiftingUnit nominal metric position", positionInMeter);
//			liftToPosition(positionInMeter);
//		}
//		
//		@Override
//		public void tick() {
//			SmartDashboard.putNumber("LiftingUnit actual position", motorB.getSelectedSensorPosition(MotorController.kPIDLoopIdx));
//		}
//		
//		@Override
//		public boolean isFinished() {
//			return onTarget();
//		}
//	}
	

	
}
