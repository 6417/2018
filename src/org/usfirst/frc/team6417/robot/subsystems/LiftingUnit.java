package org.usfirst.frc.team6417.robot.subsystems;

import java.util.Arrays;

import org.usfirst.frc.team6417.robot.Fridolin;
import org.usfirst.frc.team6417.robot.RobotMap;
import org.usfirst.frc.team6417.robot.model.Event;
import org.usfirst.frc.team6417.robot.model.State;
import org.usfirst.frc.team6417.robot.service.powermanagement.PowerManagementStrategy;

import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class LiftingUnit extends PIDSubsystem {
	public static final Event TO_GROUND = new Event("TO_GROUND");
	public static final Event TO_SWITCH = new Event("TO_SWITCH");
	public static final Event TO_EXCHANGE = new Event("TO_EXCHANGE");
	public static final Event TO_SCALE_LOW = new Event("TO_SCALE_LOW");
	public static final Event TO_SCALE_MIDDLE = new Event("TO_SCALE_MIDDLE");
	public static final Event TO_SCALE_HIGH = new Event("TO_SCALE_HIGH");

	private static final double kP = 0.2;
	private static final double kI = 0.0;
	private static final double kD = 0.0;
	
	private final Fridolin motorA, motorB;
	
	private State currentState;
	private final PowerManagementStrategy powerManagementStrategy;
	
	public LiftingUnit(PowerManagementStrategy powerManagementStrategy) {
		super("LiftingUnit", kP, kI, kD);
		
		this.powerManagementStrategy = powerManagementStrategy;
		
		setAbsoluteTolerance(5);
		setOutputRange(-1.0, 1.0);
		getPIDController().setContinuous(false);
		getPIDController().setName("LiftingUnit-Controller");
		
		motorA = new Fridolin("Motor-A", RobotMap.MOTOR.LIFTING_UNIT_PORT_A);
		motorB = new Fridolin("Motor-B", RobotMap.MOTOR.LIFTING_UNIT_PORT_B);
//		motorB.follow(motorA);

		State ground = currentState = new Ground();
		State theSwitch = new Switch();
		State scaleLow = new Switch();
		State scaleMiddle = new Switch();
		State scaleHigh = new Switch();

		Arrays.asList(ground,theSwitch,scaleLow,scaleHigh).stream().forEach( state -> {
			state.addTransition(TO_GROUND, ground)
			     .addTransition(TO_SWITCH, theSwitch)
			     .addTransition(TO_EXCHANGE, theSwitch)
			     .addTransition(TO_SCALE_LOW, scaleLow)
			     .addTransition(TO_SCALE_MIDDLE, scaleMiddle)
			     .addTransition(TO_SCALE_HIGH, scaleHigh);
		} );
		
		getPIDController().enable();
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
		// Only motorA has an encoder so only motorA gets the reset:
		motorB.set(com.ctre.phoenix.motorcontrol.ControlMode.Position, 0);
	}
	
	@Override
	protected void initDefaultCommand() {;}

	@Override
	protected double returnPIDInput() {
		int position = motorB.getSensorCollection().getPulseWidthPosition();
		SmartDashboard.putNumber("LiftingUnit position", position);		
		return position;
	}

	@Override
	protected void usePIDOutput(double output) {
		SmartDashboard.putNumber("LiftingUnit PID output", output);
		// Motor A is master. Motor B is follower and will reflect the Motor A behavior.
		// Only Motor A must be changed. B will be changed automatically from A.
		motorA.pidWrite(output);
		motorB.pidWrite(output);
	}
	
	abstract class SetpointState extends State {
		protected void init(long ticks) {
			SmartDashboard.putNumber("LiftingUnit Setpoint", ticks);
			setSetpoint(ticks);
		}
		
	}
	class Ground extends SetpointState {
		@Override
		public void init() {
			init(RobotMap.ROBOT.LIFTING_UNIT_GROUND_ALTITUDE_IN_TICKS);
		}
	}
	class Switch extends SetpointState {
		@Override
		public void init() {
			init(RobotMap.ROBOT.LIFTING_UNIT_SWITCH_ALTITUDE_IN_TICKS);
		}
	}
	class ScaleLow extends SetpointState {
		@Override
		public void init() {
			init(RobotMap.ROBOT.LIFTING_UNIT_SCALE_LOW_ALTITUDE_IN_TICKS);
		}
	}
	class ScaleMiddle extends SetpointState {
		@Override
		public void init() {
			init(RobotMap.ROBOT.LIFTING_UNIT_SCALE_MIDDLE_ALTITUDE_IN_TICKS);
		}
	}
	class ScaleHigh extends SetpointState {
		@Override
		public void init() {
			init(RobotMap.ROBOT.LIFTING_UNIT_SCALE_HIGH_ALTITUDE_IN_TICKS);
		}
	}

}
