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

//	private static final double kP = 0.2;
//	private static final double kI = 0.1;
//	private static final double kD = 0.1;
	
	private final MotorController motorA, motorB;
	
	private State currentState;
	private final PowerManagementStrategy powerManagementStrategy;
	
	public LiftingUnit(PowerManagementStrategy powerManagementStrategy) {
		super("LiftingUnit");
		
		this.powerManagementStrategy = powerManagementStrategy;
		
//		setAbsoluteTolerance(5);
//		setPercentTolerance(20);
//		setOutputRange(-1.0, 1.0);
		
//		getPIDController().setContinuous(false);
//		getPIDController().setName("LiftingUnit-Controller");
		
		final MotorControllerFactory factory = new MotorControllerFactory();
		motorA = factory.create775Pro("Motor-A", RobotMap.MOTOR.LIFTING_UNIT_PORT_A);
		motorB = factory.create775ProWithEncoder("Motor-B", RobotMap.MOTOR.LIFTING_UNIT_PORT_B);
		motorA.follow(motorB);

		State ground = currentState = new Ground();
		State theSwitch = new Switch();
		State exchange = new Exchange();
		State scaleLow = new Switch();
		State scaleMiddle = new Switch();
		State scaleHigh = new Switch();

		Arrays.asList(ground,theSwitch,scaleLow,scaleHigh).stream().forEach( state -> {
			state.addTransition(TO_GROUND, ground)
			     .addTransition(TO_SWITCH, theSwitch)
			     .addTransition(TO_EXCHANGE, exchange)
			     .addTransition(TO_SCALE_LOW, scaleLow)
			     .addTransition(TO_SCALE_MIDDLE, scaleMiddle)
			     .addTransition(TO_SCALE_HIGH, scaleHigh);
		} );
		
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
		motorB.set(com.ctre.phoenix.motorcontrol.ControlMode.Position, 0);
	}
	
	@Override
	protected void initDefaultCommand() {;}

//	@Override
//	protected double returnPIDInput() {
////		int position = motorB.getSensorCollection().getQuadraturePosition();
//		int position = motorB.getSelectedSensorPosition(0);
//		SmartDashboard.putNumber("LiftingUnit position", position);		
//		return position;
//	}
//
//	@Override
//	protected void usePIDOutput(double output) {
//		SmartDashboard.putNumber("LiftingUnit PID output", output);
//		// Motor B is master. Motor A is follower and will reflect the Motor B behavior.
//		// Only Motor B must be changed. A will be changed automatically from B.
////		motorA.pidWrite(output);
//		motorB.pidWrite(output);
//	}
	
	
	abstract class SetpointState extends State {
		protected void init(long ticks) {
			SmartDashboard.putNumber("LiftingUnit Setpoint", ticks);
			motorB.set(ControlMode.Position, ticks);
//			
//			if(!getPIDController().isEnabled()) {
//				getPIDController().enable();
//			}
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
	class Exchange extends SetpointState {
		@Override
		public void init() {
			init(RobotMap.ROBOT.LIFTING_UNIT_EXCHANGE_LOW_ALTITUDE_IN_TICKS);
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
