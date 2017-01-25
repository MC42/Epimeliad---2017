package autonomousCommands;

import org.usfirst.frc.team2590.robot.Robot;

public class TakeShot extends AutoCommand {

	@Override
	public void run() {
		Robot.shooter.takeShot();
	}

	@Override
	public boolean done() {
		return true;
	}
}
