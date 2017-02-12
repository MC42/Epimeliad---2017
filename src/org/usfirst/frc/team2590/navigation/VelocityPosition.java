package org.usfirst.frc.team2590.navigation;

/**
 * Extremely simple controller
 * @author Connor_Hofenbitzer
 *
 */
public class VelocityPosition {

  private double kP;
  private double kV;
  private double velocitySetpoint;
  private double postitionSetpoint;

  public VelocityPosition(double kP , double kV) {
    this.kP = kP;
    this.kV = kV;
    velocitySetpoint = 0;
    postitionSetpoint = 0;
  }

  public void setSetpoint(double pos , double vel) {
    this.velocitySetpoint = vel;
    this.postitionSetpoint = pos;
  }

  public double calculate(double currentDistance) {
    double error = postitionSetpoint - currentDistance;
    return (error*kP) + (velocitySetpoint*kV);
  }

}
