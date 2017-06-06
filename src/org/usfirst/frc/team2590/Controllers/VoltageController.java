package org.usfirst.frc.team2590.Controllers;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SpeedController;

/**
 * 
 * Controller which motor current control
 * @author Connor_Hofenbitzer
 *
 */
public class VoltageController {
  
  //constants
  private double out;
  private boolean on;
  private double step;
  private double cycles; 
  private boolean stalling;
  private int channelOnPDP;
  private double maxCurrent;
  private double maxVoltage;
  private SpeedController motor;
  private PowerDistributionPanel pdp;
  
   
  public VoltageController(SpeedController motor, int channel , double stepVoltage) {
    
    //general variables
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
  
  /**
   * Max current and voltage the system can run at 
   * @param current : max current
   * @param voltage : max voltage
   */
  public void setMax(double current , double voltage) {
    maxCurrent = current;
    maxVoltage = voltage/12;
  }

  /**
   * changes the state of the controller
   * @param on : the state of the controller
   */
  public void setOn(boolean on) {
    this.on = on;
  }
 
  /**
   * calculates the voltage that the motor must spin at to reach the current 
   * @param average : average current
   */
  public void calculate(double average) {
    if(on) {
      
      //the current current draw of the motor
      double current = pdp.getCurrent(channelOnPDP);
      
      // if the current draw of the motor is less than the max allowed current
      // and the output voltage is less than the max allowed voltage
      if(current < maxCurrent && out < maxVoltage) {
        //increase the output voltage
        out += step;
      
      //if the current draw is greater than the max allowed current draw
      //and the output is positive
      } else if(current >= maxCurrent && out > 0) {
        //decrease the output voltage
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
  
  
