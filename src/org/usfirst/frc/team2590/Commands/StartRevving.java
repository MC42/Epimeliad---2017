package org.usfirst.frc.team2590.Commands;

import org.usfirst.frc.team2590.robot.Robot;

public class StartRevving extends NemesisCommand {

  double stp;
  public StartRevving(double setpoint) {
    stp = setpoint;
  }
  
  @Override
  public void run() {
    Robot.shooter.setSetpoint(stp, true);
  }

  @Override
  public boolean done() {
    return true;
  }

}
