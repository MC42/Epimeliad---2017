package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.RunPath;
import org.usfirst.frc.team2590.navigation.PathSegment;
import org.usfirst.frc.team2590.navigation.Point;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class LeftGear extends AutoRoutine {
  
  //needs to change
  private Alliance side;
 
  //points
  private Point beforeGear;
  private Point nextToGear;
  
  //segments
  private PathSegment straight;
  private PathSegment getNextToGear;
  
  //path
  private RunPath getToGear;
  
  public LeftGear() {
    
    //get the alliance were on
    side = DriverStation.getInstance().getAlliance();

    //points
    beforeGear = new Point(5 , 0 , 0);
    nextToGear = new Point(6, 2 , 0);
    
    //segments
    straight = new PathSegment(new Point(0,0,0), new Point(5,-3,0));
    //getNextToGear = new PathSegment(new Point(5,0) , new Point(6,-2));
    
    //path
    getToGear = new RunPath(straight);// , getNextToGear);
  }
  
  @Override
  public void run() {
    getToGear.startChange();
    getToGear.flip();
    getToGear.run();
    waitUntilDone(3, getToGear.done());
  }

  @Override
  public void end() {
  }

}
