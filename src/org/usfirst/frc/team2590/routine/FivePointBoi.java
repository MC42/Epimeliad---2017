package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.DriveAtAngle;
import org.usfirst.frc.team2590.robot.Robot;

/**
 * its 5 whole points 
 * @author Connor_Hofenbitzer
 *
 */
public class FivePointBoi extends AutoRoutine {
  private DriveAtAngle driveForPoints;

  public FivePointBoi() {
    driveForPoints = new DriveAtAngle(10, 0);
    
  }

  @Override
  public void run() {
    //go forward far enough to get 5 points
    driveForPoints.run();
  }

  @Override
  public void end() {
    Robot.driveT.setOpenLoop();
  }


}
