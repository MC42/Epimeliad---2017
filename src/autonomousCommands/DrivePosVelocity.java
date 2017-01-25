package autonomousCommands;

import org.usfirst.frc.team2590.robot.Robot;

public class DrivePosVelocity extends AutoCommand{

	double heading;
	double leftDist;
	double rightDist;
	boolean done = false;
	
	public DrivePosVelocity(double leftDist , double rightDist , double heading) {
		this.heading = heading;
		this.leftDist = leftDist;
		this.rightDist = rightDist;
		
	}
	
	public void run() {
		Robot.dt.setVelocity(leftDist, rightDist, heading);
	}
	
	@Override
	public boolean done() {
		return Robot.dt.driveIsDone();
	}

}
