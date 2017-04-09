package org.usfirst.frc.team2590.Controllers;

/**
 * This needs to be rewritten
 * @author Connor_Hofenbitzer
 *
 */
public class DriveAtAngleController {

  private double A;
  private double B;
  private double kF;
  private double kT;
  private double kI;

  private boolean done;
  private double error;
  private double lastOut;
  private double lastError;

  private double velocity;
  private boolean flipped;
  private double angleStp;
  private double distanceStp;

  public DriveAtAngleController(double kF , double kT , double kI) {

    error = 0;
    lastOut = 0;
    done = false;
    lastError = 0;
    velocity = 0;
    angleStp = 0;
    this.kF = kF;
    this.kT = kT;
    this.kI = kI;

    distanceStp = 0;
    flipped = false;
  }

  public void setSetpoint(double setPoint , double angle ) {
    done = false;
    error = setPoint;
    angleStp = angle;
    distanceStp = setPoint;
  }

  public void changeF(double newF) {
    this.kF = newF;
  }

  public double getSetpoint() {
    return distanceStp;
  }

  public void reset() {
    done = false;
    lastOut = 0;
    lastError = 0;
  }

  public double calculate(double processVar , double gyroA , boolean right , double dt) {

    //calculate error
    error = distanceStp-processVar;

    //velocity calculations
    velocity = Math.abs(error);
    flipped = error < 0;

    //checkk if its inverted
    velocity *= (flipped ? -1 : 1);

    A = kT + ((kI*dt)/2);
    B = ((kI*dt)/2) - kT;

    double turnOutPut = lastOut + (A*(angleStp-gyroA)) + (B*lastError);

    //if it is flip the output
    double out = ((velocity*kF)) + (turnOutPut * (right?1:-1));
    lastError = (angleStp-gyroA);


   //tolerance of 1.2 inches
    if(Math.abs(error) > 0.1) {
      lastOut = turnOutPut;
      return out;
    }
    
    //the drive is done
    done = true;
    return 0.0;
  }

  public boolean isDone() {
    return done;
  }
}
