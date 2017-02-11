package org.usfirst.frc.team2590.Commands;

import org.usfirst.frc.team2590.robot.Robot;

public class DriveAtAngle extends Command{

  private double angle;
  private double distance;

  public DriveAtAngle(double distance , double angle) {
    this.angle = angle;
    this.distance = distance;
  }

  @Override
  public void run() {
    Robot.dt.resetAllSensors();
    Robot.dt.driveAtAngle(distance , angle);
  }

  @Override
  public boolean isDone() {
    return Robot.dt.angleDriveDone();
  }
}
