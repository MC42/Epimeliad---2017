package org.usfirst.frc.team2590.looper;

/**
 * The base for a loop which is called periodically
 * @author Connor_Hofenbitzer
 */
public abstract class Loop {
	
	/**
	 * Code that runs when loop is started
	 */
	public abstract void onStart();
	
	/**
	 * Code that runs while it is looping
	 */
	public abstract void loop();
	
	/**
	 * Code that runs when the loop ends
	 */
	public abstract void onEnd();

}
