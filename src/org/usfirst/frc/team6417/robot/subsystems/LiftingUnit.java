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

		// TODO nils: Move these 3 lines to the factory after testing
		int allowedErrorPercentage = 10;
		allowedErrorRelative = RobotMap.ENCODER.QUADRATURE_UNITS_PER_ROTATION / allowedErrorPercentage;
		motorB.configAllowableClosedloopError(MotorController.kSlotIdx, allowedErrorRelative, MotorController.kTimeoutMs);

		motorA.follow(motorB);

		State ground = currentState = new Ground();
		State theSwitch = new Switch();
		State exchange = new Exchange();
		State scaleLow = new Switch();
		State scaleMiddle = new Switch();
		State scaleHigh = new Switch();

		Arrays.asList(ground,theSwitch,exchange,scaleLow,scaleMiddle,scaleHigh).stream().forEach( state -> {
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
		motorB.setSelectedSensorPosition(0, MotorController.kPIDLoopIdx, MotorController.kTimeoutMs);
	}
	
	public boolean onTarget() {
		return allowedErrorRelative <= motorB.getClosedLoopError(MotorController.kPIDLoopIdx);
	}
	
	@Override
	protected void initDefaultCommand() {;}
	
	abstract class SetpointState extends State {
		protected void init(long setpoint) {
			SmartDashboard.putNumber("LiftingUnit Setpoint", setpoint);
			motorB.set(ControlMode.MotionMagic, setpoint);
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
