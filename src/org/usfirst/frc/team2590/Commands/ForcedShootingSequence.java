package org.usfirst.frc.team2590.Commands;

import org.usfirst.frc.team2590.robot.Robot;

public class ForcedShootingSequence extends NemesisCommand {

  private double setpoint;

  public ForcedShootingSequence(double setpoint) {
    this.setpoint = setpoint;
  }

  @Override
  public void run() {
    Robot.shooter.setSetpoint(setpoint);
    Robot.shooter.revShooter();
    Robot.feeder.feedIntoShooter();
    Robot.intake.agitate();
  }

  @Override
  public boolean done() {
    return false;
  }

}
