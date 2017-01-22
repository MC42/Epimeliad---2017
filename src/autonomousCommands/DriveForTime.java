package autonomousCommands;

import org.usfirst.frc.team2590.robot.Robot;

import edu.wpi.first.wpilibj.Timer;

public class DriveForTime extends AutoCommand {
	
	//in seconds
	private double time;
	private double timeError;
	private double startTime;
	private boolean firstRun = true;
	
	public DriveForTime(double timeToDrive) {
		time = timeToDrive;
	}

	@Override
	public void run() {
		if(firstRun) {
			startTime = Timer.getFPGATimestamp();
		}
		timeError = time - (Timer.getFPGATimestamp() - startTime);
		if(timeError > 1) {
			Robot.dt.deadReckon(0.5, 0.5);
		} else {
			Robot.dt.lock();
		}
	}

	@Override
	public boolean done() {
		return timeError <= 1;
	}
	
	
}
