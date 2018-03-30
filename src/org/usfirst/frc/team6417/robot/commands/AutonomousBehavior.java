package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.RobotMap;
import org.usfirst.frc.team6417.robot.subsystems.Gripper;
import org.usfirst.frc.team6417.robot.subsystems.LiftingUnitWagon;

import edu.wpi.first.wpilibj.command.CommandGroup;

public final class AutonomousBehavior extends CommandGroup {
	
	 public AutonomousBehavior() {
	    	addSequential(new GetFieldInformation());
	    	addSequential(new DefineGameStrategy());
	    	if(RobotMap.SUBSYSTEM.IS_LIFTING_UNIT_IN_USE && RobotMap.SUBSYSTEM.IS_LIFTING_UNIT_WAGON_IN_USE) {
	    		addSequential(new LiftingUnitWagonGameStart());
	    	}	    	
	    	if(RobotMap.SUBSYSTEM.IS_SWERVE_DRIVE_IN_USE) {
		    	addSequential(new SwerveDriveResetVelocityEncoder());
	    	}	    	
	    	if(RobotMap.SUBSYSTEM.IS_LIFTING_UNIT_IN_USE) {
		    	addParallel(new LiftingUnitMoveToSavePosition());
	    		addSequential(new LiftingUnitMoveToPosition(RobotMap.ROBOT.LIFTING_UNIT_SWITCH_ALTITUDE_IN_TICKS));
	    	}
	    	if(RobotMap.SUBSYSTEM.IS_LIFTING_UNIT_WAGON_IN_USE) {
	    		addSequential(new LiftingUnitWagonMove(LiftingUnitWagon.FRONT));
	    	}
	    	if(RobotMap.SUBSYSTEM.IS_SWERVE_DRIVE_IN_USE) {
		    	addSequential(new SverveDriveToSwitchAutonomous());
		    	addSequential(new SwerveDriveStraight(3000, true));
	    	}
	    	if(RobotMap.SUBSYSTEM.IS_GRIPPER_IN_USE) {
	    		addSequential(new GripperMoveTimed(2500, Gripper.PUSH));
	    	}
	    	if(RobotMap.SUBSYSTEM.IS_SWERVE_DRIVE_IN_USE) {
//		    	addSequential(new SwerveDriveStraight(3000, false));
//		    	addSequential(new SwerveDriveStraight(1000, true));
	    	}
	 }
}
