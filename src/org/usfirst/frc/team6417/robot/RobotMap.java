package org.usfirst.frc.team6417.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

	/**
	 * The RobotMap.MATH class provides you with different mathematical values
	 */
	public static class MATH {
		public static final double PI = 3.14159265359;
	}
	
	public static class SPEED {
	}

	/**
	 * In the RobotMap.MOTOR class the motor ports are
	 */
	public static class MOTOR {
		public static final int GRIPPER_LEFT_PORT = 0;
		public static final int GRIPPER_RIGHT_PORT = 1;
		public static final int POLE_PORT = 2;
		public static final int DRIVE_LEFT_PORT = 3;
		public static final int DRIVE_RIGHT_PORT = 4;
	}

	/**
	 * In the RobotMap.ENCODER class you'll find different the ports of the
	 * encoder and values to the encoder
	 */
	public static class ENCODER {
		public static final int PULSE_PER_ROTATION = 512;
		public static final int POLE_UP_PORT_A = 0;
		public static final int POLE_UP_PORT_B = 1;
	}

	/**
	 * In the RobotMap.PNEUMATICS class you'll find different port definitions
	 * so you can use your pneumatics
	 */
	public static class PNEUMATICS {
		public static final int COMPRESSOR_PORT = 0;
		public static final int EXTEND_PORT = 0;
		public static final int RETRACT_PORT = 1;
	}

	public static class CONTROLLER {
		public static final int RIGHT = 0;
		public static final int LEFT = 1;
	}

	/**
	 * In the RobotMap.JOYSTICK class you'll find the ports of the joysticks
	 */
	public static class JOYSTICK {

		public static class AXIS {
			public static final int X = 0;
			public static final int Y = 1;
			public static final int TWIST = 2;
			public static final int SLIDER = 3;
		}

		public static class BUTTONS {
		}

		/**
		 * contains Deadzones for different Joysticks
		 */
		public static class DEADZONES {
			public static final double JOYSTICK1_X = 0.05;
			public static final double JOYSTICK1_Y = 0.05;
			public static final double JOYSTICK1_TWIST = 0.08;

			public static final double JOYSTICK2_X = 0.05;
			public static final double JOYSTICK2_Y = 0.05;
			public static final double JOYSTICK2_TWIST = 0.015;
		}
	}

	public static class XBOX {

		public static class AXIS {
			public static final int LEFT_X = 0;
			public static final int LEFT_Y = 1;
			public static final int RIGHT_X = 4;
			public static final int RIGHT_Y = 5;
		}

		public static class BUTTONS {
		}

		public static class DEADZONES {
			public static final double XBOX_LEFT_X = 0;
			public static final double XBOX_LEFT_Y = 0;
			public static final double XBOX_RIGHT_X = 0;
			public static final double XBOX_RIGHT_Y = 0;
		}
	}

	/**
	 * In the RobotMap.ROBOT class you'll find different values concerning your
	 * robot
	 */
	public static class ROBOT {
		public static final double WHEEL_DIAMETER = 15.24; /*cm*/
		public static final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * MATH.PI; /*cm*/
		public static final double DIAGONAL_DISTANCE_BETWEEN_WHEELS = 0.0; /*cm*/
		public static final double DIST_PER_PULSE = ROBOT.WHEEL_CIRCUMFERENCE / ENCODER.PULSE_PER_ROTATION; /*cm*/
	}
}
