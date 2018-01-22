package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.repository.FieldInformationRepository;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

public final class GetFieldInformation extends Command {
	private String gameData = "XXX";

	@Override
	protected void initialize() {
		gameData = DriverStation.getInstance().getGameSpecificMessage();
	}

	@Override
	protected void execute() {
		FieldInformationRepository.getInstance().setFieldInfo(gameData);
	}
	
	@Override
	protected boolean isFinished() {
		return true;
	}

}
