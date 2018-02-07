package org.usfirst.frc.team6417.robot.service.powermanagement;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Subsystem;

public class PowerManager {
	private Map<String, PowerManagementStrategy> subsystems = new HashMap<String, PowerManagementStrategy>();

	public PowerManager addSubsystem(Subsystem subsystem, PowerManagementStrategy strategy) {
		subsystems.put(subsystem.getName(), strategy);
		return this;
	}
	
	public PowerManager removeSubsystem(Subsystem subsystem) {
		subsystems.remove(subsystem.getName());
		return this;
	}
	
	public double calculatePowerFor(String subsystemName) {
		if (!subsystems.containsKey(subsystemName)) {
			return subsystems.get(subsystemName).calculatePower();
		}
		DriverStation.reportError("Missing Subsystem in " + this.getClass().getSimpleName() + " Subsystem "
				+ subsystemName + "!", true);
		
		// 0 should set motors to stop
		return 0;
	}
}
