package org.usfirst.frc.team6417.robot.service.powermanagement;

public final class AdaptivePowerManagementStrategy extends PowerManagementStrategy {

	private double x;
	
	public AdaptivePowerManagementStrategy(double x) {
		this.x = x;
	}
	
	@Override
	public double calculatePower() {
		return x;
	}


    public static double powerLift(double t) {
        double powerLift = 1 + (t * (0.6 - 1));// x unklar,min PowerLift wenn max PowerDrive; in
           // Beispiel 0.6 bzw 60%
        System.out.println("power of Lift is: " + powerLift+" or "+ (powerLift*100)+"%");
        return powerLift;
    }

    public static double powerGripper(double t) {
        double powerGripper = 1 + (t * (0.5- 1));// x unklar, min PowerGripper wenn max PowerDrive in
           // Beispiel 0.5 bzw 50%
        System.out.println("power of Gripper is: " + powerGripper+" or "+ (powerGripper*100)+"%");
        return powerGripper;
    }

    public static double powerDrive(double t) {
        double powerDrive = t;
        System.out.println("power of drive is: " + powerDrive + " or " + (powerDrive * 100) + "%");
        return powerDrive;
    }

}
