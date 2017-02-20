package org.usfirst.frc.team2590.Commands;

import org.usfirst.frc.team2590.robot.Robot;

public class StopIntake extends NemesisCommand {

  @Override
  public void run() {
    Robot.intake.stopIntake();
  }

  @Override
  public boolean done() {
    return true;
  }

}
