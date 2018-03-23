package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;
import org.usfirst.frc.team6417.robot.RobotMap;
import org.usfirst.frc.team6417.robot.model.swerve.SwerveDriveAutonomousKinematics;
import org.usfirst.frc.team6417.robot.model.swerve.SwerveDriveAutonomousKinematics.GOAL_SIDE;
import org.usfirst.frc.team6417.robot.model.swerve.SwerveDriveAutonomousKinematics.POS_IN_STATION;
import org.usfirst.frc.team6417.robot.repository.FieldInformationRepository;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class SverveDriveToSwitchAutonomous extends Command {
	private final SwerveDriveAutonomousKinematics kinematics = new SwerveDriveAutonomousKinematics();
	private double velocity = 0.2;
	private int distanceInTicks = 100000;

	private boolean isFinished = false;

	public SverveDriveToSwitchAutonomous() {
		requires(Robot.swerveDrive);
		SmartDashboard.putNumber("pos-in-station", POS_IN_STATION.RIGHT_SIDE.ordinal());
	}
	
	
	@Override
	protected void initialize() {
		isFinished = false;
		
		SwerveDriveAutonomousKinematics.GOAL_SIDE selectedSwitchSideOption;
		// This is the pilot's selection of the goal depending on the selected side of the switch by the FIS
		SwerveDriveAutonomousKinematics.GOAL_SIDE leftSwitchSideOption = getGoalForLeftOption(); 
		SwerveDriveAutonomousKinematics.GOAL_SIDE rightSwitchSideOption = getGoalForRightOption(); 
		
		if(FieldInformationRepository.getInstance().isFirstSwitchLeft()) {
			selectedSwitchSideOption = leftSwitchSideOption;
		}else {
			selectedSwitchSideOption = rightSwitchSideOption;
		}
		
		selectedSwitchSideOption = SwerveDriveAutonomousKinematics.GOAL_SIDE.RIGHT;
		
		SwerveDriveAutonomousKinematics.POS_IN_STATION actualPosInStation = SwerveDriveAutonomousKinematics.POS_IN_STATION.CENTER;// posInStation; //POS_IN_STATION.values()[(int)SmartDashboard.getNumber("pos-in-station", posInStation.ordinal())];
		double omega = kinematics.calculateAngle(actualPosInStation, selectedSwitchSideOption);
		distanceInTicks = kinematics.calculateDistanceInTicks(actualPosInStation, selectedSwitchSideOption); 
		Robot.swerveDrive.drive(velocity, omega, 0);
	}

	private GOAL_SIDE getGoalForRightOption() {
		int goalOrdinal = (int)SmartDashboard.getNumber("OPTION-RIGHT", SwerveDriveAutonomousKinematics.GOAL_SIDE.RIGHT.ordinal());
		try {
			return SwerveDriveAutonomousKinematics.GOAL_SIDE.values()[goalOrdinal];
		}catch(Throwable e) {
			return SwerveDriveAutonomousKinematics.GOAL_SIDE.UNKNOWN;
		}
	}


	private GOAL_SIDE getGoalForLeftOption() {
		int goalOrdinal = (int)SmartDashboard.getNumber("OPTION-LEFT", SwerveDriveAutonomousKinematics.GOAL_SIDE.LEFT.ordinal());
		try {
			return SwerveDriveAutonomousKinematics.GOAL_SIDE.values()[goalOrdinal];
		}catch(Throwable e) {
			return SwerveDriveAutonomousKinematics.GOAL_SIDE.UNKNOWN;
		}
	}


	@Override
	protected void execute() {
		if(Robot.swerveDrive.frontRight.velocityMotor.getSelectedSensorPosition(0) >= distanceInTicks) {
			Robot.swerveDrive.frontLeft.velocityMotor.set(RobotMap.VELOCITY.STOP_VELOCITY);
			Robot.swerveDrive.frontRight.velocityMotor.set(RobotMap.VELOCITY.STOP_VELOCITY);
			Robot.swerveDrive.backLeft.velocityMotor.set(RobotMap.VELOCITY.STOP_VELOCITY);
			Robot.swerveDrive.backRight.velocityMotor.set(RobotMap.VELOCITY.STOP_VELOCITY);
			isFinished = true;
		}
	}

	@Override
	protected boolean isFinished() {
		return isFinished;
	}



}
