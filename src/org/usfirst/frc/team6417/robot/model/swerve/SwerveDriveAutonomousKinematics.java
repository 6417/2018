package org.usfirst.frc.team6417.robot.model.swerve;

public final class SwerveDriveAutonomousKinematics {
	public enum POS_IN_STATION {
		LEFT_SIDE,
		CENTER,
		RIGHT_SIDE,
		UNKNOWN
	}
	public enum GOAL_SIDE {
		LEFT,
		STRAIGHT,
		RIGHT,
		UNKNOWN
	}
	
	public double calculateAngle(POS_IN_STATION actualPosInStation, GOAL_SIDE actualSwitchGoal) {
		System.out.println("calculateAngle station:"+actualPosInStation+" -> goal:"+actualSwitchGoal);
		double angle = 0;
		switch(actualPosInStation) {
			case LEFT_SIDE:{
					switch(actualSwitchGoal) {
						case LEFT:{
							angle = -0.25;
							break;
						}
						case STRAIGHT:{
							angle = 0;
							break;
						}
						case RIGHT:{
							angle = 0.25;
							break;
						}
						case UNKNOWN:
						default:{
							angle = 0;
							break;
						}						
					}
				break;
			}
			case CENTER:{
				switch(actualSwitchGoal) {
					case LEFT:{
						angle = -0.125;
						break;
					}
					case STRAIGHT:{
						angle = 0;
						break;
					}
					case RIGHT:{
						angle = 0.125 / 1.2;
						break;
					}
					case UNKNOWN:
					default:{
						angle = 0;
						break;
					}						
				}
				break;
			}
			case RIGHT_SIDE:{
				switch(actualSwitchGoal) {
					case LEFT:{
						angle = 0.25;
						break;
					}
					case STRAIGHT:{
						angle = 0;
						break;
					}
					case RIGHT:{
						angle = -0.25;
						break;
					}
					case UNKNOWN:
					default:{
						angle = 0;
						break;
					}						
				}
			}
			case UNKNOWN:
			default:{}
		}
		return angle;
	}
	
	
	public int calculateDistanceInTicks(POS_IN_STATION actualPosInStation, GOAL_SIDE actualSwitchGoal) {
		System.out.println("SwerveDriveAutonomousKinematics.calculateDistanceInTicks("+actualPosInStation+", "+actualSwitchGoal+")");
		switch(actualPosInStation) {
		case LEFT_SIDE:{
			switch(actualSwitchGoal) {
				case LEFT:{
					return 90000;
				}
				case STRAIGHT:{
					return 5000;
				}
				case RIGHT:{
					return 100000;
				}
				case UNKNOWN:
				default:
			}
			break;
		}
		case CENTER:{
			switch(actualSwitchGoal) {
				case LEFT:{
					return 91500;
				}
				case STRAIGHT:{
					return 83000;
				}
				case RIGHT:{
					return 91500;
				}
				case UNKNOWN:
				default:
			}
			break;
		}
		case RIGHT_SIDE:{
			switch(actualSwitchGoal) {
				case LEFT:{
					return 120000;
				}
				case STRAIGHT:{
					return 5000;
				}
				case RIGHT:{
					return 90000;
				}
				case UNKNOWN:
				default:
			}
			break;
		}
		case UNKNOWN:
		default:
		}
		return 0;
	}

	
	public static void main(String[] args) {
		SwerveDriveAutonomousKinematics test = new SwerveDriveAutonomousKinematics();
		System.out.println("Station left goal left: "+test.calculateAngle(
				SwerveDriveAutonomousKinematics.POS_IN_STATION.LEFT_SIDE, 
				SwerveDriveAutonomousKinematics.GOAL_SIDE.LEFT));
		System.out.println("Station left goal straight: "+test.calculateAngle(
				SwerveDriveAutonomousKinematics.POS_IN_STATION.LEFT_SIDE, 
				SwerveDriveAutonomousKinematics.GOAL_SIDE.STRAIGHT));
		System.out.println("Station center goal left: "+test.calculateAngle(
				SwerveDriveAutonomousKinematics.POS_IN_STATION.CENTER, 
				SwerveDriveAutonomousKinematics.GOAL_SIDE.LEFT));
		System.out.println("Station center goal straight: "+test.calculateAngle(
				SwerveDriveAutonomousKinematics.POS_IN_STATION.CENTER, 
				SwerveDriveAutonomousKinematics.GOAL_SIDE.STRAIGHT));
		System.out.println("Station center goal right: "+test.calculateAngle(
				SwerveDriveAutonomousKinematics.POS_IN_STATION.CENTER, 
				SwerveDriveAutonomousKinematics.GOAL_SIDE.RIGHT));

		System.out.println("Station right goal right: "+test.calculateAngle(
				SwerveDriveAutonomousKinematics.POS_IN_STATION.RIGHT_SIDE, 
				SwerveDriveAutonomousKinematics.GOAL_SIDE.RIGHT));
		System.out.println("Station right goal straight: "+test.calculateAngle(
				SwerveDriveAutonomousKinematics.POS_IN_STATION.RIGHT_SIDE, 
				SwerveDriveAutonomousKinematics.GOAL_SIDE.STRAIGHT));

	}


}
