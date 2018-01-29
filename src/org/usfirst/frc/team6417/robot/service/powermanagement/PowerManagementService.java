package org.usfirst.frc.team6417.robot.service.powermanagement;

import edu.wpi.first.wpilibj.command.Subsystem;

public interface PowerManagementService {
	double calculatePowerFor(Subsystem subsystem);
}
