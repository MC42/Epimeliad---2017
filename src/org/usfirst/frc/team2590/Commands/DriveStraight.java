package org.usfirst.frc.team2590.Commands;

import org.usfirst.frc.team2590.robot.Robot;

public class DriveStraight extends Command {

  private double distance;

  public DriveStraight(double distance) {
    this.distance = distance;
  }

  @Override
  public void run() {
    System.out.println("started");
    Robot.dt.resetAllSensors();
    Robot.dt.setDriveSetpoint(distance);
  }

  public boolean isDone() {
    System.out.println("done " + Robot.dt.driveStDone() );
    return Robot.dt.driveStDone();
  }
}
