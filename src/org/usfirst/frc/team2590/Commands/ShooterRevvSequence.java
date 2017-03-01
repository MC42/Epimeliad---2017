package org.usfirst.frc.team2590.Commands;

import org.usfirst.frc.team2590.robot.Robot;

public class ShooterRevvSequence extends NemesisCommand{

  private double setpoint;
  
  public ShooterRevvSequence(double setpoint) {
    this.setpoint = setpoint;
  }
  
  @Override
  public void run() {
    Robot.shooter.setSetpoint(setpoint);
    Robot.shooter.revShooter();
  }

  @Override
  public boolean done() {
    return false;
  }

}
