package org.usfirst.frc.team6417.robot.model;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
	
	public void addTransition(Event event, State state) {
		eventToStateMap.put(event, state);
	}
	
	public State transition(Event event) {
		State nextState = eventToStateMap.get(event);
		if(nextState == null) {
			SmartDashboard.putString("Missing transition in gripper", event.getClass().getSimpleName());
			return this;
		}
		return nextState;
	}
}