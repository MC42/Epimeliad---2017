package org.usfirst.frc.team2590.Controllers;

import edu.wpi.first.wpilibj.Timer;

public class EnhancedTurnController {

  private double Kp;
  private double Ki;
  private double Kd;

  private double capU;
  private boolean done;
  private double cycles = 0;
  private double setpoint = 0;
  private double lastTime = 0;
  private double errorSum = 0;
  private double lastError = 0;
  private double lastSetpoint = 0;


  public EnhancedTurnController(double Kp, double Ki, double Kd  , double capU) {
    done = false;
    this.Kp = Kp;
    this.Ki = Ki;
    this.Kd = Kd;
    setpoint = 0;
    lastTime = 0;
    errorSum = 0;
    lastError = 0;
    lastSetpoint = 0;
    this.capU = capU;
  }



  /**
   * Changes the target point to be setpoint
   *
   * @param setpoint Where the mechanism controlled should go to
   */
  public void set(double setpoint) {
    done = false;
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

  public double calculate(double processVar) {
    double error = setpoint - processVar;
    double output = getOutput(processVar);

    if(Math.abs(error) > 1) {
      cycles = 0;
      return output;
    }

    cycles += 1;
    if(cycles >= 5) {
      done = true;
    }

    return output;
  }

  public void changeGains(double p , double i , double d) {
    Kp = p;
    Ki = i;
    Kd = d;
  }

  public boolean getDone() {
    return done;
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

    return output;
  }

}
