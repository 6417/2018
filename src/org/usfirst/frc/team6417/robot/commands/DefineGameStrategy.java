package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.model.swerve.SwerveDriveAutonomousKinematics;
import org.usfirst.frc.team6417.robot.repository.FieldInformationRepository;
import org.usfirst.frc.team6417.robot.repository.GameStrategyRepository;

import edu.wpi.first.wpilibj.command.Command;

public final class DefineGameStrategy extends Command {

	@Override
	protected void execute() {
		SwerveDriveAutonomousKinematics.GOAL_SIDE selectedSwitchSideOption = SwerveDriveAutonomousKinematics.GOAL_SIDE.STRAIGHT;
		// This is the pilot's selection of the goal depending on the selected side of the switch by the FIS
		switch(FieldInformationRepository.getInstance().getFirstSwitchSideOfAlliance()) {
			case LEFT:
				selectedSwitchSideOption = GameStrategyRepository.getInstance().getGoalForLeftOption();
				break;
			case RIGHT:
				selectedSwitchSideOption = GameStrategyRepository.getInstance().getGoalForRightOption();
				break;
			case UNKNOWN:
			default:
		}
		
		GameStrategyRepository.getInstance().setSelectedSwitchSideOption(selectedSwitchSideOption);
	}
	
	@Override
	protected boolean isFinished() {
		return true;
	}

}
