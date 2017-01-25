package autonomousCommands;

import org.usfirst.frc.team2590.robot.Robot;

public class Turn extends AutoCommand{
	double degrees;
	boolean isFirst = true;
	
	public Turn(double degreesToTurn) {
		degrees = degreesToTurn;
	}
	
	public void run() {
		if(isFirst) {
			Robot.dt.zeroGyro();
			isFirst = false;
		}
		
		Robot.dt.turnToPoint(degrees);
	}

	@Override
	public boolean done() {
		return Robot.dt.turnIsDone();
	}
	
}
