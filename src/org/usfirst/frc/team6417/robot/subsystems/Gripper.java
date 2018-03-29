package org.usfirst.frc.team6417.robot.subsystems;

import org.usfirst.frc.team6417.robot.MotorController;
import org.usfirst.frc.team6417.robot.MotorControllerFactory;
import org.usfirst.frc.team6417.robot.RobotMap;
import org.usfirst.frc.team6417.robot.model.Event;
import org.usfirst.frc.team6417.robot.model.State;
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

	private final PowerManagementStrategy powerManagementStrategy;

	private final MotorController leftMotor;
	private final MotorController rightMotor;

	private State currentState;

	public Gripper(PowerManagementStrategy powerManagementStrategy) {
		super(RobotMap.ROBOT.GRIPPER_NAME);
		
		this.powerManagementStrategy = powerManagementStrategy;
		
		final MotorControllerFactory factory = new MotorControllerFactory();
		leftMotor = factory.create775Pro(RobotMap.ROBOT.GRIPPER_LEFT_NAME, RobotMap.MOTOR.GRIPPER_LEFT_PORT);		
		leftMotor.configOpenloopRamp(0, MotorController.kTimeoutMs);
		rightMotor = factory.create775Pro(RobotMap.ROBOT.GRIPPER_RIGHT_NAME, RobotMap.MOTOR.GRIPPER_RIGHT_PORT);
		rightMotor.configOpenloopRamp(0, MotorController.kTimeoutMs);
		
//		leftMotor.follow(rightMotor);
		
		State stopped = currentState = new Stopped();
		State pushing = new Pushing();
		State pulling = new Pulling();

		stopped.addTransition(Gripper.PUSH, pushing);
		stopped.addTransition(Gripper.PULL, pulling);
		stopped.addTransition(Gripper.STOP, stopped);
		pushing.addTransition(Gripper.STOP, stopped);
		pushing.addTransition(Gripper.PUSH, pushing);
		pulling.addTransition(Gripper.STOP, stopped);
	}


	@Override
	protected void initDefaultCommand() {;}

	public void onEvent(Event event) {
		currentState = currentState.transition(event);
		currentState.init();
	}

	public void tick() {
		currentState.tick();
	}

	public boolean isFinished() {
		return currentState.isFinished();
	}

	private void setVelocity(double vel) {
		//vel = powerManagementStrategy.calculatePower() * vel;		
		//Only master-motor must be set
		rightMotor.set(vel);
		leftMotor.set(vel);
		SmartDashboard.putNumber(getName()+" vel", vel);
	}

	class Stopped extends State {

		@Override
		public void init() {
			setVelocity(RobotMap.VELOCITY.STOP_VELOCITY);
		}

		@Override
		public boolean isFinished() {
			return true;
		}
	}

	class Pushing extends State {

		@Override
		public void tick() {
			setVelocity(RobotMap.VELOCITY.GRIPPER_PUSH_VELOCITY);
		}

		@Override
		public boolean isFinished() {
			return true;
		}
	}

	class Pulling extends State {

		@Override
		public void tick() {
			setVelocity(RobotMap.VELOCITY.GRIPPER_PULL_VELOCITY);
		}

		@Override
		public boolean isFinished() {
			return true;
		}
	}
}
