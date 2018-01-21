package org.usfirst.frc.team6417.robot;

import org.usfirst.frc.team6417.robot.commands.GripperPull;
import org.usfirst.frc.team6417.robot.commands.GripperPush;
import org.usfirst.frc.team6417.robot.commands.GripperStop;
import org.usfirst.frc.team6417.robot.commands.PoleToGroundAltitude;
import org.usfirst.frc.team6417.robot.commands.PoleToSwitchAltitude;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {	
	private final Joystick joystickOne;
	private final JoystickButton gripperPullButton;
	private final JoystickButton gripperPushButton;
	
	private JoystickButton poleElevateToGroundAltitudeButton;
	private JoystickButton poleElevateToSwitchAltitudeButton;

	public OI() {
		joystickOne = new Joystick(RobotMap.CONTROLLER.RIGHT);
		
		gripperPullButton = new JoystickButton(joystickOne, 11);
		gripperPushButton = new JoystickButton(joystickOne, 12);
		
		poleElevateToGroundAltitudeButton = new JoystickButton(joystickOne, 3);
		poleElevateToSwitchAltitudeButton = new JoystickButton(joystickOne, 5);

		
		gripperPullButton.whenPressed(new GripperPush());
		gripperPullButton.whenReleased(new GripperStop());
		gripperPushButton.whenPressed(new GripperPull());
		gripperPushButton.whenReleased(new GripperStop());
		poleElevateToSwitchAltitudeButton.whenPressed(new PoleToSwitchAltitude());	
		poleElevateToGroundAltitudeButton.whenPressed(new PoleToGroundAltitude());
	}
}
