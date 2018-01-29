package org.usfirst.frc.team6417.robot.service.powermanagement;

import edu.wpi.first.wpilibj.command.Subsystem;

public final class SimplePowerManagementService implements PowerManagementService {

	@Override
	public double calculatePowerFor(Subsystem subsystem) {
		return 1.0;
	}

}
