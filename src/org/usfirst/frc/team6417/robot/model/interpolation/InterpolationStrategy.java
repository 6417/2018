package org.usfirst.frc.team6417.robot.model.interpolation;

/**
 * An interpolation strategy interpolates between two points.
 */
public interface InterpolationStrategy {
	double nextX();
	boolean onTarget();
}
