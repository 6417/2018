package org.usfirst.frc.team6417.robot.subsystems;

import org.usfirst.frc.team6417.robot.RobotMap;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * System responsible for controlling Pneumatics
 */
public class Pneumatics extends Subsystem {

	private Compressor compressor;
	private DoubleSolenoid solenoid;
	private boolean extended;

	/**
	 * Enables all Pneumatics class features, like extending or retracting
	 * pistons
	 */
	public Pneumatics() {
		compressor = new Compressor(RobotMap.PNEUMATICS.COMPRESSOR_PORT);

		solenoid = new DoubleSolenoid(RobotMap.PNEUMATICS.EXTEND_PORT, RobotMap.PNEUMATICS.RETRACT_PORT);

		this.retract();
	}

	/**
	 * Extends pneumatic pistons
	 */
	public void extend() {
		if (!extended) {
			solenoid.set(DoubleSolenoid.Value.kForward);
			extended = true;
		}
	}

	/**
	 * Retracts pneumatic pistons
	 */
	public void retract() {
		if (extended) {
			solenoid.set(DoubleSolenoid.Value.kReverse);
			extended = false;
		}
	}

	/**
	 * Tells you the status of the piston
	 * 
	 * @return extended
	 */
	public boolean isExtended() {
		return extended;
	}

	/**
	 * Gives you the possibility to activate compressor manually
	 */
	public void compress() {
		if ((compressor.getPressureSwitchValue()) && (!compressor.enabled())) {
			compressor.start();
		} else if (!compressor.getPressureSwitchValue()) {
			compressor.stop();
		}
	}

	public void initDefaultCommand() {

	}
}
