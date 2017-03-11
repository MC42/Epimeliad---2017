package util;

import edu.wpi.first.wpilibj.Timer;

/**
 * Boolean which waits a specified amount of time before
 * returning true
 * @author Connor_Hofenbitzer
 *
 */
public class DelayedBoolean {

  private double startTime;
  private double delayTime;
  private boolean isLocked;

  /**
   * Time is in milliseconds
   * @param time
   */
  public DelayedBoolean(double time) {
    isLocked = false;
    this.delayTime = time;
  }

  public void reStart() {
    isLocked = false;
    startTime = Timer.getFPGATimestamp()*1000;
  }

  public boolean getGood() {
    if (Math.abs(Timer.getFPGATimestamp()*1000 - startTime) > delayTime) {
      isLocked = true;
    }
    return isLocked;
  }
}
