package org.usfirst.frc.team2590.Commands;

import org.usfirst.frc.team2590.robot.Robot;

public class StopShootingSequence extends NemesisCommand{

  @Override
  public void run() {
    Robot.shooter.stopShooter();
    Robot.feeder.stopFeeder();
  }

  @Override
  public boolean done() {
    return false;
  }

  
}
