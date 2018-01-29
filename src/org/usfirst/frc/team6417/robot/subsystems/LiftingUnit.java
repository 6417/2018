package org.usfirst.frc.team6417.robot.subsystems;

import java.util.Arrays;

import org.usfirst.frc.team6417.robot.Fridolin;
import org.usfirst.frc.team6417.robot.RobotMap;
import org.usfirst.frc.team6417.robot.model.Event;
import org.usfirst.frc.team6417.robot.model.State;
import org.usfirst.frc.team6417.robot.service.powermanagement.PowerManagementService;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class LiftingUnit extends PIDSubsystem {
	public static final Event TO_GROUND = new Event("TO_GROUND");
	public static final Event TO_SWITCH = new Event("TO_SWITCH");
	public static final Event TO_SCALE_LOW = new Event("TO_SCALE_LOW");
	public static final Event TO_SCALE_HIGH = new Event("TO_SCALE_HIGH");

	private static final double kP = 1.0;
	private static final double kI = 0.0;
	private static final double kD = 0.0;
	
	private final PowerManagementService powerManagementService;
	private final SpeedController motorA, motorB;
	private final Encoder altimeter;
	
	private State currentState;
	
	public LiftingUnit(PowerManagementService powerManagementService) {
		super("LiftingUnit", kP, kI, kD);
		this.powerManagementService = powerManagementService;
		
		setAbsoluteTolerance(5);
		setOutputRange(-1.0, 1.0);
		getPIDController().setContinuous(false);		

		motorA = new Fridolin(RobotMap.MOTOR.LIFTING_UNIT_PORT_A);
		motorB = new Fridolin(RobotMap.MOTOR.LIFTING_UNIT_PORT_B);

		altimeter = new Encoder(RobotMap.DIO.LIFTING_UNIT_PORT_A, RobotMap.DIO.LIFTING_UNIT_PORT_B);
		altimeter.setDistancePerPulse(RobotMap.ROBOT.DIST_PER_PULSE);
		altimeter.setReverseDirection(true);
		
		State ground = currentState = new Ground();
		State theSwitch = new Switch();
		State scaleLow = new Switch();
		State scaleHigh = new Switch();

		Arrays.asList(ground,theSwitch,scaleLow,scaleHigh).stream().forEach( state -> {
			state.addTransition(TO_GROUND, ground)
			     .addTransition(TO_SWITCH, theSwitch)
			     .addTransition(TO_SCALE_LOW, scaleLow)
			     .addTransition(TO_SCALE_HIGH, scaleHigh);
		} );
	}
	
	public void onEvent(Event event) {
		SmartDashboard.putString("LiftingUnit event", event.getClass().getSimpleName());		
		SmartDashboard.putString("LiftingUnit state (prev)", currentState.getClass().getSimpleName());		
		currentState = currentState.transition(event);
		currentState.init();
		SmartDashboard.putString("LiftingUnit state (current)", currentState.getClass().getSimpleName());		
	}
	
	@Override
	protected void initDefaultCommand() {;}

	@Override
	protected double returnPIDInput() {
		SmartDashboard.putNumber("LiftingUnit altimeter", altimeter.get());		
		return altimeter.get();
	}

	@Override
	protected void usePIDOutput(double output) {
		SmartDashboard.putNumber("LiftingUnit PID output", output);		
		motorA.pidWrite(output);
		motorB.pidWrite(output);
	}
	
	class Ground extends State {
		@Override
		public void init() {
			SmartDashboard.putNumber("LiftingUnit Ground setpoint", 0);
			setSetpoint(0);
			enable();
		}
	}
	class Switch extends State {
		@Override
		public void init() {
			SmartDashboard.putNumber("LiftingUnit Switch setpoint", 200);
			setSetpoint(200);
			enable();
		}
	}
	class ScaleLow extends State {
		@Override
		public void init() {
			SmartDashboard.putNumber("LiftingUnit ScaleLow setpoint", 400);
			setSetpoint(400);
			enable();
		}
	}
	class ScaleHigh extends State {
		@Override
		public void init() {
			SmartDashboard.putNumber("LiftingUnit ScaleHigh setpoint", 600);
			setSetpoint(600);
			enable();
		}
	}

}
