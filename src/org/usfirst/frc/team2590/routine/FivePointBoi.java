package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.RunPath;
import org.usfirst.frc.team2590.navigation.PathSegment;
import org.usfirst.frc.team2590.navigation.Point;
import org.usfirst.frc.team2590.robot.Robot;

/**
 * its 5 whole points 
 * @author Connor_Hofenbitzer
 *
 */
public class FivePointBoi extends AutoRoutine {

  private RunPath testPath;
  private Point pastLinePoint;

  public FivePointBoi() {
    pastLinePoint = new Point(10, 0,0);
    testPath = new RunPath(new PathSegment(new Point(0,0,0) , pastLinePoint));
  }

  @Override
  public void run() {

    //go forward far enough to get 5 points
    testPath.run();
  }

  @Override
  public void end() {
    Robot.driveT.setOpenLoop();
  }


}
