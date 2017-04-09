package org.usfirst.frc.team2590.Commands;

import org.usfirst.frc.team2590.routine.Checkable;

import edu.wpi.first.wpilibj.Timer;

public abstract class NemesisCommand {
  
  /**
   * run method, main operational stuff should go here
   */
  public abstract void run();
  
  /**
   * checks if the command is finished
   * @return 
   */
  public abstract boolean done();
  

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
