package org.usfirst.frc.team6417.robot.subsystems;

import java.util.HashMap;
import java.util.Map;

import org.usfirst.frc.team6417.robot.Fridolin;
import org.usfirst.frc.team6417.robot.RobotMap;
import org.usfirst.frc.team6417.robot.commands.GripperStop;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The Gripper can be in one of three states: Stopped, Pushing or Pulling.
 * Given an event a state change happens. 
 * Push, pull and stop movements are smoothened between the start- and end-velocity.
 */
public final class Gripper extends Subsystem {
	public enum Event {
		STOP,
		PUSH,
		PULL
	}
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
		
		stopped.addTransition(Event.PUSH, pushing);
		stopped.addTransition(Event.PULL, pulling);
		stopped.addTransition(Event.STOP, stopped);		
		pushing.addTransition(Event.STOP, stopped);
		pulling.addTransition(Event.STOP, stopped);	
		
		SmartDashboard.putString("Gripper initial state", currentState.getClass().getSimpleName());
	}
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new GripperStop());
	}
	
	public void onEvent(Event event) {
		SmartDashboard.putString("Gripper state (prev)", currentState.getClass().getSimpleName());		
		SmartDashboard.putString("Gripper event", event.name());		
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
	
	
	/**
	 * An interpolation strategy interpolates between two points.
	 * @author BIN
	 */
	abstract class InterpolationStrategy {
		public abstract double nextX();
	}
	
	class SmoothStep extends InterpolationStrategy {
		double stepCount = 50.0;
		double currentStep = 0.0;

		private final double startVelocity;
		private final double endVelocity;
		
		SmoothStep(double startVelocity, double endVelocity){
			this.startVelocity = startVelocity;
			this.endVelocity = endVelocity; 
		}
		
		SmoothStep(double startVelocity, double endVelocity, int steps){
			this(startVelocity, endVelocity); 
			this.stepCount = steps;
		}
		
		@Override
		public double nextX() {
			double x = currentStep / stepCount;
			x = smoothstep(x);
			double velocity = (this.endVelocity * x) + (this.startVelocity * (1.0 - x));
			SmartDashboard.putNumber("Velocity", velocity);
			if(currentStep < stepCount) {
				currentStep++;
			}
			return velocity;
		}
		/**
		 * SmoothStep function from http://sol.gfxile.net/interpolation/index.html
		 * @param x
		 * @return smoothened value
		 */
		private double smoothstep(double x) {
			return ((x) * (x) * (3.0 - 2.0 * (x)));
		}
		
	}
	
	/**
	 * Class that represents a physical state of the gripper
	 */
	abstract class State {
		private final Map<Event, State> eventToStateMap = new HashMap<>();

		protected void init() {;}
		/**
		 * Called every ~50ms
		 */
		protected void tick() {;}
		
		public void addTransition(Event event, State state) {
			eventToStateMap.put(event, state);
		}
		
		public State transition(Event event) {
			State nextState = eventToStateMap.get(event);
			if(nextState == null) {
				SmartDashboard.putString("Missing transition in gripper", event.name());
				return this;
			}
			return nextState;
		}
	}
	class Stopped extends State {
		InterpolationStrategy regulator;
		
		@Override
		protected void init() {
			regulator = new SmoothStep(STOP_VELOCITY, currentVelocity, 10);
		}
		
		@Override
		protected void tick() {
			setVelocity(regulator.nextX());
		}
	}
	
	class Pushing extends State {
		private InterpolationStrategy regulator;
		
		@Override
		protected void init() {
			regulator = new SmoothStep(STOP_VELOCITY, PUSH_VELOCITY);
		}

		@Override
		protected void tick() {
			setVelocity(regulator.nextX());
		}		
	}
	class Pulling extends State {
		private InterpolationStrategy regulator;
		
		@Override
		protected void init() {
			regulator = new SmoothStep(STOP_VELOCITY, PULL_VELOCITY);
		}
		
		@Override
		protected void tick() {
			setVelocity(regulator.nextX());
		}		
	}
}
