package org.usfirst.frc.team6417.robot;

import org.usfirst.frc.team6417.robot.commands.GripperPull;
import org.usfirst.frc.team6417.robot.commands.GripperPush;
import org.usfirst.frc.team6417.robot.commands.GripperStop;
import org.usfirst.frc.team6417.robot.commands.LiftingUnitWagonMove;
import org.usfirst.frc.team6417.robot.commands.SwerveDriveWheelAngleCalibration;
import org.usfirst.frc.team6417.robot.subsystems.LiftingUnitWagon;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {	
	public final Joystick joystickOne = new Joystick(RobotMap.CONTROLLER.DIRECTION_CONTROLLER);
//	public final Joystick liftingUnitController = new Joystick(RobotMap.CONTROLLER.LIFTING_UNIT_CONTROLLER);
	
	private JoystickButton gripperPullButton;
	private JoystickButton gripperPushButton;
	
	private JoystickButton liftingUnitToGroundAltitudeButton;
	private JoystickButton liftingUnitToExchangeAltitudeButton;
	private JoystickButton liftingUnitToSwitchAltitudeButton;
	private JoystickButton liftingUnitToScaleLowAltitudeButton;
	private JoystickButton liftingUnitToScaleMiddleAltitudeButton;
	private JoystickButton liftingUnitToScaleHighAltitudeButton;
	private JoystickButton liftingUnitResetButton;
	private JoystickButton swerveWheelForwardButton;
	private JoystickButton liftingUnitWagonForwardButton;

	private JoystickButton liftingUnitWagonBackwardButton;

	private static OI INSTANCE;
	
	public static OI getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new OI();
		}
		return INSTANCE;
	}
	
	private OI() {
		if(RobotMap.SUBSYSTEM.IS_GRIPPER_IN_USE) {
			gripperPushButton = new JoystickButton(joystickOne, 1);
			gripperPullButton = new JoystickButton(joystickOne, 2);

			gripperPullButton.whenPressed(new GripperPull());
			gripperPullButton.whenReleased(new GripperStop());
			gripperPushButton.whenPressed(new GripperPush());
			gripperPushButton.whenReleased(new GripperStop());
		}
		if(RobotMap.SUBSYSTEM.IS_LIFTING_UNIT_WAGON_IN_USE) {
			liftingUnitWagonForwardButton = new JoystickButton(joystickOne, 11);
			liftingUnitWagonBackwardButton = new JoystickButton(joystickOne, 12);
			
			liftingUnitWagonForwardButton.whenPressed(new LiftingUnitWagonMove(LiftingUnitWagon.FRONT));
			liftingUnitWagonBackwardButton.whenPressed(new LiftingUnitWagonMove(LiftingUnitWagon.BACK));
		}
		if(RobotMap.SUBSYSTEM.IS_LIFTING_UNIT_IN_USE) {
//			liftingUnitToGroundAltitudeButton = new JoystickButton(liftingUnitController, 1);
//			liftingUnitToExchangeAltitudeButton = new JoystickButton(liftingUnitController, 2);
//			liftingUnitToSwitchAltitudeButton = new JoystickButton(liftingUnitController, 3);
//			liftingUnitToScaleLowAltitudeButton = new JoystickButton(liftingUnitController, 4);
//			liftingUnitToScaleMiddleAltitudeButton = new JoystickButton(liftingUnitController, 5);
//			liftingUnitToScaleHighAltitudeButton = new JoystickButton(liftingUnitController, 6);
//			liftingUnitResetButton = new JoystickButton(joystickOne, 10);
//
//			liftingUnitToGroundAltitudeButton.whenPressed(new LiftingUnitMove(LiftingUnit.TO_GROUND));
//			liftingUnitToSwitchAltitudeButton.whenPressed(new LiftingUnitMove(LiftingUnit.TO_SWITCH));
//			liftingUnitToExchangeAltitudeButton.whenPressed(new LiftingUnitMove(LiftingUnit.TO_EXCHANGE));
//			liftingUnitToScaleLowAltitudeButton.whenPressed(new LiftingUnitMove(LiftingUnit.TO_SCALE_LOW));
//			liftingUnitToScaleMiddleAltitudeButton.whenPressed(new LiftingUnitMove(LiftingUnit.TO_SCALE_MIDDLE));
//			liftingUnitToScaleHighAltitudeButton.whenPressed(new LiftingUnitMove(LiftingUnit.TO_SCALE_HIGH));
//			liftingUnitResetButton.whenPressed(new LiftingUnitReset());
		}
		if(RobotMap.SUBSYSTEM.IS_DIFFERENTIAL_DRIVE_IN_USE) {
		// nothing to do. the drive uses the internal default command.
		}
		if(RobotMap.SUBSYSTEM.IS_SWERVE_DRIVE_IN_USE) {
			// nothing to do. the drive uses the internal default command.
			swerveWheelForwardButton = new JoystickButton(joystickOne, 10);
			swerveWheelForwardButton.whenPressed(new SwerveDriveWheelAngleCalibration());
		}
		if(RobotMap.SUBSYSTEM.IS_SWERVE_WHEEL_IN_USE) {
//			swerveWheelForwardButton = new JoystickButton(joystickOne, 2);
//			swerveWheelForwardButton.whenPressed(new SwerveDriveWheelsToZeroPosition());
			
		}

	}
	
}
