package org.usfirst.frc.team6417.robot;

import org.usfirst.frc.team6417.robot.commands.GripperPull;
import org.usfirst.frc.team6417.robot.commands.GripperPush;
import org.usfirst.frc.team6417.robot.commands.GripperStop;
import org.usfirst.frc.team6417.robot.commands.LiftingUnitFindEndpointDown;
import org.usfirst.frc.team6417.robot.commands.LiftingUnitHoldPosition;
import org.usfirst.frc.team6417.robot.commands.LiftingUnitMoveToPosition;
import org.usfirst.frc.team6417.robot.commands.LiftingUnitWagonFindEndpointFront;
import org.usfirst.frc.team6417.robot.commands.SwerveDriveAngleOnSingleWheel;
import org.usfirst.frc.team6417.robot.commands.SwerveDriveMecanumSimilarTeleoperated;
import org.usfirst.frc.team6417.robot.commands.SwerveDriveParallelTeleoperated;
import org.usfirst.frc.team6417.robot.commands.SwerveDriveSetPosToZero;
import org.usfirst.frc.team6417.robot.commands.SwerveDriveTeleoperated;
import org.usfirst.frc.team6417.robot.commands.SwerveDriveWheelStop;
import org.usfirst.frc.team6417.robot.commands.SwerveDriveWheelTeleoperated;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {	
	public final Joystick joystickOne = new Joystick(RobotMap.CONTROLLER.DIRECTION_CONTROLLER);
	public final Joystick liftingUnitController = new Joystick(RobotMap.CONTROLLER.LIFTING_UNIT_CONTROLLER);
	
	private JoystickButton gripperPullButton;
	private JoystickButton gripperPushButton;
	private JoystickButton swerveWheelForwardButton;
	private JoystickButton liftingUnitWagonForwardButton;
	private JoystickButton liftingUnitWagonBackwardButton;
	private JoystickButton swerveWheelCheckButton;
	private JoystickButton liftingUnitHoldPositionButton;
	private JoystickButton liftingUnitGoToSwitchButton;
	private JoystickButton liftingUnitGoToGroundButton;
	private JoystickButton liftingUnitResetButton;
	private JoystickButton liftingUnitFindEndpointDownButton;
	private JoystickButton liftingUnitTeleoperated;
	private JoystickButton swerveDriveTeleopButton;
	private JoystickButton swerveDriveParallelTeleopButton;
	private JoystickButton swerveDriveMecanumSimilarTeleopButton;

	private static OI INSTANCE;
	
	public static OI getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new OI();
		}
		return INSTANCE;
	}
	
	private OI() {
		if(RobotMap.SUBSYSTEM.IS_GRIPPER_IN_USE) {
			gripperPushButton = new JoystickButton(liftingUnitController, 7);
			gripperPullButton = new JoystickButton(liftingUnitController, 8);

			gripperPullButton.whenPressed(new GripperPull());
			gripperPullButton.whenReleased(new GripperStop());
			gripperPushButton.whenPressed(new GripperPush());
			gripperPushButton.whenReleased(new GripperStop());
		}
		if(RobotMap.SUBSYSTEM.IS_LIFTING_UNIT_WAGON_IN_USE) {			
			liftingUnitWagonForwardButton = new JoystickButton(liftingUnitController, 5);
//			liftingUnitWagonBackwardButton = new JoystickButton(liftingUnitController, 6);
//			
			liftingUnitWagonForwardButton.whenPressed(new LiftingUnitWagonFindEndpointFront());//new LiftingUnitWagonMove(LiftingUnitWagon.FRONT));
//			liftingUnitWagonBackwardButton.whenPressed(new LiftingUnitWagonMove(LiftingUnitWagon.BACK));
		}
		if(RobotMap.SUBSYSTEM.IS_LIFTING_UNIT_IN_USE) {
			liftingUnitHoldPositionButton = new JoystickButton(liftingUnitController, 1);
			liftingUnitHoldPositionButton.whenPressed(new LiftingUnitHoldPosition());
			liftingUnitGoToSwitchButton = new JoystickButton(liftingUnitController, 2);
			liftingUnitGoToSwitchButton.whenPressed(new LiftingUnitMoveToPosition(RobotMap.ROBOT.LIFTING_UNIT_SWITCH_ALTITUDE_IN_TICKS));
			liftingUnitGoToGroundButton = new JoystickButton(liftingUnitController, 3);
			liftingUnitGoToGroundButton.whenPressed(new LiftingUnitMoveToPosition(RobotMap.ROBOT.LIFTING_UNIT_GROUND_ALTITUDE_IN_TICKS));
//			liftingUnitTeleoperated = new JoystickButton(liftingUnitController, 4);
//			liftingUnitTeleoperated.whenPressed(new LiftingUnitTeleoperated());
			
			liftingUnitFindEndpointDownButton = new JoystickButton(liftingUnitController, 6);
			liftingUnitFindEndpointDownButton.whenPressed(new LiftingUnitFindEndpointDown());
//			liftingUnitResetButton = new JoystickButton(liftingUnitController, 5);
//			liftingUnitResetButton.whenPressed(new LiftingUnitReset());
		}
		if(RobotMap.SUBSYSTEM.IS_DIFFERENTIAL_DRIVE_IN_USE) {
		// nothing to do. the drive uses the internal default command.
		}
		if(RobotMap.SUBSYSTEM.IS_SWERVE_DRIVE_IN_USE) {
			swerveWheelCheckButton = new JoystickButton(joystickOne, 8);
			swerveWheelCheckButton.whenPressed(new SwerveDriveAngleOnSingleWheel());
			swerveWheelForwardButton = new JoystickButton(joystickOne, 7);
			swerveWheelForwardButton.whenPressed(new SwerveDriveSetPosToZero());					
			swerveDriveTeleopButton = new JoystickButton(joystickOne, 4);
			swerveDriveTeleopButton.whenPressed(new SwerveDriveTeleoperated());
			swerveDriveParallelTeleopButton = new JoystickButton(joystickOne, 5);
			swerveDriveParallelTeleopButton.whenPressed(new SwerveDriveParallelTeleoperated());
			swerveDriveMecanumSimilarTeleopButton = new JoystickButton(joystickOne, 6);
			swerveDriveMecanumSimilarTeleopButton.whenPressed(new SwerveDriveMecanumSimilarTeleoperated());
		}
		if(RobotMap.SUBSYSTEM.IS_SWERVE_WHEEL_IN_USE) {
			swerveWheelForwardButton = new JoystickButton(joystickOne, 2);
			swerveWheelForwardButton.whenPressed(new SwerveDriveWheelTeleoperated());
			swerveWheelForwardButton.whenReleased(new SwerveDriveWheelStop());
		}

	}
	
}
