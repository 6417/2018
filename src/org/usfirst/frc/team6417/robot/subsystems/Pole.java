package org.usfirst.frc.team6417.robot.subsystems;

import org.usfirst.frc.team6417.robot.Fridolin;
import org.usfirst.frc.team6417.robot.RobotMap;
import org.usfirst.frc.team6417.robot.Util;
import org.usfirst.frc.team6417.robot.commands.PoleResetAltitude;
import org.usfirst.frc.team6417.robot.model.Event;
import org.usfirst.frc.team6417.robot.model.State;
import org.usfirst.frc.team6417.robot.model.interpolation.InterpolationStrategy;
import org.usfirst.frc.team6417.robot.model.interpolation.SmoothStepInterpolationStrategy;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The Pole is able to move up and down the Gripper
 */
public final class Pole extends Subsystem {
	public static final Event SCALE_UP = new Event();
	public static final Event SCALE_DOWN = new Event();
	public static final Event SWITCH = new Event();
	public static final Event GROUND = new Event();
	
	private static final double UP_VELOCITY = 0.23;
	private static final double DOWN_VELOCITY = -0.3;
	private static final double STOP_VELOCITY = 0;
	private static final long SCALE_UP_ALTITUDE = 200;
	private static final long SCALE_DOWN_ALTITUDE = 150;
	private static final long  SWITCH_ALTITUDE = 50;
	private static final long GROUND_ALTITUDE = 0;
	
	private static final long ALTITUDE_TOLERANCE = 5;
	
	private final Fridolin motor;
	private final Encoder altimeter;
	
	private State currentState;
	private long currentAltitude;
	
	public Pole() {
		motor = new Fridolin(RobotMap.MOTOR.POLE_PORT);
		
		altimeter = new Encoder(RobotMap.DIO.POLE_UP_PORT_A,RobotMap.DIO.POLE_UP_PORT_B);
		altimeter.setDistancePerPulse(RobotMap.ROBOT.DIST_PER_PULSE);
		altimeter.setReverseDirection(true);
		
		State scaleUp = new AltitudeReachingState(SCALE_UP_ALTITUDE);
		State scaleDown = new AltitudeReachingState(SCALE_DOWN_ALTITUDE);
		State theSwitch = new AltitudeReachingState(SWITCH_ALTITUDE);
		State ground = currentState = new AltitudeReachingState(GROUND_ALTITUDE);
		
		ground.addTransition(SWITCH, theSwitch)
			  .addTransition(SCALE_DOWN, scaleDown)
			  .addTransition(SCALE_UP, scaleUp)
			  .addTransition(GROUND, ground);

		theSwitch.addTransition(GROUND, ground)
				 .addTransition(SCALE_DOWN, scaleDown)
				 .addTransition(SCALE_UP, scaleUp)
				 .addTransition(SWITCH, theSwitch);
		
		scaleDown.addTransition(GROUND, ground)
				 .addTransition(SCALE_DOWN, scaleDown)
				 .addTransition(SCALE_UP, scaleUp)
				 .addTransition(SWITCH, theSwitch);
		
		scaleUp.addTransition(GROUND, ground)
				.addTransition(SCALE_DOWN, scaleDown)
				.addTransition(SCALE_UP, scaleUp)
				.addTransition(SWITCH, theSwitch);
	}
	
	public void onEvent(Event event) {
		currentState = currentState.transition(event);
		currentState.init();
	}
	
	public void tick() {
		currentState.tick();
	}

	public void moveUp() {
		motor.set(UP_VELOCITY);
	}
	
	public void moveDown() {
		motor.set(DOWN_VELOCITY);
	}
	
	public void stop() {
		motor.set(STOP_VELOCITY);
	}
	
	public void resetEncoders() {
		altimeter.reset();
	}

	@Override
	protected void initDefaultCommand() {
		// TODO What should the pole do at initialisation?
		setDefaultCommand(new PoleResetAltitude());
	}

	public boolean isOnSwitchAltitude() {
		return isOnAltitude(SWITCH_ALTITUDE);
	}
	
	public boolean isOnAltitude(long level) {
		long currentAltitude = altimeter.get();
		boolean isAltitudeReached = Util.inRange(currentAltitude, 
				level - ALTITUDE_TOLERANCE, 
				level + ALTITUDE_TOLERANCE);
		SmartDashboard.putNumber("Pole: Altitude", currentAltitude);
		SmartDashboard.putBoolean("Pole: Is on level", isAltitudeReached);
		SmartDashboard.putBoolean("Pole: Is below level", currentAltitude < level);
		SmartDashboard.putBoolean("Pole: Is above level", currentAltitude > level);
		return isAltitudeReached;
	}

	public boolean isOnGroundAltitude() {
		return isOnAltitude(GROUND_ALTITUDE);
	}	

	class AltitudeReachingState extends State {
		private final long targetAltitude;
		private InterpolationStrategy interpolation;

		protected AltitudeReachingState(long targetAltitude){
			this.targetAltitude = targetAltitude;
		}

		@Override
		public void init() {
			interpolation = new SmoothStepInterpolationStrategy(currentAltitude, targetAltitude);
		}
		
		@Override
		public void tick() {
			motor.set(interpolation.nextX());
		}
	}

}
