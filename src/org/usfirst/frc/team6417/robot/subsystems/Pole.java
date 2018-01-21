package org.usfirst.frc.team6417.robot.subsystems;

import org.usfirst.frc.team6417.robot.Fridolin;
import org.usfirst.frc.team6417.robot.RobotMap;
import org.usfirst.frc.team6417.robot.Util;
import org.usfirst.frc.team6417.robot.commands.PoleResetAltitude;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The Pole is able to move up and down the Gripper
 */
public final class Pole extends Subsystem {
	private static final double UP_VELOCITY = 0.23;
	private static final double DOWN_VELOCITY = -0.3;
	private static final double STOP_VELOCITY = 0;
	private static final int SWITCH_ALTITUDE = 10;
	private static final int GROUND_ALTITUDE = 0;
	private static final int ALTITUDE_TOLERANCE = 2;
	
	private final Fridolin motor;
	private final Encoder altimeter;
	
	public Pole() {
		motor = new Fridolin(RobotMap.MOTOR.POLE_PORT);
		
		altimeter = new Encoder(RobotMap.ENCODER.POLE_UP_PORT_A,RobotMap.ENCODER.POLE_UP_PORT_B);
		altimeter.setDistancePerPulse(RobotMap.ROBOT.DIST_PER_PULSE);
		altimeter.setReverseDirection(true);
	}

	public void moveUp() {
		motor.set(UP_VELOCITY);
	}
	
	public void moveDown() {
		motor.set(DOWN_VELOCITY);
	}
	
	public void stop() {
		motor.set(STOP_VELOCITY);
	}
	
	public void resetEncoders() {
		altimeter.reset();
	}

	@Override
	protected void initDefaultCommand() {
		// TODO What should the pole do at initialisation?
		setDefaultCommand(new PoleResetAltitude());
	}

	public boolean isOnSwitchAltitude() {
		return isOnAltitude(SWITCH_ALTITUDE);
	}
	
	public boolean isOnAltitude(int level) {
		int currentAltitude = altimeter.get();
		boolean isAltitudeReached = Util.inRange(currentAltitude, 
				level - ALTITUDE_TOLERANCE, 
				level + ALTITUDE_TOLERANCE);
		SmartDashboard.putNumber("Pole: Altitude", currentAltitude);
		SmartDashboard.putBoolean("Pole: Is on level", isAltitudeReached);
		SmartDashboard.putBoolean("Pole: Is below level", currentAltitude < level);
		SmartDashboard.putBoolean("Pole: Is above level", currentAltitude > level);
		return isAltitudeReached;
	}

	public boolean isOnGroundAltitude() {
		return isOnAltitude(GROUND_ALTITUDE);
	}	
	
}
