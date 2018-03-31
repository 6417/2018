package org.usfirst.frc.team6417.robot;

import org.usfirst.frc.team6417.robot.commands.GripperPull;
import org.usfirst.frc.team6417.robot.commands.GripperPush;
import org.usfirst.frc.team6417.robot.commands.GripperStop;
import org.usfirst.frc.team6417.robot.commands.LiftingUnitFindEndpointDown;
import org.usfirst.frc.team6417.robot.commands.LiftingUnitWagonFindEndpointFront;
import org.usfirst.frc.team6417.robot.commands.LiftingUnitWagonMove;
import org.usfirst.frc.team6417.robot.commands.PrepareRobotElevationBehavior;
import org.usfirst.frc.team6417.robot.commands.SwerveDriveAngleOnSingleWheel;
import org.usfirst.frc.team6417.robot.commands.SwerveDriveResetVelocityEncoder;
import org.usfirst.frc.team6417.robot.commands.SwerveDriveTeleoperated;
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
	
	private JoystickButton gripperPullButton;
	private JoystickButton gripperPushButton;
	private JoystickButton swerveWheelForwardButton;
	private JoystickButton liftingUnitWagonForwardButton;
	private JoystickButton swerveWheelCheckButton;
	private JoystickButton liftingUnitHoldPositionButton;
	private JoystickButton liftingUnitFindEndpointDownButton;
	private JoystickButton liftingUnitTeleoperated;
	private JoystickButton swerveDriveTeleopButton;
	private JoystickButton sverveDriveVelocityEncoderResetButton;

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
			liftingUnitWagonForwardButton.whenPressed(new LiftingUnitWagonFindEndpointFront());
		}
		if(RobotMap.SUBSYSTEM.IS_LIFTING_UNIT_IN_USE) {
			liftingUnitHoldPositionButton = new JoystickButton(liftingUnitController, 1);
			liftingUnitHoldPositionButton.whenPressed(new PrepareRobotElevationBehavior());
			liftingUnitTeleoperated = new JoystickButton(liftingUnitController, 4);
			liftingUnitTeleoperated.whenPressed(new LiftingUnitWagonMove(LiftingUnitWagon.GAME_START));
			
			liftingUnitFindEndpointDownButton = new JoystickButton(liftingUnitController, 7);
			liftingUnitFindEndpointDownButton.whenPressed(new LiftingUnitFindEndpointDown());
		}
		if(RobotMap.SUBSYSTEM.IS_DIFFERENTIAL_DRIVE_IN_USE) {
		// nothing to do. the drive uses the internal default command.
		}
		if(RobotMap.SUBSYSTEM.IS_SWERVE_DRIVE_IN_USE) {
			swerveWheelCheckButton = new JoystickButton(joystickOne, 8);
			swerveWheelCheckButton.whenPressed(new SwerveDriveAngleOnSingleWheel());
			swerveDriveTeleopButton = new JoystickButton(joystickOne, 5);
			swerveDriveTeleopButton.whenPressed(new SwerveDriveTeleoperated());

			sverveDriveVelocityEncoderResetButton = new JoystickButton(joystickOne, 7);
			sverveDriveVelocityEncoderResetButton.whenPressed(new SwerveDriveResetVelocityEncoder());
			
		}

	}
	
}
