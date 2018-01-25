package org.usfirst.frc.team6417.robot;

import org.usfirst.frc.team6417.robot.commands.GripperPull;
import org.usfirst.frc.team6417.robot.commands.GripperPush;
import org.usfirst.frc.team6417.robot.commands.GripperStop;
import org.usfirst.frc.team6417.robot.commands.LiftingUnitMove;
import org.usfirst.frc.team6417.robot.commands.LoadingPlatformDown;
import org.usfirst.frc.team6417.robot.commands.LoadingPlatformUp;
import org.usfirst.frc.team6417.robot.commands.PoleToGroundAltitude;
import org.usfirst.frc.team6417.robot.commands.PoleToSwitchAltitude;
import org.usfirst.frc.team6417.robot.subsystems.LiftingUnit;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {	
	public final Joystick joystickOne = new Joystick(RobotMap.CONTROLLER.RIGHT);
	private final JoystickButton gripperPullButton;
	private final JoystickButton gripperPushButton;
	
	private JoystickButton poleElevateToGroundAltitudeButton;
	private JoystickButton poleElevateToSwitchAltitudeButton;
	private JoystickButton loadingPlatformDownButton, loadingPlatformUpButton;
	private JoystickButton liftingUnitToGroundAltitudeButton;
	private JoystickButton liftingUnitToSwitchAltitudeButton;
	private JoystickButton liftingUnitToScaleLowAltitudeButton;
	private JoystickButton liftingUnitToScaleHighAltitudeButton;

	private static OI INSTANCE;
	
	public static OI getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new OI();
		}
		return INSTANCE;
	}
	
	private OI() {
		gripperPullButton = new JoystickButton(joystickOne, 11);
		gripperPushButton = new JoystickButton(joystickOne, 12);
		
		poleElevateToGroundAltitudeButton = new JoystickButton(joystickOne, 3);
		poleElevateToSwitchAltitudeButton = new JoystickButton(joystickOne, 5);
		
		loadingPlatformUpButton = new JoystickButton(joystickOne, 8);
		loadingPlatformDownButton = new JoystickButton(joystickOne, 7);
		
		liftingUnitToGroundAltitudeButton = new JoystickButton(joystickOne, 4);
		liftingUnitToSwitchAltitudeButton = new JoystickButton(joystickOne, 6);
		liftingUnitToScaleLowAltitudeButton = new JoystickButton(joystickOne, 9);
		liftingUnitToScaleHighAltitudeButton = new JoystickButton(joystickOne, 10);
		
		gripperPullButton.whenPressed(new GripperPush());
		gripperPullButton.whenReleased(new GripperStop());
		gripperPushButton.whenPressed(new GripperPull());
		gripperPushButton.whenReleased(new GripperStop());
		
		poleElevateToSwitchAltitudeButton.whenPressed(new PoleToSwitchAltitude());	
		poleElevateToGroundAltitudeButton.whenPressed(new PoleToGroundAltitude());
		
		loadingPlatformDownButton.whenPressed(new LoadingPlatformDown());
		loadingPlatformUpButton.whenPressed(new LoadingPlatformUp());
		loadingPlatformDownButton.whenPressed(new LoadingPlatformDown());
		
		liftingUnitToGroundAltitudeButton.whenPressed(new LiftingUnitMove(LiftingUnit.TO_GROUND));
		liftingUnitToSwitchAltitudeButton.whenPressed(new LiftingUnitMove(LiftingUnit.TO_SWITCH));
		liftingUnitToScaleLowAltitudeButton.whenPressed(new LiftingUnitMove(LiftingUnit.TO_SCALE_LOW));
		liftingUnitToScaleHighAltitudeButton.whenPressed(new LiftingUnitMove(LiftingUnit.TO_SCALE_HIGH));
		
	}
	
}
