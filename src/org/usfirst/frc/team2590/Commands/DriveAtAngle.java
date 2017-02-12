package org.usfirst.frc.team2590.Commands;

import org.usfirst.frc.team2590.robot.Robot;

public class DriveAtAngle extends Command{

  double angle;
  double distance;
  boolean started;

  /**
   *
   * @param distance : DISTANCE MAY NOT BE NEGITIVE
   */
  public DriveAtAngle(double distance , double angle) {
    this.angle = angle;
    this.distance = distance;
    started = false;
  }

  @Override
  public void run() {
    Robot.dt.driveAtAngle(distance , angle);
    started = true;
  }

  public boolean isDone() {
    return started && Robot.dt.angleDriveDone();
  }
}
