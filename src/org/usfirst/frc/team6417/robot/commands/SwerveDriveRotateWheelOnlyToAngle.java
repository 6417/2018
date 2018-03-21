package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;
import org.usfirst.frc.team6417.robot.repository.FieldInformationRepository;

import edu.wpi.first.wpilibj.command.Command;

public final class SwerveDriveRotateWheelOnlyToAngle extends Command {
	public SwerveDriveRotateWheelOnlyToAngle() {
		requires(Robot.swerveDrive);
	}
	
	@Override
	protected void initialize() {
		if(FieldInformationRepository.getInstance().isFirstSwitchLeft()) {
			Robot.swerveDrive.drive(0, -1, 0);
		}else {
			Robot.swerveDrive.drive(0, 1, 0);
		}
	}
	
	@Override
	protected boolean isFinished() {
		return Robot.swerveDrive.isAnglesOnTarget();
	}

}
