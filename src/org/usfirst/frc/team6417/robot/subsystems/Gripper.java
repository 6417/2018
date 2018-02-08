package org.usfirst.frc.team6417.robot.subsystems;

import org.usfirst.frc.team6417.robot.MotorController;
import org.usfirst.frc.team6417.robot.MotorControllerFactory;
import org.usfirst.frc.team6417.robot.RobotMap;
import org.usfirst.frc.team6417.robot.model.Event;
import org.usfirst.frc.team6417.robot.model.State;
import org.usfirst.frc.team6417.robot.model.interpolation.InterpolationStrategy;
import org.usfirst.frc.team6417.robot.model.interpolation.SmoothStepInterpolationStrategy;
import org.usfirst.frc.team6417.robot.service.powermanagement.PowerManagementStrategy;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The Gripper can be in one of three states: Stopped, Pushing or Pulling. Given
 * an event a state change happens. Push, pull and stop movements are smoothened
 * between the start- and end-velocity.
 */
public final class Gripper extends Subsystem {
	public static final Event STOP = new Event("STOP");
	public static final Event PUSH = new Event("PUSH");
	public static final Event PULL = new Event("PULL");

	private final double PUSH_VELOCITY = 0.45;
	private final double PULL_VELOCITY = -0.45;
	private final double STOP_VELOCITY = 0;

	private final PowerManagementStrategy powerManagementStrategy;

	private final MotorController leftMotor;
	private final MotorController rightMotor;

	private State currentState;

	public Gripper(PowerManagementStrategy powerManagementStrategy) {
		super("Gripper");
		
		this.powerManagementStrategy = powerManagementStrategy;
		
		final MotorControllerFactory factory = new MotorControllerFactory();
		leftMotor = factory.create775Pro("Left-Motor-Slave", RobotMap.MOTOR.GRIPPER_LEFT_PORT);
		rightMotor = factory.create775Pro("Right-Motor-Master", RobotMap.MOTOR.GRIPPER_RIGHT_PORT);
		leftMotor.follow(rightMotor);
		
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
	protected void initDefaultCommand() {;}

	public void onEvent(Event event) {
		SmartDashboard.putString("Gripper event", event.toString());
		SmartDashboard.putString("Gripper state (prev)", currentState.getClass().getSimpleName());
		currentState = currentState.transition(event);
		currentState.init();
		SmartDashboard.putString("Gripper state (current)", currentState.getClass().getSimpleName());
	}

	public void tick() {
		currentState.tick();
	}

	public boolean isFinished() {
		return currentState.isFinished();
	}

	private void setVelocity(double vel) {
		vel = powerManagementStrategy.calculatePower() * vel;
		//Only master-motor must be set
		rightMotor.set(vel);
		SmartDashboard.putNumber("Gripper velocity", vel);
	}

	class Stopped extends State {

		@Override
		public void init() {
			setVelocity(0.0);
		}

		@Override
		public boolean isFinished() {
			return true;
		}
	}

	class Pushing extends State {
		private InterpolationStrategy regulator;

		@Override
		public void init() {
			regulator = new SmoothStepInterpolationStrategy(STOP_VELOCITY, PUSH_VELOCITY, 10);
		}

		@Override
		public void tick() {
			setVelocity(regulator.nextX());
		}

		@Override
		public boolean isFinished() {
			return regulator.onTarget();
		}
	}

	class Pulling extends State {
		private InterpolationStrategy regulator;

		@Override
		public void init() {
			regulator = new SmoothStepInterpolationStrategy(STOP_VELOCITY, PULL_VELOCITY, 20);
		}

		@Override
		public void tick() {
			setVelocity(regulator.nextX());
		}

		@Override
		public boolean isFinished() {
			return regulator.onTarget();
		}
	}
}
