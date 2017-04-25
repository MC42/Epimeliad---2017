package org.usfirst.frc.team2590.Controllers;

import edu.wpi.first.wpilibj.Timer;

public class TurningController {

  private double Ki;
  private double errorSum;
  private boolean done;
  private double cylces;
  private double lastTime;
  private double setpoint;
  private double porportional;
  private double minRobotSpeed;
  
  public TurningController(double kP) {
    
    cylces = 0;
    done = false;
    errorSum = 0;
    lastTime = 0;
    Ki = 0.0;
    this.setpoint = 0;
    minRobotSpeed = 0.33;
    this.porportional = kP;
    
  }
  
  public void changeKp(double kP) {
    this.porportional = kP;
  }
  public void changeKi(double newKI){
    Ki = newKI;
  }
  
  public void setSetpoint(double setpoint) {
    done = false;
    errorSum = 0;
    lastTime = 0;
    this.setpoint = setpoint;
  }
  
  public double calculate(double processVariable) {
    
    double error = setpoint - processVariable;
    double time = Timer.getFPGATimestamp() * 1000;
    
    double dt = time - lastTime;
    errorSum += error * dt; //always integrate error

    if(Math.abs(error) > 1) {
      cylces = 0;
      double output = (Ki * errorSum) + (error*porportional);
      //System.out.println("out " +output );
      lastTime = time;
      return output;
    } 
    
    cylces += 1;
    
    //if the error is less than 1 degree we are done
    if(cylces >= 5) {
      done = true;
      return 0.0;
    } else {
      double output = (Ki * errorSum) + (error*porportional);
      System.out.println("out " +output );
      lastTime = time;
      return output;
    }
    
  }
  
  public boolean done() {
    return done;
  }
}
