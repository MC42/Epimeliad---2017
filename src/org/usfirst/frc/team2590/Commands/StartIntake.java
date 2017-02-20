package org.usfirst.frc.team2590.Commands;

import org.usfirst.frc.team2590.robot.Robot;

public class StartIntake extends NemesisCommand {

  @Override
  public void run() {
    Robot.intake.intakeBalls();
  }

  @Override
  public boolean done() {
    return true;
  }

}
