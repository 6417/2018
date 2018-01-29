package org.usfirst.frc.team6417.robot.service.powermanagement;

import java.util.Map;
import java.util.HashMap;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.DriverStation;

public class PowerManager {
	private Map<Subsystem, PowerManagementStrategy> subsystems = new HashMap<Subsystem, PowerManagementStrategy>();

	public PowerManager addSubsystem(Subsystem subsystem, PowerManagementStrategy strategy) {
		subsystems.put(subsystem, strategy);
		return this;
	}
	
	public PowerManager removeSubsystem(Subsystem subsystem) {
		subsystems.remove(subsystem);
		return this;
	}
	
	public double calculatePowerFor(Subsystem subsystem) {
		if (!subsystems.containsKey(subsystem)) {
			return subsystems.get(subsystem).calculatePower();
		}
		DriverStation.reportError("Missing Subsystem in " + this.getClass().getSimpleName() + " Subsystem "
				+ this.getClass().getSimpleName() + "!", true);
		
		// 0 should set motors to stop
		return 0;
	}
}
