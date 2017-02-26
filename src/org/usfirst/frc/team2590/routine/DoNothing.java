package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.robot.Robot;

public class DoNothing extends AutoRoutine {
   
  
  public DoNothing() {
    
  }
 
  @Override
  public void run() { 
    Robot.driveT.commandModelReset();
    Robot.driveT.setVelSetpoint(10, 6);
    Robot.driveT.setVelControl();
  }

  @Override
  public void end() {
    
  }

 
}
