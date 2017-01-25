package autoroutines;

import org.usfirst.frc.team2590.robot.Robot;

import autonomousCommands.DriveForTime;

/**
 * Drive past the 5 point line to get (you guessed it) 5 points
 * doesnt require encoders or gyro
 * @author Connor_Hofenbitzer
 *
 */
public class FivePointDrive extends Routine {

	private DriveForTime drive;
	
	@Override
	public void init() {
		drive = new DriveForTime(5);
	}

	@Override
	public void run() {
		drive.run();
		waitUntilDone(drive.done(), drive::run);
	}

	@Override
	public void finished() {
		Robot.dt.lock();
	}
	
}
