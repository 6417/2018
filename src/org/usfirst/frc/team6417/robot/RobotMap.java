package org.usfirst.frc.team6417.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	/**
	 * In the RobotMap.ROBOT class you'll find different values concerning your
	 * robot
	 */
	public static class ROBOT {
		public static final double WHEEL_DIAMETER = 15.24; /*cm*/
		public static final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * MATH.PI; /*cm*/
		public static final double DIAGONAL_DISTANCE_BETWEEN_WHEELS = 0.0; /*cm*/
		public static final double DIST_PER_PULSE = ROBOT.WHEEL_CIRCUMFERENCE / ENCODER.PULSE_PER_ROTATION; /*cm*/

		public static final double WHEEL_DISTANCE_FRONT_TO_BACK = 45; /*cm*/
		public static final double WHEEL_DISTANCE_LEFT_TO_RIGHT = 45; /*cm*/
		
		public static final long LIFTING_UNIT_GROUND_ALTITUDE_IN_TICKS = 0; /*encoder ticks*/
		public static final long LIFTING_UNIT_SWITCH_ALTITUDE_IN_TICKS = 200; /*encoder ticks*/
		public static final long LIFTING_UNIT_SCALE_LOW_ALTITUDE_IN_TICKS = 500; /*encoder ticks*/
		public static final long LIFTING_UNIT_SCALE_MIDDLE_ALTITUDE_IN_TICKS = 600; /*encoder ticks*/
		public static final long LIFTING_UNIT_SCALE_HIGH_ALTITUDE_IN_TICKS = 700; /*encoder ticks*/
	}
	
	/**
	 * The RobotMap.MATH class provides you with different mathematical values
	 */
	public static class MATH {
		public static final double PI = 3.14159265359;
	}
	
	public static class VELOCITY {
		public static final double STOP_VELOCITY = 0.0;
		public static final double LIFTING_UNIT_WAGON_MOTOR_FORWARD_VELOCITY = 0.25;
		public static final double LIFTING_UNIT_WAGON_MOTOR_BACKWARD_VELOCITY = -0.3;
	}

	public static class SENSOR {
		public static final int LIFTING_UNIT_WAGON_ENDPOSITION_UPPER_THRESHOLD = 530;
		public static final int LIFTING_UNIT_WAGON_ENDPOSITION_LOWER_THRESHOLD = 467;
	}
	
	public static class DIO {
		public static final int LIFTING_UNIT_PORT_A = 0;
		public static final int LIFTING_UNIT_PORT_B = 1;
	}
	
	public static class AIO {
		public static final int LIFTING_UNIT_WAGON_ENDPOSITION_FRONT_PORT = 5;
		public static final int LIFTING_UNIT_WAGON_ENDPOSITION_BACK_PORT = 6;
	}
	
	/**
	 * In the RobotMap.MOTOR a CAN-bus port is mapped to a logical-port for our robot.
	 * @see The hardware-documentation for further details:
	 * https://docs.google.com/document/d/1LdHSLqYUQ-YM8OZ3idN6Gu3Klc91NrVBLmQmynH0i6Y/edit?usp=sharing
	 */
	public static class MOTOR {
		public static final int DRIVE_FRONT_LEFT_ANGLE_PORT = 0;
		public static final int DRIVE_FRONT_LEFT_VELOCITY_PORT = 4;
		public static final int DRIVE_FRONT_RIGHT_ANGLE_PORT = 1;
		public static final int DRIVE_FRONT_RIGHT_VELOCITY_PORT = 5;
		public static final int DRIVE_BACK_LEFT_ANGLE_PORT = 2;
		public static final int DRIVE_BACK_LEFT_VELOCITY_PORT = 6;
		public static final int DRIVE_BACK_RIGHT_ANGLE_PORT = 3;
		public static final int DRIVE_BACK_RIGHT_VELOCITY_PORT = 7;
		
		public static final int LIFTING_UNIT_WAGON_PORT = 8;

		public static final int LIFTING_UNIT_PORT_A = 9;
		public static final int LIFTING_UNIT_PORT_B = 10;

		public static final int GRIPPER_LEFT_PORT = 11;
		public static final int GRIPPER_RIGHT_PORT = 12;
		
		public static final int DIFFERENTIAL_DRIVE_FRONT_LEFT_PORT = 13;
		public static final int DIFFERENTIAL_DRIVE_FRONT_RIGHT_PORT = 14;

	}

	/**
	 * In the RobotMap.ENCODER class you'll find different the ports of the
	 * encoder and values to the encoder
	 */
	public static class ENCODER {
		public static final int PULSE_PER_ROTATION = 512;

		public static final int DRIVE_FRONT_LEFT_PORT_A = 0;
		public static final int DRIVE_FRONT_LEFT_PORT_B = 1;
		public static final int DRIVE_FRONT_RIGHT_PORT_A = 2;
		public static final int DRIVE_FRONT_RIGHT_PORT_B = 3;
		public static final int DRIVE_BACK_LEFT_PORT_A = 4;
		public static final int DRIVE_BACK_LEFT_PORT_B = 5;
		public static final int DRIVE_BACK_RIGHT_PORT_A = 6;
		public static final int DRIVE_BACK_RIGHT_PORT_B = 7;
		
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


}
