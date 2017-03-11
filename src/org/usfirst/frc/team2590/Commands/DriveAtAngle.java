package org.usfirst.frc.team2590.Commands;

import org.usfirst.frc.team2590.robot.Robot;

import edu.wpi.first.wpilibj.Timer;

public class DriveAtAngle extends NemesisCommand {

  double angle;
  double distance;
  boolean started;

  /**
   *
   * @param distance : DISTANCE MAY NOT BE NEGITIVE
   */
  public DriveAtAngle(double distance , double angle) {
    started = false;
    this.angle = angle;
    this.distance = distance;
  }

  @Override
  public void run() {
    //delay to reset sensors
    Timer.delay(.01);
    Robot.driveT.resetDrive();
    Robot.driveT.resetSensors();
    Robot.driveT.driveAtAngle(distance , angle);
    started = true;
  }

  @Override
  public boolean done() {
    return started && Robot.driveT.angleDriveDone();
  }
}
