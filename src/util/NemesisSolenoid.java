package util;

import edu.wpi.first.wpilibj.Solenoid;

/**
 * Enhanced solenoid class
 * @author Connor_Hofenbitzer
 *
 */
public class NemesisSolenoid extends Solenoid{

  public NemesisSolenoid(int channel) {
    super(channel);
  }

  @Override
  public void set(boolean on) {
    //just makes sure that the piston doesnt try to refire 
    if(on != this.get()) {
      super.set(on);
    }
  }



}
