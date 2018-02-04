package org.usfirst.frc.team6417.robot.subsystems;

import org.usfirst.frc.team6417.robot.Fridolin;
import org.usfirst.frc.team6417.robot.Robot;
import org.usfirst.frc.team6417.robot.RobotMap;
import org.usfirst.frc.team6417.robot.model.Event;
import org.usfirst.frc.team6417.robot.model.State;

import edu.wpi.first.wpilibj.command.Subsystem;

public final class LiftingUnitWagon extends Subsystem {
	public static final Event FRONT = new Event("FRONT");
	public static final Event BACK = new Event("BACK");

	private final double FRONT_VELOCITY = 0.25;
	private final double BACK_VELOCITY = -0.5;
	private final double STOP_VELOCITY = 0;

	private final Fridolin motor = new Fridolin(RobotMap.MOTOR.LIFTING_UNIT_WAGON_PORT);

	private State currentState;

	public LiftingUnitWagon() {
		State front = currentState = new Front();
		State back = new Back();
		
		front.addTransition(LiftingUnitWagon.FRONT, back);
		back.addTransition(LiftingUnitWagon.BACK, front);
	}
	
	public void onEvent(Event event) {
		currentState = currentState.transition(event);
		currentState.init();
	}
	
	public void tick() {
		currentState.tick();
	}
	
	@Override
	protected void initDefaultCommand() {
		
	}
	
	private double p() {
		return Robot.powerManager.calculatePowerFor(this);
	}
	
	class Front extends State {
		private long startTime;
		
		@Override
		public void init() {
			motor.set(p() * FRONT_VELOCITY);
			startTime = System.currentTimeMillis();
		}
		@Override
		public void tick() {
			if(isTimeUp()) {
				motor.set(STOP_VELOCITY);
			}
		}
		@Override
		public boolean isFinished() {
			return isTimeUp();
		}
		private boolean isTimeUp() {
			return System.currentTimeMillis() - startTime > 1000;
		}
	}
	
	class Back extends State {
		private long startTime;

		@Override
		public void init() {
			motor.set(p() * BACK_VELOCITY);
			startTime = System.currentTimeMillis();
		}
		
		@Override
		public void tick() {
			if(System.currentTimeMillis() - startTime > 1000) {
				motor.set(STOP_VELOCITY);
			}
		}
		@Override
		public boolean isFinished() {
			return isTimeUp();
		}
		
		private boolean isTimeUp() {
			return System.currentTimeMillis() - startTime > 1000;
		}
	}

	public boolean isFinished() {
		return currentState.isFinished();
	}


}

