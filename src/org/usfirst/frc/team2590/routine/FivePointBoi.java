package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.DriveStraight;
import org.usfirst.frc.team2590.robot.Robot;

public class FivePointBoi extends AutoRoutine {

  DriveStraight drivePastLine;

  public FivePointBoi() {
    drivePastLine = new DriveStraight(10);
  }

  @Override
  public void run() {
    drivePastLine.run();
  }

  @Override
  public void end() {
    Robot.dt.setStop();
  }

}
