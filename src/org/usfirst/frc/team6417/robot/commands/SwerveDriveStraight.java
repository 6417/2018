package org.usfirst.frc.team6417.robot.commands;

import org.usfirst.frc.team6417.robot.Robot;
import org.usfirst.frc.team6417.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class SwerveDriveStraight extends Command {
	private int distanceInTicks;
	
	public SwerveDriveStraight(int distanceInTicks) {
		this.distanceInTicks = distanceInTicks;
		requires(Robot.swerveDrive);
	}
	
	@Override
	protected void initialize() {
//		Robot.navX.get().reset();		
//		Robot.navX.get().setPIDSourceType(PIDSourceType.kDisplacement);
//		Robot.navX.get().resetDisplacement();
		
//		startTimestamp = System.currentTimeMillis();
//		System.out.println("SwerveDriveStraight.execute ...");
		
		Robot.swerveDrive.driveParallel(0.5, 0);
	}
	
	@Override
	protected void execute() {
		SmartDashboard.putNumber("NavX.Angle", Robot.navX.get().getAngle());		
		SmartDashboard.putNumber("NavX.compass", Robot.navX.get().getCompassHeading());		
		SmartDashboard.putNumber("NavX.DispX", Robot.navX.get().getDisplacementY());		
////		System.out.println("SwerveDriveStraight.execute("+(System.currentTimeMillis() - startTimestamp)+")");
//		if((System.currentTimeMillis() - startTimestamp) > 30000) {
//			Robot.swerveDrive.driveParallel(0, 0);
////			System.out.println("SwerveDriveStraight.execute done");
//			isFinished = true;
//		} else {
//			Robot.swerveDrive.driveParallel(0.5,0);// -Robot.navX.get().getDisplacementX());
////			System.out.println("SwerveDriveStraight.execute. Correcting displacement "+(-Robot.navX.get().getDisplacementX()));
//		}
	}	
	
	@Override
	protected boolean isFinished() {
		if(Robot.swerveDrive.frontLeft.velocityMotor.getSelectedSensorPosition(0) >= distanceInTicks) {
			Robot.swerveDrive.frontLeft.velocityMotor.set(RobotMap.VELOCITY.STOP_VELOCITY);
			return true;
		}
		return false;
	}

}
