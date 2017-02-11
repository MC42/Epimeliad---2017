package util;

import edu.wpi.first.wpilibj.InterruptHandlerFunction;

public abstract class NemesisInteruptFunction<T> extends InterruptHandlerFunction<T> {
  public abstract void interruptFired(int interruptAssertedMask, T param);

}
