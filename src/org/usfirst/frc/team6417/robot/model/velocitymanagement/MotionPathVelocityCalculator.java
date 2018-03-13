package org.usfirst.frc.team6417.robot.model.velocitymanagement;

import org.usfirst.frc.team6417.robot.RobotMap;

public final class MotionPathVelocityCalculator {
	int xEPB = RobotMap.ROBOT.LIFTING_UNIT_WAGON_BACK_POSITION_IN_TICKS;
	int xBPB = RobotMap.ROBOT.LIFTING_UNIT_WAGON_BACK_POSITION_BREAK_IN_TICKS;
	int xBPF = RobotMap.ROBOT.LIFTING_UNIT_WAGON_FRONT_POSITION_BREAK_IN_TICKS;
	int xEPF = RobotMap.ROBOT.LIFTING_UNIT_WAGON_FRONT_POSITION_IN_TICKS;
	double yEPF = RobotMap.VELOCITY.STOP_VELOCITY;
	double yBPF = RobotMap.VELOCITY.LIFTING_UNIT_WAGON_MOTOR_FORWARD_VELOCITY;//-1;
	double yBPB = RobotMap.VELOCITY.LIFTING_UNIT_WAGON_MOTOR_BACKWARD_VELOCITY; // 1
	double yEPB = RobotMap.VELOCITY.STOP_VELOCITY;		

	public MotionPathVelocityCalculator() {;}
	
	/**
	 * The MotionPathVelocityCalculator uses four points that represent a motion-profile from
	 * a point xEPB to xEPF via xBPB and xBPF. The xE* are the endpoints. The xB* are break-points for the motors.
	 */
	public MotionPathVelocityCalculator(
			int xEPB_,
			double yEPB,
			int xBPB_, 
			double yBPB,
			int xBPF_, 
			double yBPF,
			int xEPF_, 
			double yEPF
			) 
	{
		this.xEPB = xEPB_;
		this.xBPB = xBPB_;
		this.xBPF = xBPF_;
		this.xEPF = xEPF_;
		this.yEPF = yEPF;
		this.yBPF = yBPF;
		this.yBPB = yBPB;
		this.yEPB = yEPB;
	}
	
	/**
	 * Calculates the velocity by the given position and direction of movement.
	 * If the velocity is out-of-bounds the velocity is corrected to the boundary-velocity.
	 */
	public double calculateVelocity(double velocity, int position) {
		double boundaryVelocity = calculateVelocityBoundary(velocity, position);
		if(velocity < 0) {
			if(velocity > boundaryVelocity) {
				return velocity;
			}
		}else {
			if(velocity < boundaryVelocity) {
				return velocity;
			}
		}
		return boundaryVelocity;
	}
	/**
	 * Calculates the boundary of the given position and direction of movement
	 */
	private double calculateVelocityBoundary(double velocity, int position) {
		// Define the correct boundary-velocity depending on direction of movement in motion-phase (b)
		double boundaryVelocity = yBPF;
		if( velocity < 0 ) {
			boundaryVelocity = yBPB;
		}

		// If the position is out-of-bounds the velocity of the endpoints is selected
		if(position > xEPB) {
			return yEPF;
		} else if(position < xEPF) {
			return yEPB;
		}
		
		// Calculate the boundary-velocity if necessary
		if(velocity < 0) {
			// Moving backwards
			if(position > xBPB) {
				// Calculate boundary-velocity in motion-phase (c)
				double m = (yEPB - yBPB)/(xEPB - xBPB);
				double q = yEPB - m * xEPB;
				boundaryVelocity = m * position + q;
			}
		}else if(velocity > 0) {
			// Moving forwards
			if(position < xBPF) {
				// Calculate the boundary-velocity in motion-phase (a)
				double m = (yBPF - yEPF) / (xBPF - xEPF);
				double q = yEPF - m * xEPF;
				boundaryVelocity = m * position + q;
			}
		}
		return boundaryVelocity;
	}	
	
	public static void main(String[] args) {

		int div = 100;
		{
			System.out.println("LUW:");
		int xEPF_ = RobotMap.ROBOT.LIFTING_UNIT_WAGON_FRONT_POSITION_IN_TICKS;
		double yEPF = RobotMap.VELOCITY.STOP_VELOCITY;

		int xBPF_ = RobotMap.ROBOT.LIFTING_UNIT_WAGON_FRONT_POSITION_BREAK_IN_TICKS;
		double yBPF = RobotMap.VELOCITY.LIFTING_UNIT_WAGON_MOTOR_FORWARD_VELOCITY;//-1;
		
		int xBPB_ = RobotMap.ROBOT.LIFTING_UNIT_WAGON_BACK_POSITION_BREAK_IN_TICKS;
		double yBPB = RobotMap.VELOCITY.LIFTING_UNIT_WAGON_MOTOR_BACKWARD_VELOCITY; // 1
		
		int xEPB_ = RobotMap.ROBOT.LIFTING_UNIT_WAGON_BACK_POSITION_IN_TICKS;
		double yEPB = RobotMap.VELOCITY.STOP_VELOCITY;	

		MotionPathVelocityCalculator test = new MotionPathVelocityCalculator(
				xEPB_,
				yEPB,
				xBPB_,
				yBPB,
				xBPF_,
				yBPF,
				xEPF_,
				yEPF
				);
		
		
		double vel = RobotMap.VELOCITY.LIFTING_UNIT_WAGON_MOTOR_FORWARD_VELOCITY;
		int pos = xEPF_; 
		double res = test.calculateVelocity(vel, pos);
		System.out.println("1. vel:"+vel+", vel-bounded:"+res+", pos:"+pos);
		
		vel = RobotMap.VELOCITY.LIFTING_UNIT_WAGON_MOTOR_BACKWARD_VELOCITY;
		pos = xEPB_; 
		res = test.calculateVelocity(vel, pos);
		System.out.println("2. vel:"+vel+", vel-bounded:"+res+", pos:"+pos);

		vel = RobotMap.VELOCITY.LIFTING_UNIT_WAGON_MOTOR_FORWARD_VELOCITY;
		pos = RobotMap.ROBOT.LIFTING_UNIT_WAGON_FRONT_POSITION_IN_TICKS + RobotMap.ROBOT.LIFTING_UNIT_WAGON_BREAK_DISTANCE_IN_TICKS / div; 
		res = test.calculateVelocity(vel, pos);
		System.out.println("3. vel:"+vel+", vel-bounded:"+res+", pos:"+pos);

		vel = RobotMap.VELOCITY.LIFTING_UNIT_WAGON_MOTOR_BACKWARD_VELOCITY;
		pos = RobotMap.ROBOT.LIFTING_UNIT_WAGON_BACK_POSITION_IN_TICKS - RobotMap.ROBOT.LIFTING_UNIT_WAGON_BREAK_DISTANCE_IN_TICKS / div; 
		res = test.calculateVelocity(vel, pos);
		System.out.println("4. vel:"+vel+", vel-bounded:"+res+", pos:"+pos);
		}
		System.out.println("");
		{
			System.out.println("LU:");
			int xEPF_ = RobotMap.ROBOT.LIFTING_UNIT_GROUND_ALTITUDE_IN_TICKS;
			double yEPF = RobotMap.VELOCITY.STOP_VELOCITY;

			int xBPF_ = RobotMap.ROBOT.LIFTING_UNIT_GROUND_ALTITUDE_BREAK_IN_TICKS;
			double yBPF = RobotMap.VELOCITY.LIFTING_UNIT_MOTOR_DOWN_VELOCITY;//-1;
			
			int xBPB_ = RobotMap.ROBOT.LIFTING_UNIT_SCALE_HIGH_ALTITUDE_BREAK_IN_TICKS;
			double yBPB = RobotMap.VELOCITY.LIFTING_UNIT_MOTOR_UP_VELOCITY; // 1
			
			int xEPB_ = RobotMap.ROBOT.LIFTING_UNIT_SCALE_HIGH_ALTITUDE_IN_TICKS;
			double yEPB = RobotMap.VELOCITY.STOP_VELOCITY;	

			MotionPathVelocityCalculator test = new MotionPathVelocityCalculator(
					xEPB_,
					yEPB,
					xBPB_,
					yBPB,
					xBPF_,
					yBPF,
					xEPF_,
					yEPF
					);
			
			
			double vel = RobotMap.VELOCITY.LIFTING_UNIT_MOTOR_DOWN_VELOCITY;
			int pos = xEPF_; 
			double res = test.calculateVelocity(vel, pos);
			System.out.println("1. vel:"+vel+", vel-bounded:"+res+", pos:"+pos);
			
			vel = RobotMap.VELOCITY.LIFTING_UNIT_MOTOR_UP_VELOCITY;
			pos = xEPB_; 
			res = test.calculateVelocity(vel, pos);
			System.out.println("2. vel:"+vel+", vel-bounded:"+res+", pos:"+pos);

			vel = RobotMap.VELOCITY.LIFTING_UNIT_MOTOR_DOWN_VELOCITY;
			pos = RobotMap.ROBOT.LIFTING_UNIT_GROUND_ALTITUDE_IN_TICKS + RobotMap.ROBOT.LIFTING_UNIT_BREAK_DISTANCE_IN_TICKS / div; 
			res = test.calculateVelocity(vel, pos);
			System.out.println("3. vel:"+vel+", vel-bounded:"+res+", pos:"+pos);

			vel = RobotMap.VELOCITY.LIFTING_UNIT_MOTOR_UP_VELOCITY;
			pos = RobotMap.ROBOT.LIFTING_UNIT_SCALE_HIGH_ALTITUDE_IN_TICKS - RobotMap.ROBOT.LIFTING_UNIT_BREAK_DISTANCE_IN_TICKS / div; 
			res = test.calculateVelocity(vel, pos);
			System.out.println("4. vel:"+vel+", vel-bounded:"+res+", pos:"+pos);
			}		
		
	}

}
