package org.usfirst.frc.team2590.navigation;

public class FeedForwardController {

  private double kF;
  private double velSetpoint;
 
  public FeedForwardController(double feedForward) {
    velSetpoint = 0;
    this.kF = feedForward;
    
  }
  
  public void setVelocitySetpoint(double setpoint) {
    this.velSetpoint = setpoint;
  }
  
  public double calculate() {
    return (velSetpoint*kF);
  }
}
