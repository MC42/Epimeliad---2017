package autonomousCommands;

import org.usfirst.frc.team2590.robot.Robot;

public class DriveForTime extends AutoCommand {
	
	//in seconds
	private double time;
	private TimeOut timer;
	private boolean done = false;
	private boolean firstTime = true;
	
	public DriveForTime(double timeToDrive) {
		time = timeToDrive;
	}

	@Override
	public void run() {
		
		if(firstTime) {
			timer = new TimeOut(time*1000);
			firstTime = false;
		}
		
		if(!timer.isTimedOut()) {
			Robot.dt.deadReckon(0.5, 0.5);
		} else {
			done = true;
			Robot.dt.lock();
		}
	}

	@Override
	public boolean done() {
		return done;
	}
	
	
}
