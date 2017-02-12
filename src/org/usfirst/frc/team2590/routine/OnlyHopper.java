package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.RunPath;
import org.usfirst.frc.team2590.navigation.PathSegment;
import org.usfirst.frc.team2590.navigation.Point;

public class OnlyHopper extends AutoRoutine{

  RunPath driveIntoHopper;
  PathSegment middleSegment;
  PathSegment boilerSegment;
  Point middlePoint;
  Point hopper;
  
  public OnlyHopper() {
    
    middlePoint = new Point(5.5 , 3.5, 0);
    hopper = new Point(10.91, 3.6666, 0);
    middleSegment = new PathSegment(new Point(0,0,0), middlePoint);
    boilerSegment = new PathSegment(middlePoint, hopper);
    driveIntoHopper = new RunPath(middleSegment , boilerSegment);
  }

  @Override
  public void run() {
    driveIntoHopper.startChange();
    driveIntoHopper.run();
  }

  @Override
  public void end() {

  }

}
