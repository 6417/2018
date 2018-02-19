package org.usfirst.frc.team6417.robot.subsystems;

import org.usfirst.frc.team6417.robot.MotorController;
import org.usfirst.frc.team6417.robot.RobotMap;
import org.usfirst.frc.team6417.robot.model.Event;
import org.usfirst.frc.team6417.robot.model.State;
import org.usfirst.frc.team6417.robot.service.powermanagement.PowerManagementStrategy;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class LiftingUnitWagon extends Subsystem {
	public static final Event FRONT = new Event("FRONT");
	public static final Event BACK = new Event("BACK");
	public static final Event STOP = new Event("STOP");

	private final MotorController motor = new MotorController("Motor", RobotMap.MOTOR.LIFTING_UNIT_WAGON_PORT);
	
	private final DigitalInput frontEndpointPositionDetector;

	private State currentState;
	
	private PowerManagementStrategy powerManagementStrategy;
	
	private boolean isCalibrated = false;

	public LiftingUnitWagon(PowerManagementStrategy powerManagementStrategy) {
		super("LiftingUnitWagon");
		this.powerManagementStrategy = powerManagementStrategy;
		
		frontEndpointPositionDetector = new DigitalInput(0);
		motor.configOpenloopRamp(0.5, 0);

		State stop = currentState = new Stop();
		State front = new Front();
		State back = new Back();
		
		stop.addTransition(LiftingUnitWagon.FRONT, front);
		front.addTransition(LiftingUnitWagon.BACK, back);
		front.addTransition(LiftingUnitWagon.STOP, stop);
		back.addTransition(LiftingUnitWagon.FRONT, front);
		back.addTransition(LiftingUnitWagon.STOP, stop);
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
		return powerManagementStrategy.calculatePower();
	}
	
	class Back extends State {
		
		@Override
		public void init() {
			if(!isCalibrated) {
				return;
			}
			motor.set(p() * RobotMap.VELOCITY.LIFTING_UNIT_WAGON_MOTOR_BACKWARD_VELOCITY);
		}
		@Override
		public void tick() {
			if(!isCalibrated) {
				return;
			}
			
			if(isInEndPosition()) {
				motor.set(RobotMap.VELOCITY.STOP_VELOCITY);
			}else {
				motor.set(p() * RobotMap.VELOCITY.LIFTING_UNIT_WAGON_MOTOR_BACKWARD_VELOCITY);
			}
			SmartDashboard.putNumber("LUW ticks", motor.getSelectedSensorPosition(MotorController.kSlotIdx));			
		}
		@Override
		public boolean isFinished() {
			if(!isCalibrated) {
				return true;
			}

			return isInEndPosition();
		}
		
		private boolean isInEndPosition() {
			return motor.getSelectedSensorPosition(MotorController.kSlotIdx) >= RobotMap.SENSOR.LIFTING_UNIT_WAGON_ENDPOSITION_FRONT_THRESHOLD; 
		}
		
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
				motor.setSelectedSensorPosition(0, MotorController.kSlotIdx, MotorController.kTimeoutMs);
				isCalibrated = true;
			}else {
				motor.set(p() * RobotMap.VELOCITY.LIFTING_UNIT_WAGON_MOTOR_FORWARD_VELOCITY);
			}
			SmartDashboard.putNumber("LUW ticks", motor.getSelectedSensorPosition(MotorController.kSlotIdx));
		}
		@Override
		public boolean isFinished() {
			boolean isFinished = isInEndPosition();
			SmartDashboard.putNumber("LUW ticks", motor.getSelectedSensorPosition(MotorController.kSlotIdx));
			return isFinished;
		}
		
		private boolean isInEndPosition() {
			return !frontEndpointPositionDetector.get() ;
		}
		
	}

	class Stop extends State {
		@Override
		public void init() {
			motor.set(RobotMap.VELOCITY.STOP_VELOCITY);
		}

		@Override
		public boolean isFinished() {
			return true;
		}
		
	}

}

