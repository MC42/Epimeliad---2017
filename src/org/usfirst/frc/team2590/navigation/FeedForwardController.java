package org.usfirst.frc.team2590.navigation;

public class FeedForwardController {

  private double kP;
  private double kF;
  private double kA;
  
  private double posSetpoint;
  private double accSetpoint;
  private double velSetpoint;
 
  public FeedForwardController(double feedForward , double porportional , double acceleration) {
    velSetpoint = 0;
    this.kF = feedForward;
    this.kP = porportional;
    this.kA = acceleration;
    
  }
  
  public void setVelocitySetpoint(double setpoint , double velocity , double acceleration) {
    this.velSetpoint = velocity;
    this.posSetpoint = setpoint;
    this.accSetpoint = acceleration;
  }
  
  public double calculate(double currentPosition) {
    double error = posSetpoint - currentPosition;
    return (error*kP)+(velSetpoint*kF)+(accSetpoint*kA);
  }
}
