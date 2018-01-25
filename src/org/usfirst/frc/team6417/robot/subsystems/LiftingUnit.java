package org.usfirst.frc.team6417.robot.subsystems;

import java.util.Arrays;

import org.usfirst.frc.team6417.robot.Fridolin;
import org.usfirst.frc.team6417.robot.RobotMap;
import org.usfirst.frc.team6417.robot.model.Event;
import org.usfirst.frc.team6417.robot.model.State;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public final class LiftingUnit extends PIDSubsystem {
	public static final Event TO_GROUND = new Event();
	public static final Event TO_SWITCH = new Event();
	public static final Event TO_SCALE_LOW = new Event();
	public static final Event TO_SCALE_HIGH = new Event();
	
	private final SpeedController motorA, motorB;
	private final Encoder altimeter;
	
	private long altimeterToReach = 0;
	private State currentState;
	
	public LiftingUnit() {
		super("LiftingUnit", 1.0, 0, 0);

		setAbsoluteTolerance(5);
		getPIDController().setContinuous(false);

		motorA = new Fridolin(RobotMap.MOTOR.LIFTING_UNIT_PORT_A);
		motorB = new Fridolin(RobotMap.MOTOR.LIFTING_UNIT_PORT_B);

		altimeter = new Encoder(RobotMap.DIO.POLE_UP_PORT_A, RobotMap.DIO.POLE_UP_PORT_B);
		altimeter.setDistancePerPulse(RobotMap.ROBOT.DIST_PER_PULSE);
		altimeter.setReverseDirection(true);
		
		State ground = currentState = new Ground();
		State theSwitch = new Switch();
		State scaleLow = new Switch();
		State scaleHigh = new Switch();

		Arrays.asList(ground,theSwitch,scaleLow,scaleHigh).stream().forEach( state -> {
			state.addTransition(TO_GROUND, theSwitch)
			     .addTransition(TO_SWITCH, scaleLow)
			     .addTransition(TO_SCALE_LOW, scaleHigh)
			     .addTransition(TO_SCALE_HIGH, ground);
		} );
	}
	
	public void onEvent(Event event) {
		currentState = currentState.transition(event);
		currentState.init();
	}
	
	@Override
	protected void initDefaultCommand() {;}

	@Override
	protected double returnPIDInput() {
		// Is changed by the state-machine
		return altimeterToReach;
	}

	@Override
	protected void usePIDOutput(double output) {
		motorA.pidWrite(output);
		motorB.pidWrite(output);
	}
	
	class Ground extends State {
		@Override
		public void init() {
			altimeterToReach = 0;
		}
	}
	class Switch extends State {
		@Override
		public void init() {
			altimeterToReach = 200;
		}
	}
	class ScaleLow extends State {
		@Override
		public void init() {
			altimeterToReach = 400;
		}
	}
	class ScaleHigh extends State {
		@Override
		public void init() {
			altimeterToReach = 600;
		}
	}

}
