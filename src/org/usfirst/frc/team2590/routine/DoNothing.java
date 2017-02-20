package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.TurnToCamera;

public class DoNothing extends AutoRoutine {
   
  
  TurnToCamera turn;
  
  public DoNothing() {
    turn = new TurnToCamera();
  }
 
  @Override
  public void run() {
    turn.run();
 
  }

  @Override
  public void end() {
    
  }

}
