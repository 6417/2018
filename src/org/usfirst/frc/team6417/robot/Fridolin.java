package org.usfirst.frc.team6417.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Fridolin extends WPI_TalonSRX {

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
