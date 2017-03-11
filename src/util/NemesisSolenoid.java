package util;

import edu.wpi.first.wpilibj.Solenoid;

public class NemesisSolenoid extends Solenoid{

  public NemesisSolenoid(int channel) {
    super(channel);
  }

  @Override
  public void set(boolean on) {
    if(on != this.get()) {
      super.set(on);
    }
  }



}
