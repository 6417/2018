package org.usfirst.frc.team6417.robot.model;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.DriverStation;

/**
 * Class that represents a physical state of a device
 */
public class State {
	private final Map<Event, State> eventToStateMap = new HashMap<>();

	public void init() {;}
	
	/**
	 * Called every ~50ms
	 */
	public void tick() {;}
	
	public State addTransition(Event event, State state) {
		eventToStateMap.put(event, state);
		return this;
	}
	
	public State transition(Event event) {
		State nextState = eventToStateMap.get(event);
		if(nextState == null) {
			DriverStation.reportError("Missing transition in state machine from "+this.getClass().getSimpleName()+" with event "+event, true);
			return this;
		}
		return nextState;
	}

	public boolean isFinished() {
		return false;
	}
}
