package org.usfirst.frc.team2590.Controllers;

public class TurningController {

  private boolean done;
  private double cylces;
  private double setpoint;
  private double porportional;
  private double minRobotSpeed;
  
  public TurningController(double kP) {
    cylces = 0;
    done = false;
    this.setpoint = 0;
    minRobotSpeed = 0.33;
    this.porportional = kP;
  }
  
  public void changeKp(double kP) {
    this.porportional = kP;
  }
  
  public void setSetpoint(double setpoint) {
    done = false;
    this.setpoint = setpoint;
  }
  
  public double calculate(double processVariable) {
    double error = setpoint - processVariable;
    
    if(Math.abs(error) > 1) {
      cylces = 0;
      if(Math.abs(error*porportional) <= minRobotSpeed) {
        return minRobotSpeed* (error*porportional < 0? -1:1);
      }
      return error*porportional;
    } 
    
    cylces += 1;
    
    //if the error is less than 1 degree we are done
    if(cylces >= 5) {
      done = true;
      return 0.0;
    } else {
      if(Math.abs(error*porportional) <= minRobotSpeed) {
        return minRobotSpeed* (error*porportional < 0? -1:1);
      }
      return error*porportional;
    }
    
  }
  
  public boolean done() {
    return done;
  }
}
