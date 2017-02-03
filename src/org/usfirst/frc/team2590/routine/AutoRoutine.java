package org.usfirst.frc.team2590.routine;

public abstract class AutoRoutine {
  public abstract void run();
  public abstract void end();

  public void waitUntilDone(boolean condition) {
    while(!condition) {
      try {
        Thread.sleep(100);
      } catch (Exception e) {}
    }
  }

}
