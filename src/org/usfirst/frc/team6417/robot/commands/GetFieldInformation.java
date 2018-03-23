package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.repository.FieldInformationRepository;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

public final class GetFieldInformation extends Command {

	@Override
	protected void execute() {
		final String gameData = DriverStation.getInstance().getGameSpecificMessage();		
		FieldInformationRepository.getInstance().setFieldInfo(gameData);
	}
	
	@Override
	protected boolean isFinished() {
		return true;
	}

}
