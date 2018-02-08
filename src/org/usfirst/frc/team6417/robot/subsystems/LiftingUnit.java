package org.usfirst.frc.team6417.robot.subsystems;

import java.util.Arrays;

import org.usfirst.frc.team6417.robot.Fridolin;
import org.usfirst.frc.team6417.robot.RobotMap;
import org.usfirst.frc.team6417.robot.model.Event;
import org.usfirst.frc.team6417.robot.model.State;
import org.usfirst.frc.team6417.robot.service.powermanagement.PowerManagementStrategy;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

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
	
	private final Fridolin motorA, motorB;
	
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
		
		motorA = new Fridolin("Motor-A", RobotMap.MOTOR.LIFTING_UNIT_PORT_A);
		motorB = new Fridolin("Motor-B", RobotMap.MOTOR.LIFTING_UNIT_PORT_B);
		
		configure(motorA);
		configure(motorB);
		configureForClosedLoop(motorB);
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
	
	private void configureForClosedLoop(Fridolin motor) {
		/* choose the sensor and sensor direction */
		motor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 
				Fridolin.kPIDLoopIdx,
				Fridolin.kTimeoutMs);

		/*
		 * set the allowable closed-loop error, Closed-Loop output will be
		 * neutral within this range. See Table in Section 17.2.1 for native
		 * units per rotation.
		 */
		motor.configAllowableClosedloopError(0, Fridolin.kPIDLoopIdx, Fridolin.kTimeoutMs);

		/* set closed loop gains in slot0, typically kF stays zero. */
		motor.config_kF(Fridolin.kPIDLoopIdx, 0.0, Fridolin.kTimeoutMs);
		motor.config_kP(Fridolin.kPIDLoopIdx, 0.1, Fridolin.kTimeoutMs);
		motor.config_kI(Fridolin.kPIDLoopIdx, 0.0, Fridolin.kTimeoutMs);
		motor.config_kD(Fridolin.kPIDLoopIdx, 0.0, Fridolin.kTimeoutMs);
		
		/*
		 * lets grab the 360 degree position of the MagEncoder's absolute
		 * position, and intitally set the relative sensor to match.
		 */
		int absolutePosition = motor.getSelectedSensorPosition(Fridolin.kPIDLoopIdx);
		/* mask out overflows, keep bottom 12 bits */
		absolutePosition &= 0xFFF;
		if (Fridolin.kSensorPhase)
			absolutePosition *= -1;
		if (Fridolin.kMotorInvert)
			absolutePosition *= -1;
		/* set the quadrature (relative) sensor to match absolute */
		motor.setSelectedSensorPosition(absolutePosition, Fridolin.kPIDLoopIdx, Fridolin.kTimeoutMs);

	}

	private void configure(Fridolin motor) {
		/* choose to ensure sensor is positive when output is positive */
		motor.setSensorPhase(Fridolin.kSensorPhase);

		/* choose based on what direction you want forward/positive to be.
		 * This does not affect sensor phase. */ 
		motor.setInverted(Fridolin.kMotorInvert);

		/* set the peak and nominal outputs, 12V means full */
		motor.configNominalOutputForward(0, Fridolin.kTimeoutMs);
		motor.configNominalOutputReverse(0, Fridolin.kTimeoutMs);
		motor.configPeakOutputForward(1, Fridolin.kTimeoutMs);
		motor.configPeakOutputReverse(-1, Fridolin.kTimeoutMs);

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
