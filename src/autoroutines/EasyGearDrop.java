package autoroutines;

import org.usfirst.frc.team2590.robot.Robot;

import autonomousCommands.DrivePosVelocity;
import autonomousCommands.Turn;

/**
 * 65 point drop and run
 * @author Connor_Hofenbitzer
 *
 */
public class EasyGearDrop extends Routine {

	private Turn turnAfterDrop;
	private DrivePosVelocity driveToPeg;
	private DrivePosVelocity driveAwayFromGear;
	private DrivePosVelocity driveToFivePointLine;

	
	@Override
	public void init() {
		turnAfterDrop = new Turn(45);
		driveToPeg = new DrivePosVelocity(4,4,0);
		driveAwayFromGear = new DrivePosVelocity(-4,-4,0);
		driveToFivePointLine = new DrivePosVelocity(10, 10,0);
	}
	@Override
	public void run() {
		//drive to drop the gear
		driveToPeg.run();
		waitUntilDone(driveToPeg.done(),driveToPeg::run);
		
		//drive away after dropping gear
		driveAwayFromGear.run();
		waitUntilDone(driveAwayFromGear.done(), driveAwayFromGear::run );
		
		//turn and get ready to drive away
		turnAfterDrop.run();
		waitUntilDone(turnAfterDrop.done(), turnAfterDrop::run);

		//drive past the 5 point line
		driveToFivePointLine.run();
		waitUntilDone(driveToFivePointLine.done(), driveToFivePointLine::run);
	}
	@Override
	public void finished() {
		Robot.dt.lock();
	}
	

}
