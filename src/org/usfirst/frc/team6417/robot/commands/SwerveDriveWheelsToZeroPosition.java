package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class SwerveDriveWheelsToZeroPosition extends Command {

	
	public SwerveDriveWheelsToZeroPosition() {
		requires(Robot.swerveDrive);
	}
	
	@Override
	protected void initialize() {
		SmartDashboard.putString("Swerve wheel state", "Zero Point Calibration");
		Robot.swerveDrive.startZeroPointCalibration();
	}
	
	@Override
	protected boolean isFinished() {
		return Robot.swerveDrive.isZeroPointCalibrationFinished();
	}

}
