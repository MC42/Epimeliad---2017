package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.Command;

public abstract class AutoRoutine {

  public abstract void run();
  public abstract void end();

  public void runCommand(Command command) {
    command.run();

    while(!command.isDone()) {
      try {
        Thread.sleep(100);
      } catch(Exception e) {
        
      }
    }
  }
  
  
}
