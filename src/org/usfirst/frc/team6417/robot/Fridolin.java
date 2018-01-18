package org.usfirst.frc.team6417.robot;

import edu.wpi.first.wpilibj.Talon;

public class Fridolin extends Talon {
	
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
