package org.usfirst.frc.team6417.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.command.Subsystem;
import com.kauailabs.navx.frc.AHRS;

/**
 * This class activates the NavX Sensor and makes it available to use
 */
public class NavX extends Subsystem {

	private AHRS ahrs;

	public NavX() {
		try {
			ahrs = new AHRS(SerialPort.Port.kMXP);
		} catch (RuntimeException ex) {
			DriverStation.reportError("Error instantiating navX MXP: " + ex.getMessage(), true);
		}
	}

	public AHRS get() {
		return ahrs;
	}

	public void reset() {
		ahrs.reset();
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}
}
