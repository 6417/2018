package org.usfirst.frc.team6417.robot;

import edu.wpi.first.wpilibj.Joystick;

public class DriveAtWrongLocation {
	private double maxDriveSpeed = 1;
	private double maxTurnSpeed = 1;
	
	public double[][] getJoyData(Joystick joy) {
		double x = joy.getX();
		double y = joy.getY();
		double twist = joy.getTwist();
		double angle = ((Math.atan2(y, x) + ((3 * Math.PI)/4)) % 360);
		double radius = (Math.sqrt((x*x)+(y*y)));
		double[][] data = new double[4][2];
		for(int i = 0; i < data.length; i++) {
			data[i][0] = angle;
		}
		
		data[0][1] = (((radius*maxDriveSpeed)+(twist*maxTurnSpeed))/(maxTurnSpeed+maxDriveSpeed)); /*motorFrontRight*/
		data[0][1] = (((radius*maxDriveSpeed)+(twist*maxTurnSpeed))/(maxTurnSpeed+maxDriveSpeed)); /*motorRearRight*/
		data[0][1] = (((radius*maxDriveSpeed)-(twist*maxTurnSpeed))/(maxTurnSpeed+maxDriveSpeed)); /*motorFrontLeft*/
		data[0][1] = (((radius*maxDriveSpeed)-(twist*maxTurnSpeed))/(maxTurnSpeed+maxDriveSpeed)); /*motorRearLeft*/
		
		return data;
	}
}
