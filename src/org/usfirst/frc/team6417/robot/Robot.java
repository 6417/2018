package org.usfirst.frc.team6417.robot;

import org.usfirst.frc.team6417.robot.commands.AutonomousBehavior;
import org.usfirst.frc.team6417.robot.model.powermanagement.Calibration;
import org.usfirst.frc.team6417.robot.model.powermanagement.PowerExtremals;
import org.usfirst.frc.team6417.robot.service.powermanagement.AdaptivePowerManagementStrategy;
import org.usfirst.frc.team6417.robot.service.powermanagement.PowerManagementStrategy;
import org.usfirst.frc.team6417.robot.service.powermanagement.PowerManager;
import org.usfirst.frc.team6417.robot.subsystems.Drive;
import org.usfirst.frc.team6417.robot.subsystems.Gripper;
import org.usfirst.frc.team6417.robot.subsystems.LiftingUnit;
import org.usfirst.frc.team6417.robot.subsystems.LoadingPlatform;
import org.usfirst.frc.team6417.robot.subsystems.NavX;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public class Robot extends TimedRobot {
	// Subsystems
	public static NavX navX;
	public static Gripper gripper;
	public static Drive drive;
	public static LoadingPlatform loadingPlatform;
	// Controllers
	public static OI oi;
	public static LiftingUnit liftingUnit;
	// Services
	public static PowerManager powerManager;
	private Command autonomousBehavior;

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	public void robotInit() {
		try {
			powerManager = new PowerManager();

			navX = new NavX();
			gripper = new Gripper();
			drive = new Drive();
			loadingPlatform = new LoadingPlatform();
			liftingUnit = new LiftingUnit();

			powerManager.addSubsystem(gripper, 
					createPMS(Calibration.PD.powerOfGripper, Calibration.PL.powerOfGripper));
			powerManager.addSubsystem(drive, 
					createPMS(Calibration.PD.powerOfDrive, Calibration.PL.powerOfDrive));
			powerManager.addSubsystem(loadingPlatform,
					createPMS(Calibration.PD.powerOfLiftingUnitWagon, Calibration.PL.powerOfLiftingUnitWagon));
			powerManager.addSubsystem(liftingUnit,
					createPMS(Calibration.PD.powerOfLiftingUnit, Calibration.PL.powerOfLiftingUnit));

			autonomousBehavior = new AutonomousBehavior();

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
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void robotPeriodic() {

	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
	}
}
