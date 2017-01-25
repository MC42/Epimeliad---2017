package autonomousCommands;

import org.usfirst.frc.team2590.robot.Robot;

public class RevShooter extends AutoCommand {

	private double setpoint;
	
	public RevShooter(double setpoint) {
		this.setpoint = setpoint;
	}
	
	
	@Override
	public void run() {
		Robot.shooter.setSetpoint(setpoint);
		Robot.shooter.getReady();
	}


	@Override
	public boolean done() {
		return true;
	}
	
}
