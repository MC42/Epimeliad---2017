package org.usfirst.frc.team2590.Commands;

import org.usfirst.frc.team2590.robot.Robot;

import edu.wpi.first.wpilibj.Timer;

/**
 * Drives at an angle
 * @author Connor_Hofenbitzer
 *
 */
public class DriveAtAngle extends NemesisCommand {

  private double angle;
  private boolean started;
  private double distance;

  /**
   *
   * @param distance : DISTANCE MAY NOT BE NEGITIVE
   */
  public DriveAtAngle(double distance , double angle) {
    started = false;
    this.angle = angle;
    this.distance = distance;
  }

  /**
   * Changes the distance and angle
   * @param newDistance : new distance to drive to
   * @param newAngle : new angle to drive to
   */
  public void changeConstants(double newDistance, double newAngle) {
    this.angle = newAngle;
    this.distance = newDistance;
  }
  
  @Override
  public void run() {
    //delay to reset sensors
    Robot.driveT.resetDriveController();
    Robot.driveT.resetSensors();
    Timer.delay(.01);
    Robot.driveT.driveAtAngle(distance , angle);
    started = true;
  }

  @Override
  public boolean done() {
    return started && Robot.driveT.angleDriveDone();
  }
}
