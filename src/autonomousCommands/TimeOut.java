package autonomousCommands;

import edu.wpi.first.wpilibj.Timer;

public class TimeOut {
	
	private double startTime = Timer.getFPGATimestamp()*1000;
	private double timeOut = 3; //in seconds
	
	public TimeOut(double timeOut) {
		this.timeOut = timeOut;
		startTime = Timer.getFPGATimestamp()*1000;
	}
	
	public boolean isTimedOut() {
		return (Math.abs(Timer.getFPGATimestamp()*1000 - startTime) < timeOut);
	}
	
}
