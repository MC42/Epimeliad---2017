package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.TurnToCamera;

public class DoNothing extends AutoRoutine {
   
  TurnToCamera testTurn;
  
  public DoNothing() {
    testTurn = new TurnToCamera();
  }
 
  @Override
  public void run() { 
    testTurn.run();
  }

  @Override
  public void end() {
    
  }

 
}
