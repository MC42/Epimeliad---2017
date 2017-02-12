package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.DriveStraight;
import org.usfirst.frc.team2590.Commands.RunPath;
import org.usfirst.frc.team2590.navigation.PathSegment;
import org.usfirst.frc.team2590.navigation.Point;
import org.usfirst.frc.team2590.robot.Robot;

public class FivePointBoi extends AutoRoutine {

  DriveStraight drivePastLine;
  RunPath testPath;
  
  public FivePointBoi() {
    testPath = new RunPath(new PathSegment(new Point(0,0,0) , new Point(9,-6,0))); 
        //new PathSegment(new Point(5.1, -2, 0), new Point(6, -7, 0)));
  }

  @Override
  public void run() {
    testPath.startChange();
    testPath.run();
  }

  @Override
  public void end() {
    Robot.dt.setStop();
  }

}
