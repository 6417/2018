package org.usfirst.frc.team6417.robot;

import org.usfirst.frc.team6417.robot.commands.GripperPull;
import org.usfirst.frc.team6417.robot.commands.GripperPush;
import org.usfirst.frc.team6417.robot.commands.GripperStop;
import org.usfirst.frc.team6417.robot.commands.LiftRobotUpTeleoperated;
import org.usfirst.frc.team6417.robot.commands.LiftingUnitFindEndpointDown;
import org.usfirst.frc.team6417.robot.commands.LiftingUnitMoveToPosition;
import org.usfirst.frc.team6417.robot.commands.LiftingUnitWagonFindEndpointFront;
import org.usfirst.frc.team6417.robot.commands.LiftingUnitWagonMove;
import org.usfirst.frc.team6417.robot.commands.PrepareRobotElevationBehavior;
import org.usfirst.frc.team6417.robot.commands.SwerveDriveAngleOnSingleWheel;
import org.usfirst.frc.team6417.robot.commands.SwerveDriveResetVelocityEncoder;
import org.usfirst.frc.team6417.robot.commands.SwerveDriveTeleoperated;
import org.usfirst.frc.team6417.robot.commands.SwerveDriveWheelStop;
import org.usfirst.frc.team6417.robot.commands.SwerveDriveWheelTeleoperated;
import org.usfirst.frc.team6417.robot.subsystems.LiftingUnitWagon;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {	
	public final Joystick joystickOne = new Joystick(RobotMap.CONTROLLER.DIRECTION_CONTROLLER);
	public final Joystick liftingUnitController = new Joystick(RobotMap.CONTROLLER.LIFTING_UNIT_CONTROLLER);
	public final Joystick liftUpController = new Joystick(RobotMap.CONTROLLER.LIFT_UP_ROBOT_CONTROLLER);
	
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
	private JoystickButton sverveDriveVelocityEncoderResetButton;
	private JoystickButton startLiftingUnitFarBackButton;
	private JoystickButton liftUpRobotButton;
	private JoystickButton liftingUnitWagonFromFarBackToBackButton;

	private static OI INSTANCE;
	
	public static OI getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new OI();
		}
		return INSTANCE;
	}
	
	private OI() {
		if(RobotMap.SUBSYSTEM.IS_GRIPPER_IN_USE) {
			gripperPushButton = new JoystickButton(liftingUnitController, 5);
			gripperPullButton = new JoystickButton(liftingUnitController, 6);

			gripperPullButton.whenPressed(new GripperPull());
			gripperPullButton.whenReleased(new GripperStop());
			gripperPushButton.whenPressed(new GripperPush());
			gripperPushButton.whenReleased(new GripperStop());
		}
		if(RobotMap.SUBSYSTEM.IS_LIFTING_UNIT_WAGON_IN_USE) {			
			liftingUnitWagonForwardButton = new JoystickButton(liftingUnitController, 8);
//			liftingUnitWagonBackwardButton = new JoystickButton(liftingUnitController, 6);
//			
			liftingUnitWagonForwardButton.whenPressed(new LiftingUnitWagonFindEndpointFront());//new LiftingUnitWagonMove(LiftingUnitWagon.FRONT));
//			liftingUnitWagonBackwardButton.whenPressed(new LiftingUnitWagonMove(LiftingUnitWagon.BACK));
			
			liftingUnitWagonFromFarBackToBackButton = new JoystickButton(liftUpController, 3);
			liftingUnitWagonFromFarBackToBackButton.whenPressed(new LiftingUnitWagonMove(LiftingUnitWagon.BACK));
		}
		if(RobotMap.SUBSYSTEM.IS_LIFTING_UNIT_IN_USE) {
			liftingUnitHoldPositionButton = new JoystickButton(liftingUnitController, 1);
//			liftingUnitHoldPositionButton.whenPressed(new LiftingUnitHoldPosition());
			liftingUnitHoldPositionButton.whenPressed(new PrepareRobotElevationBehavior());
			liftingUnitGoToSwitchButton = new JoystickButton(liftingUnitController, 2);
			liftingUnitGoToSwitchButton.whenPressed(new LiftingUnitMoveToPosition(RobotMap.ROBOT.LIFTING_UNIT_SWITCH_ALTITUDE_IN_TICKS));
			liftingUnitGoToGroundButton = new JoystickButton(liftingUnitController, 3);
			liftingUnitGoToGroundButton.whenPressed(new LiftingUnitMoveToPosition(RobotMap.ROBOT.LIFTING_UNIT_GROUND_ALTITUDE_IN_TICKS));
			liftingUnitTeleoperated = new JoystickButton(liftingUnitController, 4);
			liftingUnitTeleoperated.whenPressed(new LiftingUnitWagonMove(LiftingUnitWagon.GAME_START));
			
			liftingUnitFindEndpointDownButton = new JoystickButton(liftingUnitController, 7);
			liftingUnitFindEndpointDownButton.whenPressed(new LiftingUnitFindEndpointDown());
//			liftingUnitResetButton = new JoystickButton(liftingUnitController, 5);
//			liftingUnitResetButton.whenPressed(new LiftingUnitReset());
			
			startLiftingUnitFarBackButton = new JoystickButton(liftUpController, 1);
			startLiftingUnitFarBackButton.whenPressed(new PrepareRobotElevationBehavior());
			liftUpRobotButton = new JoystickButton(liftUpController, 2);
			liftUpRobotButton.whenPressed(new LiftRobotUpTeleoperated());
		}
		if(RobotMap.SUBSYSTEM.IS_DIFFERENTIAL_DRIVE_IN_USE) {
		// nothing to do. the drive uses the internal default command.
		}
		if(RobotMap.SUBSYSTEM.IS_SWERVE_DRIVE_IN_USE) {
//			swerveWheelCheckButton = new JoystickButton(joystickOne, 2);
//			swerveWheelCheckButton.whenPressed(new SwerveDriveAngleOnSingleWheel());

			swerveWheelCheckButton = new JoystickButton(joystickOne, 8);
			swerveWheelCheckButton.whenPressed(new SwerveDriveAngleOnSingleWheel());
//			swerveWheelForwardButton = new JoystickButton(joystickOne, 7);
//			swerveWheelForwardButton.whenPressed(new SwerveDriveSetPosToZero());					
			swerveDriveTeleopButton = new JoystickButton(joystickOne, 4);
			swerveDriveTeleopButton.whenPressed(new SwerveDriveTeleoperated());
//			swerveDriveParallelTeleopButton = new JoystickButton(joystickOne, 5);
//			swerveDriveParallelTeleopButton.whenPressed(new SwerveDriveMecanumSimilarTeleoperated());
//			swerveDriveMecanumSimilarTeleopButton = new JoystickButton(joystickOne, 6);
//			swerveDriveMecanumSimilarTeleopButton.whenPressed(new SwerveDriveMecanumSimilarTeleoperated());
//			swerveDriveMecanumSimilarTeleopButton.whenPressed(new SwerveDriveWheelAngleCalibration());
			
			sverveDriveVelocityEncoderResetButton = new JoystickButton(joystickOne, 7);
			sverveDriveVelocityEncoderResetButton.whenPressed(new SwerveDriveResetVelocityEncoder());
			
		}
		if(RobotMap.SUBSYSTEM.IS_SWERVE_WHEEL_IN_USE) {
			swerveWheelForwardButton = new JoystickButton(joystickOne, 2);
			swerveWheelForwardButton.whenPressed(new SwerveDriveWheelTeleoperated());
			swerveWheelForwardButton.whenReleased(new SwerveDriveWheelStop());
		}

	}
	
}
