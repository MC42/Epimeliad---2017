package autonomousCommands;

import org.usfirst.frc.team2590.robot.Robot;

public class StopShot extends AutoCommand {

	@Override
	public void run() {
		Robot.shooter.stopShot();
	}

	@Override
	public boolean done() {
		return true;
	}

	
}
