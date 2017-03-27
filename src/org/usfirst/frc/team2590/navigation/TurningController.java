package org.usfirst.frc.team2590.navigation;

public class TurningController {

  private boolean done;
  private double setpoint;
  private double porportional;
  
  public TurningController(double kP) {
    done = false;
    this.setpoint = 0;
    this.porportional = kP;
  }
  
  public void setSetpoint(double setpoint) {
    done = false;
    this.setpoint = setpoint;
  }
  
  public double calculate(double processVariable) {
    double error = setpoint - processVariable;
    if(Math.abs(error) > 1)
      return error*porportional;
    
    //if the error is less than 1 degree we are done
    done = true;
    return 0.0;
  }
  
  public boolean done() {
    return done;
  }
}
