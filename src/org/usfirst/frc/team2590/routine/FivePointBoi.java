package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.RunPath;
import org.usfirst.frc.team2590.navigation.PathSegment;
import org.usfirst.frc.team2590.navigation.Point;
import org.usfirst.frc.team2590.robot.Robot;

public class FivePointBoi extends AutoRoutine {

  RunPath testPath;
  Point pastLinePoint;
  
  public FivePointBoi() {
    pastLinePoint = new Point(5, 0,0);
    testPath = new RunPath(new PathSegment(new Point(0,0,0) , pastLinePoint)); 
  }

  @Override
  public void run() {
    testPath.startChange();
    testPath.run();
  }

  @Override
  public void end() {
    Robot.driveT.setStop();
  }

}
