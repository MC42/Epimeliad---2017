package org.usfirst.frc.team2590.routine;

import edu.wpi.first.wpilibj.Timer;

/**
 * Base auto stuff
 * @author Connor_Hofenbitzer
 *
 */
public abstract class AutoRoutine {

  public abstract void run();
  public abstract void end();

  /**
   * halts the codes progression until a condition is met or the timeout is reached
   * @param timeOut : after this amount of time the code will finish anyways
   * @param condition : if this condition is met, allow the code to proceed
   */
  public static void waitUntilDone(double timeOut , Checkable condition) {
    double start = Timer.getFPGATimestamp();
    while(Timer.getFPGATimestamp() - start < timeOut) {
      if(condition.checker()) {
        return;
      }
      try {
        Thread.sleep(10);
      } catch (Exception e) {}
    }
  }

}
