package org.usfirst.frc.team2590.Controllers;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SpeedController;

public class VoltageController {
  
  //constants
  private double out;
  private boolean on;
  private double step;
  private double cycles; 
  private boolean stalling;
  private int channelOnPDP;
  private SpeedController motor;
  private PowerDistributionPanel pdp;
  
  //maximums
  private double maxCurrent;
  private double maxVoltage;
  
  public VoltageController(SpeedController motor, int channel , double stepVoltage) {
    
    out = 0;
    on = true;
    cycles = 0;
    maxVoltage = 0;
    maxCurrent = 0;
    stalling = false;
    
    //control related stuff
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
  
  public void calculate(double average) {
    if(on) {
      double current = pdp.getCurrent(channelOnPDP);
      
      if(current < maxCurrent && out < maxVoltage) {
        out += step;
      } else if(current >= maxCurrent && out > 0) {
        out -= step;
      }
      
      if(average > (maxCurrent-2)) {
        cycles += 1;
      } else {
        cycles = 0;
      }
      
      stalling = (cycles >= 2);
      motor.set(out);
    }
  }
  
  public boolean getStalling() {
    return stalling;
  }
}
  
  
