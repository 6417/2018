package org.usfirst.frc.team6417.robot.repository;

import org.usfirst.frc.team6417.robot.model.swerve.SwerveDriveAutonomousKinematics;
import org.usfirst.frc.team6417.robot.model.swerve.SwerveDriveAutonomousKinematics.GOAL_SIDE;
import org.usfirst.frc.team6417.robot.model.swerve.SwerveDriveAutonomousKinematics.POS_IN_STATION;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class GameStrategyRepository {
	private static GameStrategyRepository instance = null;
	
	public static GameStrategyRepository getInstance() {
		if(instance == null) {
			instance = new GameStrategyRepository();
		}
		return instance;		
	}

	private GOAL_SIDE selectedSwitchSideOption = GOAL_SIDE.UNKNOWN;
	
	public POS_IN_STATION getPositionInStation() {
		return POS_IN_STATION.values()[(int)SmartDashboard.getNumber("pos-in-station", SwerveDriveAutonomousKinematics.POS_IN_STATION.CENTER.ordinal())];
	}
	
	public boolean isNeedPushWithGripperAtSwitch() {
		switch(selectedSwitchSideOption) {
			case LEFT:
				return true;
			case STRAIGHT:
				return false;
			case RIGHT:
				return true;
			case UNKNOWN:
			default:
				return false;
		}
	}
	
	public GOAL_SIDE getGoalForRightOption() {
		int goalOrdinal = (int)SmartDashboard.getNumber("right-switch-side-option", SwerveDriveAutonomousKinematics.GOAL_SIDE.STRAIGHT.ordinal());
		try {
			return SwerveDriveAutonomousKinematics.GOAL_SIDE.values()[goalOrdinal];
		}catch(Throwable e) {
			return SwerveDriveAutonomousKinematics.GOAL_SIDE.UNKNOWN;
		}
	}


	public GOAL_SIDE getGoalForLeftOption() {
		int goalOrdinal = (int)SmartDashboard.getNumber("left-switch-side-option", SwerveDriveAutonomousKinematics.GOAL_SIDE.STRAIGHT.ordinal());
		try {
			return SwerveDriveAutonomousKinematics.GOAL_SIDE.values()[goalOrdinal];
		}catch(Throwable e) {
			return SwerveDriveAutonomousKinematics.GOAL_SIDE.UNKNOWN;
		}
	}

	public void setSelectedSwitchSideOption(GOAL_SIDE selectedSwitchSideOption) {
		System.out.println("GameStrategyRepository.setSelectedSwitchSideOption("+selectedSwitchSideOption+")");
		this.selectedSwitchSideOption  = selectedSwitchSideOption;
	}
	
	public GOAL_SIDE getSelectedSwitchSideOption() {
		return this.selectedSwitchSideOption;
	}
	
}
