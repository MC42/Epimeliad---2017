package org.usfirst.frc.team2590.Controllers;

import edu.wpi.first.wpilibj.Timer;

/**
 * 
 * Controller which handles turning
 * @author Connor_Hofenbitzer
 *
 */
public class TurningController {

  private final double MAX_CYCLES = 5;
  
  private boolean done;
  private double cylces;
  private double lastTime;
  private double setpoint;
  private double integral;
  private double errorSum;
  private double porportional;
  private double minRobotSpeed;
  
  public TurningController(double kP , double kI , double minSpeed) {
    
    cylces = 0;
    done = false;
    errorSum = 0;
    lastTime = 0;
    integral = kI;
    this.setpoint = 0;
    this.porportional = kP;
    minRobotSpeed = minSpeed;
    
  }
  
  /**
   * Allows you to change gains
   * @param kP : new kP
   */
  public void changeKp(double kP) {
    porportional = kP;
  }
  
  /**
   * Allows you to change gains
   * @param newKI : new integra,
   */
  public void changeKi(double kI){
    integral = kI;
  }
  
  /**
   * Sets a new setpoint for the controller
   * @param setpoint : new controller setpoint
   */
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

    double output = (integral * errorSum) + (error * porportional);

    if(Math.abs(output) <= minRobotSpeed) {
      output = Math.signum(output)*minRobotSpeed;
    }
    
    //check the cycles
    if(Math.abs(error) > 1) {
      cylces = 0;
    } else {
      cylces += 1;
    }
    
    done = (cylces >= MAX_CYCLES);
    
    //set the previous variables
    lastTime = time;

    //return the corresponding output
    return done ? 0.0 : output;
    
  }
  
  public boolean done() {
    return done;
  }
}
