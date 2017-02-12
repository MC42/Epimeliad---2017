package org.usfirst.frc.team2590.navigation;

public class DriveAtAngleController {

  double kF;
  double kT;
  double error;
  double maxAcc;
  double velocity;
  boolean flipped;
  double angleStp;
  double distanceStp;

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
    //flipped = setPoint < 0;
  }

  public double calculate(double processVar , double gyroA , boolean right , boolean isFlipped) {
    error = distanceStp-processVar;
    
    //velocity calculations
    velocity = Math.sqrt(Math.abs(2*maxAcc*error));
    flipped = error < 0;
    
    //checkk if its inverted
    velocity *= (flipped ? -1 : 1);

    //if it is flip the output
    double out = ((velocity*kF)) + (((angleStp-gyroA)*kT) * (right?1:-1));
    System.out.println("error = " + error);
    
    if(Math.abs(error) >= 0.2) {
      return out * (isFlipped?-1:1);
    }
    
    return 0.0;
  }

  public boolean isDone() {
    System.out.println("error " + (Math.abs(error) < 0.5));
    return Math.abs(error) < 0.5;
  }
}
