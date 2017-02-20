package org.usfirst.frc.team2590.navigation;

import edu.wpi.first.wpilibj.Timer;

public class TurnInPlaceController {


  private final double Kp;
  private final double Ki;
  private final double Kd;
  
  private double capU;
  private double setpoint;
  private double lastTime = 0;
  private double errorSum = 0;
  private double lastError = 0;
  private double lastSetpoint = 0;
  
  boolean flipped;

  public TurnInPlaceController(double Kp, double Ki, double Kd , boolean flipped , double capU) {
        this.Kp = Kp;
        this.Ki = Ki;
        this.Kd = Kd;
        setpoint = 0;
        lastTime = 0;
        errorSum = 0;
        lastError = 0;
        lastSetpoint = 0;
        this.capU = capU;
        this.flipped = flipped;
  }
  
  

  /**
   * Changes the target point to be setpoint
   * 
   * @param setpoint Where the mechanism controlled should go to
   */
  public void set(double setpoint) {
        this.setpoint = setpoint;
  }
  
  /**
   * Returns the target position
   * 
   * @return The setpoint
   */
  public double getSetpoint() {
        return setpoint;
  }

  /**
   * Returns the output for the mechanism (should be called periodically)
   * 
   * @param proccessVar The current location of the mechanism
   * @return The output to the motor controlling the mechanism
   */
  public double getOutput(double proccessVar) {

        double error = setpoint - proccessVar;
        
        //did the setpoint change?
        if (setpoint != lastSetpoint) {
              errorSum = 0;
              lastError = error;
              lastTime = Timer.getFPGATimestamp() * 1000;
        }
        
        double time = Timer.getFPGATimestamp() * 1000;
        double dt = time - lastTime;
        
        errorSum += error * dt; //always integrate error
        
        double dError = (error - lastError) / dt; 
        
        double output = Kp * error + Ki * errorSum + Kd * dError;

        if(output > capU){
              output = capU;
        } 
        if(output < -capU){
              output = -capU;
        }
        //set variables for next run through loop
        lastTime = time;
        lastSetpoint = setpoint;
        lastError = error;
        
        return (flipped) ? output : -output;
  }
  
}
