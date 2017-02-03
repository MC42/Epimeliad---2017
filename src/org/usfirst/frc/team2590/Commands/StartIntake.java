package org.usfirst.frc.team2590.Commands;

import org.usfirst.frc.team2590.robot.Robot;

public class StartIntake extends Command {

  @Override
  public void run() {
    Robot.intake.intakeBalls();
  }

}
