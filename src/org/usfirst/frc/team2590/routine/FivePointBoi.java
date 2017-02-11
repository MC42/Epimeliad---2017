package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.DriveAtAngle;
import org.usfirst.frc.team2590.Commands.DriveStraight;
import org.usfirst.frc.team2590.Commands.RunPath;
import org.usfirst.frc.team2590.Commands.ShiftPosition;
import org.usfirst.frc.team2590.navigation.PathSegment;
import org.usfirst.frc.team2590.navigation.Point;
import org.usfirst.frc.team2590.robot.Robot;

/**
 * Gets 5 points by driving past the auto line
 * @author Connor_Hofenbitzer
 *
 */
public class FivePointBoi extends AutoRoutine {

  //drive past the auto line for 5 points
  private ShiftPosition shiftLowGear;
  //private DriveAtAngle drivePastLine;
  private RunPath pathRunner;
  
  public FivePointBoi() {
    pathRunner = new RunPath(new PathSegment(new Point(0,0,0),new Point(10,0,0)));
    //drivePastLine = new DriveAtAngle(10,0);
    shiftLowGear = new ShiftPosition(true);
  }

  @Override
  public void run() {
    //drivePastLine.run();
   pathRunner.startChange();
   pathRunner.run();
  }

  @Override
  public void end() {
    Robot.dt.setStop();
  }

}
