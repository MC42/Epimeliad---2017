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
  }

  public void setSetpoint(double setPoint ) {
    distanceStp = setPoint;


  }

  public double calculate(double processVar) {
    error = Math.abs(distanceStp-processVar);
    //velocity calculations
    velocity = Math.sqrt(2*maxAcc*error);

    //checkk if its inverted
    flipped = (processVar < 0 || distanceStp-processVar < 0);

    //if it is flip the output
    return (velocity*(flipped?-1:1)) * kF;
  }

  public boolean isDone() {
    return error < 0.5;
  }
}
