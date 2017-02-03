package org.usfirst.frc.team2590.Commands;

import org.usfirst.frc.team2590.robot.Robot;

public class DriveStraight extends Command {

  double time;
  double distance;
  boolean isFinished;

  /**
   *
   * @param distance : DISTANCE MAY NOT BE NEGITIVE
   */
  public DriveStraight(double distance) {
    this.distance = Math.abs(distance);
  }

  @Override
  public void run() {
    Robot.dt.setDriveSetpoint(distance);
  }

  public boolean isDone() {
    return Robot.dt.driveStDone();
  }
}
