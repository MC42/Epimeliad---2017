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
    this.angle = angle;
    this.distance = distance;
    started = false;
  }

  @Override
  public void run() {
    Timer.delay(.01);
    Robot.driveT.resetSensors();
    Robot.driveT.driveAtAngle(distance , angle);
    started = true;
  }

  public boolean done() {
    System.out.println("running " );
    return started && Robot.driveT.angleDriveDone();
  }
}
