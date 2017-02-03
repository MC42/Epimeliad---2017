package org.usfirst.frc.team2590.Commands;

import org.usfirst.frc.team2590.robot.Robot;

public class DriveAtAngle extends Command{

  double angle;
  double distance;
  boolean isFinished;

  /**
   *
   * @param distance : DISTANCE MAY NOT BE NEGITIVE
   */
  public DriveAtAngle(double distance , double angle) {
    this.angle = angle;
    this.distance = Math.abs(distance);
  }

  @Override
  public void run() {
    Robot.dt.driveAtAngle(distance , angle);
  }

  public boolean isDone() {
    return Robot.dt.angleDriveDone();
  }
}
