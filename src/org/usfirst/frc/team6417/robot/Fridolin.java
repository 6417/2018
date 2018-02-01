package org.usfirst.frc.team6417.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Fridolin extends WPI_TalonSRX {
	
	private double maxSpeed = 1;
	
	public Fridolin(final int channel) {
	    super(channel);
	  }
	
	@Override
	public void set(double speed){
		super.set(maxSpeed*speed);
	}
	
	public void setMaxSpeed(double speed){
		maxSpeed = speed;
	}
}
