package org.usfirst.frc.team6417.robot.subsystems;

import org.usfirst.frc.team6417.robot.Fridolin;
import org.usfirst.frc.team6417.robot.Robot;
import org.usfirst.frc.team6417.robot.RobotMap;
import org.usfirst.frc.team6417.robot.model.Event;
import org.usfirst.frc.team6417.robot.model.State;

import edu.wpi.first.wpilibj.AnalogTrigger;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.command.Subsystem;

public final class LiftingUnitWagon extends Subsystem {
	public static final Event FRONT = new Event("FRONT");
	public static final Event BACK = new Event("BACK");

	private final Fridolin motor = new Fridolin(RobotMap.MOTOR.LIFTING_UNIT_WAGON_PORT);
	
	private final Counter frontEndPositionDetector;
	private final Counter backEndPositionDetector;

	private State currentState;

	public LiftingUnitWagon() {
		AnalogTrigger analogTrigger = new AnalogTrigger(RobotMap.AIO.LIFTING_UNIT_WAGON_ENDPOSITION_FRONT_PORT);
		analogTrigger.setLimitsRaw(RobotMap.SENSOR.LIFTING_UNIT_WAGON_ENDPOSITION_LOWER_THRESHOLD, 
								   RobotMap.SENSOR.LIFTING_UNIT_WAGON_ENDPOSITION_UPPER_THRESHOLD);
		frontEndPositionDetector = new Counter(analogTrigger);
		analogTrigger = new AnalogTrigger(RobotMap.AIO.LIFTING_UNIT_WAGON_ENDPOSITION_BACK_PORT);
		analogTrigger.setLimitsRaw(RobotMap.SENSOR.LIFTING_UNIT_WAGON_ENDPOSITION_LOWER_THRESHOLD, 
								   RobotMap.SENSOR.LIFTING_UNIT_WAGON_ENDPOSITION_UPPER_THRESHOLD);
		backEndPositionDetector = new Counter(analogTrigger);
		
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
	protected void initDefaultCommand() {;}
	
	public boolean isFinished() {
		return currentState.isFinished();
	}
	
	private double p() {
		return Robot.powerManager.calculatePowerFor(this);
	}
	
	class Front extends State {
		
		@Override
		public void init() {
			motor.set(p() * RobotMap.VELOCITY.LIFTING_UNIT_WAGON_MOTOR_FORWARD_VELOCITY);
		}
		@Override
		public void tick() {
			if(isInEndPosition()) {
				motor.set(RobotMap.VELOCITY.STOP_VELOCITY);
			}else {
				motor.set(p() * RobotMap.VELOCITY.LIFTING_UNIT_WAGON_MOTOR_FORWARD_VELOCITY);
			}
		}
		@Override
		public boolean isFinished() {
			boolean isFinished = isInEndPosition();
			if(isFinished) {
				frontEndPositionDetector.reset();
			}
			return isFinished;
		}
		
		private boolean isInEndPosition() {
			return frontEndPositionDetector.get() > 0;
		}
		
	}
	
	class Back extends State {
		@Override
		public void init() {
			motor.set(p() * RobotMap.VELOCITY.LIFTING_UNIT_WAGON_MOTOR_BACKWARD_VELOCITY);
		}
		@Override
		public void tick() {
			if(isInEndPosition()) {
				motor.set(RobotMap.VELOCITY.STOP_VELOCITY);
			}else {
				motor.set(p() * RobotMap.VELOCITY.LIFTING_UNIT_WAGON_MOTOR_BACKWARD_VELOCITY);
			}
		}
		@Override
		public boolean isFinished() {
			boolean isFinished = isInEndPosition();
			if(isFinished) {
				backEndPositionDetector.reset();
			}
			return isFinished;
		}
		
		private boolean isInEndPosition() {
			return backEndPositionDetector.get() > 0;
		}
		
	}


}

