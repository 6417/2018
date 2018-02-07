package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.repository.FieldInformationRepository;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class GetFieldInformation extends Command {
	private String gameData = "XXX";

	@Override
	protected void initialize() {
		gameData = DriverStation.getInstance().getGameSpecificMessage();		
		SmartDashboard.putString("Game Data", gameData);
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
