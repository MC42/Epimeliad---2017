package util;

import edu.wpi.first.wpilibj.Solenoid;

public class NemesisSolenoid extends Solenoid{

  public NemesisSolenoid(int channel) {
    super(channel);
  }

  @Override
  public void set(boolean open) {
    if(super.get() != open) {
      super.set(open);
    }
  }

  public void toggle() {
    super.set(!super.get());
  }

}
