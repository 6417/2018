package org.usfirst.frc.team6417.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Fridolin extends WPI_TalonSRX {
	/**
	 * Which PID slot to pull gains from. Starting 2018, you can choose from
	 * 0,1,2 or 3. Only the first two (0,1) are visible in web-based
	 * configuration.
	 */
	public static final int kSlotIdx = 0;

	/*
	 * Talon SRX/ Victor SPX will supported multiple (cascaded) PID loops. For
	 * now we just want the primary one.
	 */
	public static final int kPIDLoopIdx = 0;

	/*
	 * set to zero to skip waiting for confirmation, set to nonzero to wait and
	 * report to DS if action fails.
	 */
	public static final int kTimeoutMs = 10;
	
	/* choose so that Talon does not report sensor out of phase */
	public static boolean kSensorPhase = true;

	/* choose based on what direction you want to be positive,
		this does not affect motor invert. */
	public static boolean kMotorInvert = false;
	
	
	private double maxSpeed = 1;

	public Fridolin(final int channel) {
		super(channel);
	}

	public Fridolin(final String name, final int channel) {
		this(channel);
		setName(name);
	}

	@Override
	public void set(double speed) {
		super.set(maxSpeed * speed);
	}

	public void setMaxSpeed(double speed) {
		maxSpeed = speed;
	}
}
