package autoroutines;

import autonomousCommands.Checkable;

public abstract class Routine {
	
	public abstract void init();
	public abstract void run();
	public abstract void finished();
	
	/**
	 * Does not continue until the checkable returns ture
	 * @param check thing to be checked for
	 */
	public void waitUntilDone(boolean check , Checkable run) {
		//just delay the thread until the check is good to go
		while(!check) {
			run.runner();
			try {
				Thread.sleep(100);
			} catch(Exception e) {}
		}
		
	}
}
