package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.RobotMap;

import edu.wpi.first.wpilibj.command.CommandGroup;

public final class TestBehavior extends CommandGroup {
	public TestBehavior() {
		addSequential(new GetFieldInformation());
		
		if(RobotMap.SUBSYSTEM.IS_LIFTING_UNIT_IN_USE) {
			addSequential(new LiftingUnitFindEndpointDown());
		}
		

//		if(RobotMap.SUBSYSTEM.IS_SWERVE_DRIVE_IN_USE) {
//			addSequential(new SwerveDriveWheelsToZeroPosition());
//		}
//		addSequential(new SwerveDriveStraight());
		
//		addSequential(new Command() {
//
//			@Override
//			protected void initialize() {
//				Robot.liftingUnit.reset();
//			}
//
//			@Override
//			protected boolean isFinished() {
//				return true;
//			}
//		});
//		addSequential(new Command() {
//
//			@Override
//			protected void initialize() {
//				Robot.liftingUnit.setSetpoint(1000);
//			}
//
//			@Override
//			protected boolean isFinished() {
//				return Robot.liftingUnit.onTarget();
//			}
//		});
//		addSequential(new Command() {
//
//			@Override
//			protected void initialize() {
//				Robot.liftingUnit.setSetpoint(0);
//			}
//
//			@Override
//			protected boolean isFinished() {
//				return Robot.liftingUnit.onTarget();
//			}
//		});
//		addSequential(new GripperPull());
//		addSequential(new GripperStop());
//		addSequential(new GripperPush());
//		addSequential(new GripperStop());
	}
}
