package org.usfirst.frc.team6417.robot;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class MotorControllerFactory {

	public MotorController createCIM(String name, int id) {
		return create775Pro(name, id);
	}

	public MotorController createSmall(String name, int id) {
		return create775Pro(name, id);
	}

	public MotorController create775Pro(String name, int id) {
		MotorController motor = new MotorController(name, id);
		configure(motor);
		configureCurrentLimits(motor);
		return motor;
	}

	public MotorController create775ProWithEncoder(String name, int id) {
		MotorController motor = create775Pro(name, id);
		configureForClosedLoop(motor);
		return motor;
	}

	public MotorController create777ProWithPositionControl(String name, int id) {
		MotorController motor = create775Pro(name, id);
		configureForClosedLoop(motor);
		
		motor.setSensorPhase(true);
		motor.setInverted(false);

		/* Set relevant frame periods to be at least as fast as periodic rate */
		motor.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, MotorController.kTimeoutMs);
		motor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, MotorController.kTimeoutMs);

		/* set closed loop gains in slot0 - see documentation */
		motor.selectProfileSlot(MotorController.kSlotIdx, MotorController.kPIDLoopIdx);
		motor.config_kF(MotorController.kSlotIdx, 0, MotorController.kTimeoutMs);
		motor.config_kP(MotorController.kSlotIdx, 1.0, MotorController.kTimeoutMs);
		motor.config_kI(MotorController.kSlotIdx, 0, MotorController.kTimeoutMs);
		motor.config_kD(MotorController.kSlotIdx, 0, MotorController.kTimeoutMs);
		
		/* set the allowed closed-loop error */
		int allowedErrorPercentage = 10;
		int allowedErrorRelative = RobotMap.ENCODER.QUADRATURE_UNITS_PER_ROTATION / allowedErrorPercentage;
		motor.configAllowableClosedloopError(MotorController.kSlotIdx, allowedErrorRelative, MotorController.kTimeoutMs);

		/* set acceleration and vcruise velocity - see documentation */
		motor.configMotionCruiseVelocity(15000, MotorController.kTimeoutMs);
		motor.configMotionAcceleration(6000, MotorController.kTimeoutMs);
		
		/* zero the sensor */
		motor.setSelectedSensorPosition(0, MotorController.kPIDLoopIdx, MotorController.kTimeoutMs);

		return motor;
	}

	private static void configureForClosedLoop(MotorController motor) {
		/* choose the sensor and sensor direction */
		motor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, MotorController.kPIDLoopIdx,
				MotorController.kTimeoutMs);
		/*
		 * set the allowable closed-loop error, Closed-Loop output will be neutral
		 * within this range. See Table in Section 17.2.1 for native units per rotation.
		 */
		motor.configAllowableClosedloopError(0, MotorController.kPIDLoopIdx, MotorController.kTimeoutMs);

		/* set closed loop gains in slot0, typically kF stays zero. */
		motor.config_kF(MotorController.kPIDLoopIdx, 0.0, MotorController.kTimeoutMs);
		motor.config_kP(MotorController.kPIDLoopIdx, 0.1, MotorController.kTimeoutMs);
		motor.config_kI(MotorController.kPIDLoopIdx, 0.0, MotorController.kTimeoutMs);
		motor.config_kD(MotorController.kPIDLoopIdx, 0.0, MotorController.kTimeoutMs);

		/*
		 * lets grab the 360 degree position of the MagEncoder's absolute position, and
		 * intitally set the relative sensor to match.
		 */
		int absolutePosition = motor.getSelectedSensorPosition(MotorController.kPIDLoopIdx);
		/* mask out overflows, keep bottom 12 bits */
		absolutePosition &= 0xFFF;
		if (MotorController.kSensorPhase)
			absolutePosition *= -1;
		if (MotorController.kMotorInvert)
			absolutePosition *= -1;
		/* set the quadrature (relative) sensor to match absolute */
		motor.setSelectedSensorPosition(absolutePosition, MotorController.kPIDLoopIdx, MotorController.kTimeoutMs);
        SmartDashboard.putNumber(motor.getName()+" position",motor.getSelectedSensorPosition(MotorController.kPIDLoopIdx));
	}

	private static void configure(MotorController motor) {
		/* choose to ensure sensor is positive when output is positive */
		motor.setSensorPhase(MotorController.kSensorPhase);

		/*
		 * choose based on what direction you want forward/positive to be. This does not
		 * affect sensor phase.
		 */
		motor.setInverted(MotorController.kMotorInvert);

		/* set the peak and nominal outputs, 12V means full */
		motor.configNominalOutputForward(0, MotorController.kTimeoutMs);
		motor.configNominalOutputReverse(0, MotorController.kTimeoutMs);
		motor.configPeakOutputForward(1, MotorController.kTimeoutMs);
		motor.configPeakOutputReverse(-1, MotorController.kTimeoutMs);

	}

	private static void configureCurrentLimits(MotorController motor) {
		// /* Limits the current to 10 amps whenever the current has exceeded 15 amps
		// for 100 Ms */
		// motor.configContinuousCurrentLimit(10, 0);
		// motor.configPeakCurrentLimit(15, 0);
		// motor.configPeakCurrentDuration(100, 0);
		// motor.enableCurrentLimit(true);
		// /* Motor is configured to ramp from neutral to full within 2 seconds */
		// motor.configOpenloopRamp(0.7, 0);
	}
}
