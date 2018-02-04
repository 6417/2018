package org.usfirst.frc.team6417.robot.model.powermanagement;

/**
 * Here the points PL and PD that represents the extremal-points 
 * as described in the research-paper: "Adaptive Power Management on Mobile Robot":
 * https://docs.google.com/document/d/1BEIhBlTZs1JRAGjC4NFdFg1YW6JQMdG3SM3B1ZmXsKg/edit?usp=sharing
 * <ul><li>PL is has maximal lifting force with lowest drive force.</li>
 * <li>PD has minimal lifting force and maximal drive force.</li></ul>
 * @author nils
 */
public final class Calibration {
	public static final PowerManagementPoint PL = new PowerManagementPoint(0.1, 1.0, 0.9, 1.0);
	public static final PowerManagementPoint PD = new PowerManagementPoint(1.0, 0.6, 0.5, 0.7);
}
