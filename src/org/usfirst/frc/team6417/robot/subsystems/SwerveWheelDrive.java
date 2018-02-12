package org.usfirst.frc.team6417.robot.subsystems;

import org.usfirst.frc.team6417.robot.RobotMap;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Implementation of the wheel of a swerve drive
 * @author https://jacobmisirian.gitbooks.io/frc-swerve-drive-programming/content/part-2-driving.html
 */
public final class SwerveWheelDrive extends Subsystem {
	private Talon angleMotor;
	private Talon speedMotor;
	private PIDController pidController;
	
	private final double MAX_VOLTS = 4.95;
	private Encoder theEncoder;
	
	public SwerveWheelDrive (int angleMotor, int speedMotor, int encoder) {
		super("SwerveWheelDrive");
		
	    this.angleMotor = new Talon(angleMotor);// new MotorController ("Angle-Motor", angleMotor);
	    this.speedMotor = new Talon(speedMotor);// new MotorController ("Speed-Motor", speedMotor);
	    this.speedMotor.setInverted(true);
	    theEncoder = new Encoder(0, 1);
	    theEncoder.reset();

		SmartDashboard.putNumber("Swerve Velocity", 0);
		SmartDashboard.putNumber("Swerve Angle Nominal", 0);
		SmartDashboard.putNumber("Swerve Angle Actual", theEncoder.get());

//	    pidController = new PIDController (1, 0, 0, new AnalogInput (encoder), this.angleMotor);
//	    pidController.setName("Angle-Motor-Controller");
	    
//	    pidController.setOutputRange (-1, 1);
//	    pidController.setContinuous ();
//	    pidController.enable ();
	}

	@Override
	protected void initDefaultCommand() {
//		setDefaultCommand(new SwerveDriveWheelTeleoperated());
	}

	double error = 0;
	private int pulsesToGo;
	private int target;
	
	private void toAngleInRadians(double angleInRadians) {
//		theEncoder.reset();
//		double rotation = angle * (125.0 / 360.0);// / RobotMap.ROBOT.SWERVE_ANGLE_GEAR_RATIO * angle;
		double rotation = angleInRadians / RobotMap.ROBOT.SWERVE_ANGLE_PER_WORM_GEAR_ROTATION;// / RobotMap.ROBOT.SWERVE_ANGLE_GEAR_RATIO * angle;
		double rotationInPulses = rotation * RobotMap.ENCODER.PULSE_PER_ROTATION;		
		int pulsesToCount = (int) (rotationInPulses);

		error += rotationInPulses - (double)pulsesToCount;
		
		SmartDashboard.putNumber("Expected Error", error);
		SmartDashboard.putNumber("Angle encoder actual", theEncoder.get());
		SmartDashboard.putNumber("Worm-gear rotations", pulsesToCount);

		angleMotor.set(0.6);
		while(theEncoder.get() <= pulsesToCount) {
			//;
			SmartDashboard.putNumber("Angle encoder actual", theEncoder.get());
			SmartDashboard.putNumber("Worm-gear rotations", pulsesToCount);
		}
		angleMotor.set(0.0);
	}
	
	public void toAngle(double angleInDegrees) {
		toAngleInRadians(angleInDegrees * RobotMap.MATH.PI / 180.0);
	}
	
	public void drive (double speed, double angle) {
		speedMotor.set (speed);
		SmartDashboard.putNumber("Swerve Velocity", speed);
		SmartDashboard.putNumber("Swerve Angle Nominal", angle);
		SmartDashboard.putNumber("Swerve Angle Actual", theEncoder.get());

	    double setpoint = angle * (MAX_VOLTS * 0.5) + (MAX_VOLTS * 0.5); // Optimization offset can be calculated here.
	    if (setpoint < 0) {
	        setpoint = MAX_VOLTS + setpoint;
	    }
	    if (setpoint > MAX_VOLTS) {
	        setpoint = setpoint - MAX_VOLTS;
	    }
	    
//	    angleMotor.set(angle);

//	    pidController.setSetpoint (setpoint);
	}

	public void gotoAngle(double angleInRadians) {
		double rotation = angleInRadians / RobotMap.ROBOT.SWERVE_ANGLE_PER_WORM_GEAR_ROTATION;
		double rotationInPulses = rotation * RobotMap.ENCODER.PULSE_PER_ROTATION;		
		pulsesToGo = (int) (rotationInPulses);

		error += rotationInPulses - (double)pulsesToGo;
		
		target = theEncoder.get() + pulsesToGo;
		
		SmartDashboard.putNumber("Expected Error", error);
		SmartDashboard.putNumber("Angle encoder actual", theEncoder.get());
		SmartDashboard.putNumber("Worm-gear pulses left", target - theEncoder.get());
		SmartDashboard.putNumber("Worm-gear pulses", pulsesToGo);
		SmartDashboard.putNumber("Worm-gear target", target);

		if(pulsesToGo > 0) {
			angleMotor.set(RobotMap.VELOCITY.SWERVE_DRIVE_ANGLE_MOTOR_FORWARD_VELOCITY);
		}else {
			angleMotor.set(RobotMap.VELOCITY.SWERVE_DRIVE_ANGLE_MOTOR_BACKWARD_VELOCITY);
		}
	}
	
	public void tick() {
		SmartDashboard.putNumber("Angle encoder actual", theEncoder.get());
		SmartDashboard.putNumber("Worm-gear pulses left", target - theEncoder.get());
		SmartDashboard.putNumber("Worm-gear target", target);
	}

	public boolean onTarget() {
		if(pulsesToGo > 0) {
			return (theEncoder.get() >= target);
		}
		return (theEncoder.get() <= target);
	}
}
