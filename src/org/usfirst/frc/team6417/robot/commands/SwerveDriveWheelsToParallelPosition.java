package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class SwerveDriveWheelsToParallelPosition extends Command {

	
	public SwerveDriveWheelsToParallelPosition() {
		requires(Robot.swerveDrive);
	}
	
	@Override
	protected void initialize() {
		SmartDashboard.putString("Swerve wheel state", "Angle correction");
		Robot.swerveDrive.startParallelCalibration();
	}
	
	@Override
	protected boolean isFinished() {
		return Robot.swerveDrive.isParallelCalibration();
	}

}
