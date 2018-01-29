
package org.usfirst.frc.team6417.robot;



import org.usfirst.frc.team6417.robot.commands.AutonomousBehavior;
import org.usfirst.frc.team6417.robot.service.powermanagement.PowerManagementService;
import org.usfirst.frc.team6417.robot.service.powermanagement.SimplePowerManagementService;
import org.usfirst.frc.team6417.robot.subsystems.Drive;
import org.usfirst.frc.team6417.robot.subsystems.Gripper;
import org.usfirst.frc.team6417.robot.subsystems.LiftingUnit;
import org.usfirst.frc.team6417.robot.subsystems.LoadingPlatform;
import org.usfirst.frc.team6417.robot.subsystems.NavX;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public class Robot extends IterativeRobot {
	// Subsystems
	public static NavX navX;
	public static Gripper gripper;
	public static Drive drive;
	public static LoadingPlatform loadingPlatform;
	// Controllers
	public static OI oi;
	public static LiftingUnit liftingUnit;
	// Services
	public static PowerManagementService powerManagementService;
	private Command autonomousBehavior;


	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		try {
			powerManagementService = new SimplePowerManagementService();
			
			navX = new NavX();
			gripper = new Gripper(powerManagementService);
			drive = new Drive(powerManagementService);
			loadingPlatform = new LoadingPlatform(powerManagementService);
			liftingUnit = new LiftingUnit(powerManagementService);
			
			autonomousBehavior = new AutonomousBehavior();

			oi = OI.getInstance();
		} catch (Throwable e) {
			DriverStation.reportError(e.getMessage(),e.getStackTrace());			
		}
	}

	/**
	 * 
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
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
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
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
