package org.usfirst.frc.team6417.robot;

import org.usfirst.frc.team6417.robot.commands.AutonomousBehavior;
import org.usfirst.frc.team6417.robot.commands.CalibrationBehavior;
import org.usfirst.frc.team6417.robot.commands.TestBehavior;
import org.usfirst.frc.team6417.robot.model.powermanagement.Calibration;
import org.usfirst.frc.team6417.robot.model.powermanagement.PowerExtremals;
import org.usfirst.frc.team6417.robot.service.powermanagement.AdaptivePowerManagementStrategy;
import org.usfirst.frc.team6417.robot.service.powermanagement.PowerManagementStrategy;
import org.usfirst.frc.team6417.robot.subsystems.Drive;
import org.usfirst.frc.team6417.robot.subsystems.Gripper;
import org.usfirst.frc.team6417.robot.subsystems.LiftingUnit;
import org.usfirst.frc.team6417.robot.subsystems.LiftingUnitWagon;
import org.usfirst.frc.team6417.robot.subsystems.NavX;
import org.usfirst.frc.team6417.robot.subsystems.SwerveDrive;
import org.usfirst.frc.team6417.robot.subsystems.SwerveWheelDrive;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public class Robot extends TimedRobot {
	// Subsystems
	public static NavX navX;
	public static Gripper gripper;
	public static Drive drive;
	public static LiftingUnitWagon liftingUnitWagon;	
	public static SwerveDrive swerveDrive;
	
	// Controllers
	public static OI oi;
	public static LiftingUnit liftingUnit;
	// Services
	
	private Command autonomousBehavior;
	private Command testBehavior;
	private Command calibrationBehavior;
	
	public static SwerveWheelDrive swerveDriveWheel;

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	public void robotInit() {
		try {
			
			navX = new NavX();
			
			if(RobotMap.SUBSYSTEM.IS_GRIPPER_IN_USE) {
				gripper = new Gripper(createPMS(Calibration.PD.powerOfGripper, Calibration.PL.powerOfGripper));
			}
			if(RobotMap.SUBSYSTEM.IS_LIFTING_UNIT_WAGON_IN_USE) {
				liftingUnitWagon = new LiftingUnitWagon(createPMS(Calibration.PD.powerOfLiftingUnitWagon, Calibration.PL.powerOfLiftingUnitWagon));
			}
			if(RobotMap.SUBSYSTEM.IS_LIFTING_UNIT_IN_USE) {
				liftingUnit = new LiftingUnit(createPMS(Calibration.PD.powerOfLiftingUnit, Calibration.PL.powerOfLiftingUnit));
			}
			if(RobotMap.SUBSYSTEM.IS_DIFFERENTIAL_DRIVE_IN_USE) {
				drive = new Drive(createPMS(Calibration.PD.powerOfDrive, Calibration.PL.powerOfDrive));
			}
			if(RobotMap.SUBSYSTEM.IS_SWERVE_DRIVE_IN_USE) {
				swerveDrive = new SwerveDrive();
			}
			if(RobotMap.SUBSYSTEM.IS_SWERVE_WHEEL_IN_USE) {
				swerveDriveWheel = new SwerveWheelDrive("BL",
														RobotMap.MOTOR.DRIVE_BACK_LEFT_ANGLE_PORT, 
														RobotMap.MOTOR.DRIVE_BACK_LEFT_VELOCITY_PORT,
														RobotMap.AIO.DRIVE_BACK_LEFT_POSITION_SENSOR_PORT);
			}
			if(RobotMap.SUBSYSTEM.IS_CAMERA_IN_USE) {
				//CameraServer.getInstance().startAutomaticCapture();
			}


			
//			
//			
//
//			powerManager.addSubsystem(gripper, 
//					createPMS(Calibration.PD.powerOfGripper, Calibration.PL.powerOfGripper));
//			powerManager.addSubsystem(drive, 
//					createPMS(Calibration.PD.powerOfDrive, Calibration.PL.powerOfDrive));
//			powerManager.addSubsystem(liftingUnitWagon,
//					);
//			powerManager.addSubsystem(liftingUnit,
//					createPMS(Calibration.PD.powerOfLiftingUnit, Calibration.PL.powerOfLiftingUnit));

			autonomousBehavior = new AutonomousBehavior();
			testBehavior = new TestBehavior();
			calibrationBehavior = new CalibrationBehavior();
			
			oi = OI.getInstance();
			
			
		} catch (Throwable e) {
			DriverStation.reportError(e.getMessage(), e.getStackTrace());
		}
	}

	/**
	 * Please make shure to use PD value as fist parameter and PL value as second.
	 * @param pd
	 * @param pl
	 * @return Configured {@link PowerManagementStrategy} instance
	 */
	private PowerManagementStrategy createPMS(final double pd, final double pl) {
		return new AdaptivePowerManagementStrategy(new PowerExtremals(pd, pl));
	}

	/**
	 * 
	 * This function is called once each time the robot enters Disabled mode. You
	 * can use it to reset any subsystem information you want to clear when the
	 * robot is disabled.
	 */
	@Override
	public void disabledInit() {
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable chooser
	 * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
	 * remove all of the chooser code and uncomment the getString code to get the
	 * auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons to
	 * the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		autonomousBehavior.start();
//		calibrationBehavior.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// nothing to do since all pilot's commands are handled via event-listeners in the OI class.
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
//		SmartDashboard.putNumber("Position-Sensor A "+
//		Robot.swerveDrive.backLeft.positionSensor0.getChannel(), 
//		Robot.swerveDrive.backLeft.positionSensor0.getValue());
	}

	@Override
	public void robotPeriodic() {

	}
	
	@Override
	public void testInit() {
		System.out.println("Robot.testInit()");
//		calibrationBehavior.start();
//		testBehavior.start();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		Scheduler.getInstance().run();
	}
}
