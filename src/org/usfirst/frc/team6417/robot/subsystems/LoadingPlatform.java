package org.usfirst.frc.team6417.robot.subsystems;

import org.usfirst.frc.team6417.robot.Fridolin;
import org.usfirst.frc.team6417.robot.RobotMap;
import org.usfirst.frc.team6417.robot.model.Event;
import org.usfirst.frc.team6417.robot.model.State;

import edu.wpi.first.wpilibj.command.Subsystem;

public final class LoadingPlatform extends Subsystem {
	public static final Event UP = new Event();
	public static final Event DOWN = new Event();

	private final double UP_VELOCITY = 0.25;
	private final double DOWN_VELOCITY = -0.5;
	private final double STOP_VELOCITY = 0;

	private final Fridolin motor = new Fridolin(RobotMap.MOTOR.LOADING_PLATFORM_PORT);

	private State currentState;

	public LoadingPlatform() {
		State up = currentState = new Up();
		State down = new Down();
		
		up.addTransition(LoadingPlatform.DOWN, down);
		down.addTransition(LoadingPlatform.UP, up);
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
	
	class Up extends State {
		private long startTime;
		
		@Override
		public void init() {
			motor.set(UP_VELOCITY);
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
	
	class Down extends State {
		private long startTime;

		@Override
		public void init() {
			motor.set(DOWN_VELOCITY);
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

