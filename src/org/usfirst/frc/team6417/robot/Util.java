package org.usfirst.frc.team6417.robot;

public class Util {
	
	/**
	 * Limits the Object o to a range given by min and max. ( min <= o <= max )
	 * @param o Object to compare
	 * @param min minimum of the range of o
	 * @param max maximum of the range of o
	 * @return 
	 */
	public static <T extends Comparable<? super T>> T limit(T o, T min, T max) {
		if (o.compareTo(min) < 0) return min;
	    if (o.compareTo(max) > 0) return max;
	    return o;
	}
	
	/**
	 * Test the Object o if it is in the range given.
	 * @param o Object to compare
	 * @param min minimum of the range of o
	 * @param max maximum of the range of o
	 * @return
	 */
	public static <T extends Comparable<? super T>> boolean inRange(T o, T min, T max) {
		if (o.compareTo(min) < 0) return false;
	    if (o.compareTo(max) > 0) return false;
	    return true;
	}

	
}
