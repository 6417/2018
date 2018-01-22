package org.usfirst.frc.team6417.robot.subsystems;

import org.usfirst.frc.team6417.robot.Fridolin;
import org.usfirst.frc.team6417.robot.RobotMap;
import org.usfirst.frc.team6417.robot.commands.GripperStop;
import org.usfirst.frc.team6417.robot.model.Event;
import org.usfirst.frc.team6417.robot.model.State;
import org.usfirst.frc.team6417.robot.model.interpolation.InterpolationStrategy;
import org.usfirst.frc.team6417.robot.model.interpolation.SmoothStepInterpolationStrategy;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The Gripper can be in one of three states: Stopped, Pushing or Pulling.
 * Given an event a state change happens. 
 * Push, pull and stop movements are smoothened between the start- and end-velocity.
 */
public final class Gripper extends Subsystem {
	public static final Event STOP = new Event();
	public static final Event PUSH = new Event();
	public static final Event PULL = new Event();

	private final double PUSH_VELOCITY = 0.95;
	private final double PULL_VELOCITY = -0.95;
	private final double STOP_VELOCITY = 0;
	
	private final Fridolin leftMotor;
	private final Fridolin rightMotor;

	private State currentState;
	private double currentVelocity = 0;
	
	public Gripper() {
		leftMotor = new Fridolin(RobotMap.MOTOR.GRIPPER_LEFT_PORT);
		rightMotor = new Fridolin(RobotMap.MOTOR.GRIPPER_RIGHT_PORT);
		
		State stopped = currentState = new Stopped();
		State pushing = new Pushing();
		State pulling = new Pulling();
		
		stopped.addTransition(Gripper.PUSH, pushing);
		stopped.addTransition(Gripper.PULL, pulling);
		stopped.addTransition(Gripper.STOP, stopped);		
		pushing.addTransition(Gripper.STOP, stopped);
		pulling.addTransition(Gripper.STOP, stopped);	
		
		SmartDashboard.putString("Gripper initial state", currentState.getClass().getSimpleName());
	}
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new GripperStop());
	}
	
	public void onEvent(Event event) {
		SmartDashboard.putString("Gripper state (prev)", currentState.getClass().getSimpleName());		
		SmartDashboard.putString("Gripper event", event.getClass().getSimpleName());		
		currentState = currentState.transition(event);
		currentState.init();
		SmartDashboard.putString("Gripper state (current)", currentState.getClass().getSimpleName());		
	}
	
	public void tick() {
		currentState.tick();
	}
	
	private void setVelocity(double vel) {
		this.currentVelocity = vel;
		leftMotor.set(vel);
		rightMotor.set(vel);
		SmartDashboard.putNumber("Gripper velocity", vel);
	}
	
	
	class Stopped extends State {
		InterpolationStrategy regulator;
		
		@Override
		public void init() {
			regulator = new SmoothStepInterpolationStrategy(STOP_VELOCITY, currentVelocity, 10);
		}
		
		@Override
		public void tick() {
			setVelocity(regulator.nextX());
		}
	}
	
	class Pushing extends State {
		private InterpolationStrategy regulator;
		
		@Override
		public void init() {
			regulator = new SmoothStepInterpolationStrategy(STOP_VELOCITY, PUSH_VELOCITY);
		}

		@Override
		public void tick() {
			setVelocity(regulator.nextX());
		}		
	}
	class Pulling extends State {
		private InterpolationStrategy regulator;
		
		@Override
		public void init() {
			regulator = new SmoothStepInterpolationStrategy(STOP_VELOCITY, PULL_VELOCITY);
		}
		
		@Override
		public void tick() {
			setVelocity(regulator.nextX());
		}		
	}
}
