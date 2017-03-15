package org.usfirst.frc.team2590.navigation;

import org.usfirst.frc.team2590.robot.Robot;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SpeedController;

public class VoltageController {
  
  //constants
  private double out;
  private boolean on;
  private double step;
  private boolean stalling;
  private int channelOnPDP;
  private SpeedController motor;
  private PowerDistributionPanel pdp;
  
  //maximums
  private double maxCurrent;
  private double maxVoltage;
  
  public VoltageController(SpeedController motor, int channel , double stepVoltage) {
    out = 0;
    this.on = true;
    maxVoltage = 0;
    maxCurrent = 0;
    stalling = false;
    this.motor = motor;
    this.step = stepVoltage;
    this.channelOnPDP = channel;
    pdp = new PowerDistributionPanel(0);
  }
  
  public void setMax(double current , double voltage) {
    maxCurrent = current;
    maxVoltage = voltage/12;
  }

  public void setOn(boolean on) {
    this.on = on;
  }
  public void calculate() {
    if(on) {
      double current = pdp.getCurrent(channelOnPDP);
      System.out.println("current " + current);
      
      if(current < maxCurrent && out < maxVoltage) {
        out += step;
      } else if(current >= maxCurrent && out > 0) {
        out -= step;
      }
      
      stalling = Robot.gearHold.getAverage() > (maxCurrent-2);
      motor.set(out);
    } else {
      motor.set(0);
    }
  }
  
  public boolean getStalling() {
    return stalling;
  }
}
  
  
