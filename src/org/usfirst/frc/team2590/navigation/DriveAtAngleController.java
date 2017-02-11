package org.usfirst.frc.team2590.navigation;

public class DriveAtAngleController {

  private double kF;
  private double kT;
  private double error;
  private double maxAcc;
  private double velocity;
  private boolean flipped;
  private double angleStp;
  private double distanceStp;

  public DriveAtAngleController(double maxAcc , double kF , double kT) {

    error = 0;
    velocity = 0;
    angleStp = 0;
    this.kF = kF;
    this.kT = kT;
    distanceStp = 0;
    flipped = false;
    this.maxAcc = maxAcc;
  }

  public void setSetpoint(double setPoint , double angle ) {
    angleStp = angle;
    distanceStp = setPoint;


  }

  public double calculate(double processVar , double gyroA , boolean right) {
    error = Math.abs(distanceStp-processVar);
    //System.out.println(" process " + processVar + " error " + error );
    //velocity calculations
    velocity = Math.sqrt(2*maxAcc*error);

    //checkk if its inverted
    flipped = (processVar < 0 || distanceStp-processVar < 0);

    //if it is flip the output
    return ((velocity*(flipped?-1:1)) * kF) + (((angleStp+gyroA)*kT) * (right?-1:1));
  }

  public boolean isDone() {
    return error < 0.5;
  }
}
