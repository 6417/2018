package org.usfirst.frc.team6417.robot.subsystems;

import java.util.Arrays;

import org.usfirst.frc.team6417.robot.MotorController;
import org.usfirst.frc.team6417.robot.MotorControllerFactory;
import org.usfirst.frc.team6417.robot.RobotMap;
import org.usfirst.frc.team6417.robot.model.Event;
import org.usfirst.frc.team6417.robot.model.State;
import org.usfirst.frc.team6417.robot.service.powermanagement.PowerManagementStrategy;

import com.ctre.phoenix.motorcontrol.ControlMode;

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
		motorA = factory.create775Pro("Motor-A-Slave", RobotMap.MOTOR.LIFTING_UNIT_PORT_A);
		motorB = factory.create777ProWithPositionControl("Motor-B-Master", RobotMap.MOTOR.LIFTING_UNIT_PORT_B);
		motorA.follow(motorB);

		// TODO nils: Move these 3 lines to the factory after testing
		int allowedErrorPercentage = 10;
//		Use this: 
		allowedErrorRelative = RobotMap.ENCODER.PULSE_PER_ROTATION / allowedErrorPercentage;
		motorB.configAllowableClosedloopError(MotorController.kSlotIdx, allowedErrorRelative, MotorController.kTimeoutMs);

		State ground = currentState = new PositionState(RobotMap.ROBOT.LIFTING_UNIT_GROUND_ALTITUDE_IN_TICKS);
		State theSwitch = new PositionState(RobotMap.ROBOT.LIFTING_UNIT_SWITCH_ALTITUDE_IN_TICKS);
		State exchange = new PositionState(RobotMap.ROBOT.LIFTING_UNIT_EXCHANGE_LOW_ALTITUDE_IN_TICKS);
		State scaleLow = new PositionState(RobotMap.ROBOT.LIFTING_UNIT_SCALE_LOW_ALTITUDE_IN_TICKS);
		State scaleMiddle = new PositionState(RobotMap.ROBOT.LIFTING_UNIT_SCALE_MIDDLE_ALTITUDE_IN_TICKS);
		State scaleHigh = new PositionState(RobotMap.ROBOT.LIFTING_UNIT_SCALE_HIGH_ALTITUDE_IN_TICKS);

		State metricPosition = new MetricPositionState(0.3/*meter*/);

		Arrays.asList(ground,theSwitch,exchange,scaleLow,scaleMiddle,scaleHigh,metricPosition).stream().forEach( state -> {
			state.addTransition(TO_GROUND, ground)
			     .addTransition(TO_SWITCH, theSwitch)
			     .addTransition(TO_EXCHANGE, exchange)
			     .addTransition(TO_SCALE_LOW, scaleLow)
			     .addTransition(TO_SCALE_MIDDLE, scaleMiddle)
			     .addTransition(TO_SCALE_HIGH, scaleHigh)
			     .addTransition(TO_POSITION, metricPosition);
		} );
		
	}

	private double calculateGearRatio() {
		// See documentation here:
		// https://docs.google.com/document/d/1cqJp1bNhLM5h0Qkk95_VV4HTZDqJ9aykWqInW393COQ/edit?usp=sharing
		double b = 37;
		double c = 13;
		double d = 37;
		double e = 12;
		double f = 7;
		
		return ((b/c)*(d/e)*f);
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
		double chainWheelRadiusInMeter = 0.05707;//m
		return 2.0 * RobotMap.MATH.PI * chainWheelRadiusInMeter;
	}

	public void onEvent(Event event) {
		SmartDashboard.putString("LiftingUnit event", event.toString());		
		SmartDashboard.putString("LiftingUnit state (prev)", currentState.getClass().getSimpleName());		
		currentState = currentState.transition(event);
		currentState.init();
		SmartDashboard.putString("LiftingUnit state (current)", currentState.getClass().getSimpleName());		
	}
	
	public void reset() {
		// Reset the encoder to 0. 
		// Only motorB has an encoder so only motorB gets the reset:
		motorB.setSelectedSensorPosition(0, MotorController.kPIDLoopIdx, MotorController.kTimeoutMs);
	}
	
	public boolean onTarget() {
		return allowedErrorRelative <= motorB.getClosedLoopError(MotorController.kPIDLoopIdx);
	}
	
	@Override
	protected void initDefaultCommand() {;}
	
	private final class PositionState extends State {
		private final long position;
		
		PositionState(long position){
			this.position = position;
		}
		
		@Override
		public void init() {
			SmartDashboard.putNumber("LiftingUnit nominal position", position);
			motorB.set(ControlMode.Position, position);
		}
		
		@Override
		public void tick() {
			SmartDashboard.putNumber("LiftingUnit actual position", motorB.getSelectedSensorPosition(MotorController.kPIDLoopIdx));
			motorB.set(ControlMode.Position, position);
		}
	}
	

	private final class MetricPositionState extends State {
		private final double positionInMeter;
		
		MetricPositionState(double positionInMeter){
			this.positionInMeter = positionInMeter;
		}
		
		@Override
		public void init() {
			SmartDashboard.putNumber("LiftingUnit nominal metric position", positionInMeter);
			liftToPosition(positionInMeter);
		}
		
		@Override
		public void tick() {
			SmartDashboard.putNumber("LiftingUnit actual position", motorB.getSelectedSensorPosition(MotorController.kPIDLoopIdx));
		}
		
		@Override
		public boolean isFinished() {
			return onTarget();
		}
	}
	

	
}
