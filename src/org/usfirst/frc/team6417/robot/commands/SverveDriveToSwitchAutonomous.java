package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;
import org.usfirst.frc.team6417.robot.RobotMap;
import org.usfirst.frc.team6417.robot.model.swerve.SwerveDriveAutonomousKinematics;
import org.usfirst.frc.team6417.robot.model.swerve.SwerveDriveAutonomousKinematics.GOAL_SIDE;
import org.usfirst.frc.team6417.robot.model.swerve.SwerveDriveAutonomousKinematics.POS_IN_STATION;
import org.usfirst.frc.team6417.robot.repository.GameStrategyRepository;

import edu.wpi.first.wpilibj.command.Command;

public final class SverveDriveToSwitchAutonomous extends Command {
	private final SwerveDriveAutonomousKinematics kinematics = new SwerveDriveAutonomousKinematics();
	private boolean hasAlreadyExecuted = false;
	private double velocity = 0.2;
	private int distanceInTicks = 100000;

	private boolean isFinished = false;

	public SverveDriveToSwitchAutonomous() {
		requires(Robot.swerveDrive);
	}
	
	
	protected void calculateKinematics() {
		if(hasAlreadyExecuted) {
//			System.out.println("SverveDriveToSwitchAutonomous.calculateKinematics(already executed)");
			return;
		}
		isFinished = false;
		hasAlreadyExecuted = true;
		
//		SwerveDriveAutonomousKinematics.GOAL_SIDE selectedSwitchSideOption;
//		// This is the pilot's selection of the goal depending on the selected side of the switch by the FIS
//		SwerveDriveAutonomousKinematics.GOAL_SIDE leftSwitchSideOption = GameStrategyRepository.getInstance().getGoalForLeftOption(); 
//		SwerveDriveAutonomousKinematics.GOAL_SIDE rightSwitchSideOption = GameStrategyRepository.getInstance().getGoalForRightOption(); 
//
//		if(FieldInformationRepository.getInstance().isFirstSwitchLeft()) {
//			selectedSwitchSideOption = leftSwitchSideOption;
//		}else {
//			selectedSwitchSideOption = rightSwitchSideOption;
//		}
		GOAL_SIDE selectedSwitchSideOption = GameStrategyRepository.getInstance().getSelectedSwitchSideOption();
		POS_IN_STATION actualPosInStation = GameStrategyRepository.getInstance().getPositionInStation(); 

System.out.println("SverveDriveToSwitchAutonomous.calculateKinematics(station:"+actualPosInStation+" -> goal:"+selectedSwitchSideOption+")");
		double omega = kinematics.calculateAngle(actualPosInStation, selectedSwitchSideOption);
		distanceInTicks = kinematics.calculateDistanceInTicks(actualPosInStation, selectedSwitchSideOption);
		distanceInTicks += Robot.swerveDrive.frontRight.velocityMotor.getSelectedSensorPosition(0);
		Robot.swerveDrive.drive(velocity, omega, 0);
		System.out.println("SverveDriveToSwitchAutonomous.calculateKinematics()");
	}
	
	@Override
	protected void execute() {
		calculateKinematics();
		
		if(Robot.swerveDrive.frontRight.velocityMotor.getSelectedSensorPosition(0) >= distanceInTicks) {
			Robot.swerveDrive.frontLeft.velocityMotor.set(RobotMap.VELOCITY.STOP_VELOCITY);
			Robot.swerveDrive.frontRight.velocityMotor.set(RobotMap.VELOCITY.STOP_VELOCITY);
			Robot.swerveDrive.backLeft.velocityMotor.set(RobotMap.VELOCITY.STOP_VELOCITY);
			Robot.swerveDrive.backRight.velocityMotor.set(RobotMap.VELOCITY.STOP_VELOCITY);
			Robot.swerveDrive.gotoAngle(0);
			isFinished = true;
		}
	}

	@Override
	protected boolean isFinished() {
		return isFinished && Robot.swerveDrive.isAnglesOnTarget();
	}



}
