package autonomousCommands;

import org.usfirst.frc.team2590.robot.Robot;

public class RevShooter extends AutoCommand {

	private double setpoint;
	private boolean shootWhenReady;
	
	public RevShooter(double setpoint , boolean shootWhenCan) {
		this.setpoint = setpoint;
		shootWhenReady = shootWhenCan;
	}
	
	
	@Override
	public void run() {
		Robot.shooter.setSetpoint(setpoint);
		
		if(shootWhenReady) {
			Robot.shooter.patienceIsKey();
		} else {
			Robot.shooter.getReady();
		}
	}


	@Override
	public boolean done() {
		return false;
	}
	
}
