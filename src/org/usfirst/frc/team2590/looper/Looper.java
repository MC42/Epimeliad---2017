package org.usfirst.frc.team2590.looper;

import java.util.ArrayList;

/**
 * The handler for update loops
 * @author Connor_Hofenbitzer
 */
public class Looper {
	
	private long delay_;
	private ArrayList<Loop> loops_;
	private boolean running_ = false;
	
	private Runnable looper_ = new Runnable() {
		@Override
		public void run() {
			while(true) {
				if(running_) {
				
					//periodically update the loops
					for(Loop loop : loops_) {
						loop.loop();
					}
				
					// let the thread get some rest 
					try {
						Thread.sleep(delay_);
					} catch(Exception e) {
						e.printStackTrace();
					}
				
				}
			}
		}
	};
	
	
	public Looper(long delayTime) {
		delay_ = delayTime;
		loops_ = new ArrayList<Loop>();
		
		new Thread(looper_).start();
	}
	
	/**
	 * add a new loop to the arraylist
	 * @param loop : its a loop
	 */
	public void register(Loop loop) {
		loops_.add(loop);
	}
	
	/**
	 * Starts all the loops
	 */
	public void startLoops() {
		for(Loop loop : loops_) {
			loop.onStart();
		}
		running_ = true;
	}
	
	/**
	 * Ends the loops
	 */
	public void onEnd() {
		running_ = false;
		for(Loop loop : loops_) {
			loop.onEnd();
		}
	}
	
}
