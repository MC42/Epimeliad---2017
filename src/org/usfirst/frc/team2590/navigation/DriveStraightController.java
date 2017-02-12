package org.usfirst.frc.team2590.navigation;

public class DriveStraightController {

  double kF;
  double error;
  double maxAcc;
  double velocity;
  boolean flipped;
  double distanceStp;

  public DriveStraightController(double maxAcc , double kF) {
    error = 0;
    velocity = 0;
    distanceStp = 0;
    flipped = false;
    this.maxAcc = maxAcc;
    this.kF = kF;
  }

  public void setSetpoint(double setPoint ) {
    distanceStp = setPoint;
  }

  public double calculate(double processVar) {
    error = distanceStp-processVar;
    //velocity calculations
    velocity = Math.sqrt(Math.abs(2*maxAcc*error));
    flipped = error < 0;
    velocity *= (flipped?-1:1);
    //if it is flip the output
    System.out.println("out " + velocity*kF);
    return velocity * kF;
  }

  public boolean isDone() {
    return error < 0.5;
  }
}
