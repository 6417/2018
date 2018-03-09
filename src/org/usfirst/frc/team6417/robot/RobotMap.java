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
	/**
	 * In the RobotMap.ROBOT class you'll find different values concerning your
	 * robot
	 */
	public static class ROBOT {
		public static final double WHEEL_DIAMETER = 15.24; /*cm*/
		public static final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * MATH.PI; /*cm*/
		public static final double DIAGONAL_DISTANCE_BETWEEN_WHEELS = 0.0; /*cm*/
		public static final double DIST_PER_PULSE = ROBOT.WHEEL_CIRCUMFERENCE / ENCODER.PULSE_PER_ROTATION; /*cm*/

		public static final double WHEEL_DISTANCE_FRONT_TO_BACK = 63; /*cm*/
		public static final double WHEEL_DISTANCE_LEFT_TO_RIGHT = 53; /*cm*/
		
		
		public static final int LIFTING_UNIT_GROUND_ALTITUDE_IN_TICKS = 0; /*encoder ticks*/
		public static final int LIFTING_UNIT_EXCHANGE_LOW_ALTITUDE_IN_TICKS = -2000; //-20000; /*encoder ticks*/
		public static final int LIFTING_UNIT_SWITCH_ALTITUDE_IN_TICKS = -4000; //-40000; /*encoder ticks*/
		public static final int LIFTING_UNIT_SCALE_LOW_ALTITUDE_IN_TICKS = -6000;// -60000; /*encoder ticks*/
		public static final int LIFTING_UNIT_SCALE_MIDDLE_ALTITUDE_IN_TICKS = -8000; //-80000; /*encoder ticks*/
		public static final int LIFTING_UNIT_SCALE_HIGH_ALTITUDE_IN_TICKS = -10000; //-100000; /*encoder ticks*/
		public static final double LIFTING_UNIT_CHAIN_WHEEL_RADIUS_IN_METER = 0.028535;
		
		public static final double SWERVE_ANGLE_GEAR_RATIO = 1.0 / 125.0; /* One rotation of worm-gear leads to 1/125 rotation of the angle-gear */
		public static final double SWERVE_ANGLE_PER_WORM_GEAR_ROTATION_IN_GRAD = 360.0 * SWERVE_ANGLE_GEAR_RATIO; // 360.0 * SWERVE_ANGLE_GEAR_RATIO; /* One rotation of worm-gear leads to 1/125 rotation of the angle-gear */
		public static final double SWERVE_ANGLE_PER_WORM_GEAR_ROTATION_IN_RADIANS = 2.0 * RobotMap.MATH.PI * SWERVE_ANGLE_GEAR_RATIO; // 360.0 * SWERVE_ANGLE_GEAR_RATIO; /* One rotation of worm-gear leads to 1/125 rotation of the angle-gear */		
		
		public static final String DRIVE_FRONT_LEFT_NAME = "FL";
		public static final String DRIVE_FRONT_RIGHT_NAME = "FR";
		public static final String DRIVE_BACK_LEFT_NAME = "BL";
		public static final String DRIVE_BACK_RIGHT_NAME = "BR";
		public static final String DRIVE_ANGLE = "A";
		public static final String DRIVE_VELOCITY = "V";
		
	}
	
	public static class SUBSYSTEM {
		public static boolean IS_GRIPPER_IN_USE = false;
		public static boolean IS_LIFTING_UNIT_IN_USE = false;
		public static boolean IS_LIFTING_UNIT_WAGON_IN_USE = false;
		public static boolean IS_DIFFERENTIAL_DRIVE_IN_USE = false;
		public static boolean IS_SWERVE_DRIVE_IN_USE = true;
		public static boolean IS_SWERVE_WHEEL_IN_USE = false;
		public static boolean IS_CAMERA_IN_USE = false;
	}	
	
	public static class VELOCITY {
		public static final double STOP_VELOCITY = 0.0;
		
		public static final double LIFTING_UNIT_MOTOR_UP_VELOCITY = 0.25;
		public static final double LIFTING_UNIT_MOTOR_DOWN_VELOCITY = -0.3;
		
		public static final double LIFTING_UNIT_WAGON_MOTOR_FORWARD_VELOCITY = 0.25;
		public static final double LIFTING_UNIT_WAGON_MOTOR_BACKWARD_VELOCITY = -0.3;

		public static final double SWERVE_DRIVE_ANGLE_MOTOR_FORWARD_VELOCITY = 0.4;
		public static final double SWERVE_DRIVE_ANGLE_MOTOR_BACKWARD_VELOCITY = -0.4;
		public static final double SWERVE_DRIVE_ANGLE_MOTOR_ZEROPOINT_CALIBRATION_VELOCITY = 0.4;
	}

	public static class SENSOR {
		public static final int LIFTING_UNIT_WAGON_ENDPOSITION_FRONT_THRESHOLD = 650000;//750000;
//		public static final int LIFTING_UNIT_WAGON_ENDPOSITION_LOWER_THRESHOLD = 467;

		public static final int DRIVE_WHEEL_ZEROPOINT_UPPER_THRESHOLD = 2100;
		public static final int DRIVE_WHEEL_ZEROPOINT_LOWER_THRESHOLD = 1000;
}
	
	public static class DIO {
		public static final int LIFTING_UNIT_WAGON_POSITION_BACK_PORT = 0;
	}
	
	public static class AIO {
		public static final int DRIVE_FRONT_LEFT_POSITION_SENSOR_PORT = 0;
		public static final int DRIVE_FRONT_RIGHT_POSITION_SENSOR_PORT = 1;
		public static final int DRIVE_BACK_LEFT_POSITION_SENSOR_PORT = 2;
		public static final int DRIVE_BACK_RIGHT_POSITION_SENSOR_PORT = 3; // ok
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
		
		public static final int DIFFERENTIAL_DRIVE_FRONT_LEFT_PORT = DRIVE_FRONT_LEFT_VELOCITY_PORT;
		public static final int DIFFERENTIAL_DRIVE_FRONT_RIGHT_PORT = 14;

	}

	/**
	 * In the RobotMap.ENCODER class you'll find different the ports of the
	 * encoder and values to the encoder
	 */
	public static class ENCODER {
		public static final int INITIAL_VALUE = 0;
		
		public static final int PULSE_PER_ROTATION = 512;
		public static final int PULSE_EPSILON = 500; // Amount of ticks of fuzziness when compare two encoder-values on equal
		public static final int PULSE_PER_ROTATION_WORM_GEAR = 2048; // TODO Verify this empirically measured value
		
		// https://www.vexrobotics.com/vexpro/motors-electronics/encoders/217-5046.html :
		public static final int PULSE_PER_ROTATION_VERSA_PLANETARY = 1024;
		public static final int QUADRATURE_UNITS_PER_ROTATION = 4096;
		public static final int QUADRATURE_EPSILON = 2000; // Amount of ticks of fuzziness when compare two encoder-values on equal
	}

	public static class CONTROLLER {
		public static final int DIRECTION_CONTROLLER = 0;
		public static final int LIFTING_UNIT_CONTROLLER = 1;
	}

	public static class LIFTING_UNIT_CONTROLLER {
		public static final int B01 = 1;
		public static final int B02 = 2;
		public static final int B03 = 3;
		public static final int B04 = 4;
		public static final int B05 = 5;
		public static final int B06 = 6;
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
			public static final int B01 = 1;
			public static final int B02 = 2;
			public static final int B03 = 3;
			public static final int B04 = 4;
			public static final int B05 = 5;
			public static final int B06 = 6;
			public static final int B07 = 7;
			public static final int B08 = 8;
			public static final int B09 = 9;
			public static final int B10 = 10;
			public static final int B11 = 11;
			public static final int B12 = 12;
			public static final int B13 = 13;
		}

		/**
		 * contains Deadzones for different Joysticks
		 */
		public static class DEADZONES {
			public static final double JOYSTICK1_X = 0.01;
			public static final double JOYSTICK1_Y = 0.01;
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
