package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.RunPath;
import org.usfirst.frc.team2590.navigation.PathSegment;
import org.usfirst.frc.team2590.navigation.Point;

public class OnlyHopper extends AutoRoutine{

  Point middlePoint;
  Point frontHopper;
  Point middleHopper;
  PathSegment endSegment;
  RunPath driveIntoHopper;
  PathSegment boilerSegment;
  PathSegment middleSegment;
   
  public OnlyHopper() {

    //drive to a point in between the start point and the hopper
    middlePoint = new Point(5 , 1, 0);
    //drive to the front of the hopper
    frontHopper = new Point( 8 ,1.5, 0); //3.75
    //drive past the hopper
    middleHopper = new Point(10, 1.5, 0);
    
    //binding the points
    middleSegment = new PathSegment(new Point(0,0,0), middlePoint);
    boilerSegment = new PathSegment(middlePoint, frontHopper);
    endSegment = new PathSegment(frontHopper, middleHopper);
    
    driveIntoHopper = new RunPath(middleSegment, boilerSegment, endSegment);
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
