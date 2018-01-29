package org.usfirst.frc.team6417.robot.subsystems;

import org.usfirst.frc.team6417.robot.Fridolin;
import org.usfirst.frc.team6417.robot.RobotMap;
import org.usfirst.frc.team6417.robot.model.Event;
import org.usfirst.frc.team6417.robot.model.State;
import org.usfirst.frc.team6417.robot.service.powermanagement.PowerManagementService;

import edu.wpi.first.wpilibj.command.Subsystem;

public final class LoadingPlatform extends Subsystem {
	public static final Event UP = new Event("UP");
	public static final Event DOWN = new Event("DOWN");

	private final double UP_VELOCITY = 0.25;
	private final double DOWN_VELOCITY = -0.5;
	private final double STOP_VELOCITY = 0;

	private final PowerManagementService powerManagementService;
	private final Fridolin motor = new Fridolin(RobotMap.MOTOR.LOADING_PLATFORM_PORT);

	private State currentState;

	public LoadingPlatform(PowerManagementService powerManagementService) {
		this.powerManagementService = powerManagementService;
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
	
	private double p() {
		return powerManagementService.calculatePowerFor(this);
	}
	
	class Up extends State {
		private long startTime;
		
		@Override
		public void init() {
			motor.set(p() * UP_VELOCITY);
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
			motor.set(p() * DOWN_VELOCITY);
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

