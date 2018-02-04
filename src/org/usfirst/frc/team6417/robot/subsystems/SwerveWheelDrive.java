package org.usfirst.frc.team6417.robot.subsystems;

import org.usfirst.frc.team6417.robot.Fridolin;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Implementation of the wheel of a swerve drive
 * @author https://jacobmisirian.gitbooks.io/frc-swerve-drive-programming/content/part-2-driving.html
 */
public final class SwerveWheelDrive extends Subsystem {
	private Fridolin angleMotor;
	private Fridolin speedMotor;
	private PIDController pidController;
	
	private final double MAX_VOLTS = 4.95;
	
	public SwerveWheelDrive (int angleMotor, int speedMotor, int encoder) {
	    this.angleMotor = new Fridolin (angleMotor);
	    this.speedMotor = new Fridolin (speedMotor);
	    pidController = new PIDController (1, 0, 0, new AnalogInput (encoder), this.angleMotor);

	    pidController.setOutputRange (-1, 1);
	    pidController.setContinuous ();
	    pidController.enable ();
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}

	public void drive (double speed, double angle) {
		speedMotor.set (speed);

	    double setpoint = angle * (MAX_VOLTS * 0.5) + (MAX_VOLTS * 0.5); // Optimization offset can be calculated here.
	    if (setpoint < 0) {
	        setpoint = MAX_VOLTS + setpoint;
	    }
	    if (setpoint > MAX_VOLTS) {
	        setpoint = setpoint - MAX_VOLTS;
	    }

	    pidController.setSetpoint (setpoint);
	}
}
