package org.usfirst.frc.team2590.Commands;

import org.usfirst.frc.team2590.robot.Robot;

public class IntakeSequence extends NemesisCommand{

  @Override
  public void run() {
    Robot.intake.intakeBalls();
    Robot.feeder.expellBalls();
  }

  @Override
  public boolean done() {
    return false;
  }

  
}
