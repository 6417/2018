package org.usfirst.frc.team6417.robot.subsystems;

import org.usfirst.frc.team6417.robot.MotorController;
import org.usfirst.frc.team6417.robot.MotorControllerFactory;
import org.usfirst.frc.team6417.robot.RobotMap;
import org.usfirst.frc.team6417.robot.Util;
import org.usfirst.frc.team6417.robot.commands.LiftingUnitWagonTeleoperated;
import org.usfirst.frc.team6417.robot.model.Event;
import org.usfirst.frc.team6417.robot.model.State;
import org.usfirst.frc.team6417.robot.model.velocitymanagement.MotionPathVelocityCalculator;
import org.usfirst.frc.team6417.robot.service.powermanagement.PowerManagementStrategy;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class LiftingUnitWagon extends Subsystem {
	public static final Event FRONT = new Event("FRONT");
	public static final Event BACK = new Event("BACK");
	public static final Event STOP = new Event("STOP");

	private final MotorController motor;
	
	private final DigitalInput frontEndpointPositionDetector;

	private State currentState;
	
	private PowerManagementStrategy powerManagementStrategy;
	
	private final MotionPathVelocityCalculator motionPathVelocityCalculator = new MotionPathVelocityCalculator(
			RobotMap.ROBOT.LIFTING_UNIT_WAGON_BACK_POSITION_IN_TICKS,
			RobotMap.VELOCITY.STOP_VELOCITY,
			RobotMap.ROBOT.LIFTING_UNIT_WAGON_BACK_POSITION_BREAK_IN_TICKS,
			RobotMap.VELOCITY.LIFTING_UNIT_WAGON_MOTOR_BACKWARD_VELOCITY,
			RobotMap.ROBOT.LIFTING_UNIT_WAGON_FRONT_POSITION_BREAK_IN_TICKS,
			RobotMap.VELOCITY.LIFTING_UNIT_WAGON_MOTOR_FORWARD_VELOCITY,
			RobotMap.ROBOT.LIFTING_UNIT_WAGON_FRONT_POSITION_IN_TICKS,
			RobotMap.VELOCITY.STOP_VELOCITY
			);
	private boolean isCalibrated = false;
	private boolean isHoldingPosition;

	public LiftingUnitWagon(PowerManagementStrategy powerManagementStrategy) {
		super(RobotMap.ROBOT.LIFTING_UNIT_WAGON_NAME);
		this.powerManagementStrategy = powerManagementStrategy;
		
		final MotorControllerFactory factory = new MotorControllerFactory();
		motor = factory.create777ProWithPositionControl(RobotMap.ROBOT.LIFTING_UNIT_WAGON_MOTOR_NAME + "/"+RobotMap.MOTOR.LIFTING_UNIT_WAGON_PORT, RobotMap.MOTOR.LIFTING_UNIT_WAGON_PORT);
		motor.setInverted(true);
		motor.setSensorPhase(false);
//		motor.setNeutralMode(NeutralMode.Brake);
		motor.configOpenloopRamp(1, MotorController.kTimeoutMs);
		motor.configClosedloopRamp(0, MotorController.kTimeoutMs);
		
		motor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, MotorController.kPIDLoopIdx ,
				MotorController.kTimeoutMs );
		
		motor.config_kF(MotorController.kPIDLoopIdx, 0.0, MotorController.kTimeoutMs);
		motor.config_kP(MotorController.kPIDLoopIdx, 0.2, MotorController.kTimeoutMs);
		motor.config_kI(MotorController.kPIDLoopIdx, 0.0, MotorController.kTimeoutMs);
		motor.config_kD(MotorController.kPIDLoopIdx, 0.2, MotorController.kTimeoutMs);
		
		int allowedErrorPercentage = 10;
		int allowedErrorRelative = RobotMap.ENCODER.PULSE_PER_ROTATION / allowedErrorPercentage;
		motor.configAllowableClosedloopError(0, allowedErrorRelative, MotorController.kTimeoutMs);
//		motor = new MotorController("LUWV/"+RobotMap.MOTOR.LIFTING_UNIT_WAGON_PORT, 
//									 RobotMap.MOTOR.LIFTING_UNIT_WAGON_PORT);
//		motor.configOpenloopRamp(0.5, 0);
		

		frontEndpointPositionDetector = new DigitalInput(RobotMap.DIO.LIFTING_UNIT_WAGON_POSITION_FRONT_PORT);
		frontEndpointPositionDetector.setName(RobotMap.ROBOT.LIFTING_UNIT_WAGON_POSITION_FRONT_SENSOR_NAME+"/"+RobotMap.DIO.LIFTING_UNIT_WAGON_POSITION_FRONT_PORT);
		SmartDashboard.putBoolean(frontEndpointPositionDetector.getName(), frontEndpointPositionDetector.get());

		State stop = currentState = new Stop();
		State front = new Front();
		State back = new Back();
		
		stop.addTransition(LiftingUnitWagon.FRONT, front);
		front.addTransition(LiftingUnitWagon.BACK, back);
		front.addTransition(LiftingUnitWagon.STOP, stop);
		back.addTransition(LiftingUnitWagon.FRONT, front);
		back.addTransition(LiftingUnitWagon.STOP, stop);
	}
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new LiftingUnitWagonTeleoperated());
	}
	
	public boolean isInEndpositionFront() {
		return !frontEndpointPositionDetector.get();
	}
	public boolean isInEndpositionBack() {
		return getCurrentPosition() <= RobotMap.ROBOT.LIFTING_UNIT_WAGON_BACK_POSITION_IN_TICKS;
	}
	public boolean isInEndpoint() {
		return isInEndpositionBack() || isInEndpositionFront();
	}
	
	public void holdPosition() {
		if(isHoldingPosition) {
			return;
		}
		
		motor.set(ControlMode.Position, getCurrentPosition());
		setHoldPosition(true);
	}

	/**
	 * Manual control for the lifting-unit.
	 * @param velocity -1..1
	 */
	public void move(double velocity) {
		if(!isCalibrated) {
			System.err.println(getName()+" not calibrated.");
			return;
		}
		
		internalMove(velocity);
	}
	
	public void moveToPos(double positionInTicks) {
		if(!isCalibrated) {
			System.err.println(getName()+" not calibrated.");
			return;
		}
		
		motor.set(ControlMode.Position, positionInTicks);
	}
	
	
	
	private void internalMove(double velocity) {
		SmartDashboard.putNumber("LUW pos curr", getCurrentPosition());
		SmartDashboard.putNumber("LUW vel", velocity);
		velocity = Util.limit(velocity, 
							  RobotMap.VELOCITY.LIFTING_UNIT_WAGON_MOTOR_BACKWARD_VELOCITY, 
							  RobotMap.VELOCITY.LIFTING_UNIT_WAGON_MOTOR_FORWARD_VELOCITY);
//		velocity = motionPathVelocityCalculator.calculateVelocity(velocity, getCurrentPosition());
		if(velocity > 0) {
			if(isInEndpositionFront()) {
				holdPosition();
			}else{
				motor.set(ControlMode.PercentOutput, velocity);	
				setHoldPosition(false);
			}
		}else if(velocity < 0) {
			if(isInEndpositionBack()) {
				holdPosition();
			}else{
				motor.set(ControlMode.PercentOutput, velocity);				
				setHoldPosition(false);
			}
		}else{
			holdPosition();
		}
	}

	private int getCurrentPosition() {
		return motor.getSelectedSensorPosition(MotorController.kSlotIdx);
	}

	public void resetEncoder() {
		motor.setSelectedSensorPosition(0, MotorController.kSlotIdx, MotorController.kTimeoutMs);
	}
	
	void setHoldPosition(boolean isHoldPos) {
		this.isHoldingPosition = isHoldPos;
		SmartDashboard.putBoolean(RobotMap.ROBOT.LIFTING_UNIT_WAGON_NAME+" pos ctrl", isHoldPos);
		SmartDashboard.putBoolean(RobotMap.ROBOT.LIFTING_UNIT_WAGON_NAME+" vel ctrl", !isHoldPos);
	}

	public void startMoveToEndpointFront() {
		if(isInEndpositionFront()) {
			return;
		}
		
		motor.set(RobotMap.VELOCITY.LIFTING_UNIT_WAGON_MOTOR_VERY_SLOW_FORWARD_VELOCITY);	
	}

	public void stopMoveToEndpointFront() {
		motor.set(RobotMap.VELOCITY.STOP_VELOCITY);
		resetEncoder();
		isCalibrated = true;
		holdPosition();
	}


	public void onEvent(Event event) {
		currentState = currentState.transition(event);
		currentState.init();
	}
	
	public void tick() {
		currentState.tick();
		SmartDashboard.putBoolean(frontEndpointPositionDetector.getName(), frontEndpointPositionDetector.get());
		SmartDashboard.putNumber(RobotMap.ROBOT.LIFTING_UNIT_WAGON_NAME+" pos", getCurrentPosition());
	}
	public boolean isFinished() {
		return currentState.isFinished();
	}
	
	private double p() {
		return powerManagementStrategy.calculatePower();
	}
	
//	class Back extends State {
//		
//		@Override
//		public void init() {
//			if(!isCalibrated) {
//				return;
//			}
//			motor.set(p() * RobotMap.VELOCITY.LIFTING_UNIT_WAGON_MOTOR_BACKWARD_VELOCITY);
//		}
//		@Override
//		public void tick() {
//			if(!isCalibrated) {
//				return;
//			}
//			
//			if(isInEndPosition()) {
//				motor.set(RobotMap.VELOCITY.STOP_VELOCITY);
//			}else {
//				motor.set(p() * RobotMap.VELOCITY.LIFTING_UNIT_WAGON_MOTOR_BACKWARD_VELOCITY);
//			}
//			SmartDashboard.putNumber("LUW ticks", motor.getSelectedSensorPosition(MotorController.kSlotIdx));			
//		}
//		@Override
//		public boolean isFinished() {
//			if(!isCalibrated) {
//				return true;
//			}
//
//			return isInEndPosition();
//		}
//		
//		private boolean isInEndPosition() {
//			return motor.getSelectedSensorPosition(MotorController.kSlotIdx) >= RobotMap.SENSOR.LIFTING_UNIT_WAGON_ENDPOSITION_FRONT_THRESHOLD; 
//		}
//		
//	}
	
	class Front extends State {
		@Override
		public void init() {
			moveToPos(RobotMap.ROBOT.LIFTING_UNIT_WAGON_FRONT_POSITION_IN_TICKS);
		}
		
		@Override
		public boolean isFinished() {
			return isInEndpositionFront();
		}

	}

	class Back extends State {
		@Override
		public void init() {
			moveToPos(RobotMap.ROBOT.LIFTING_UNIT_WAGON_BACK_POSITION_IN_TICKS);
		}
		
		@Override
		public boolean isFinished() {
			return isInEndpositionBack();
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

