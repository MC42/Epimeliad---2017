package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.RunPath;
import org.usfirst.frc.team2590.navigation.PathSegment;
import org.usfirst.frc.team2590.navigation.Point;
import org.usfirst.frc.team2590.robot.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class LeftGear extends AutoRoutine {
  
  //needs to change
  private Alliance side;
 
  //points
  private Point onGear;
  private Point beforeGear;
  private Point nextToGear;
  
  //segments
  private PathSegment straight;
  private PathSegment getNextToGear;
  private PathSegment getOntoGear;
  
  //path
  private RunPath getToGear;
  
  public LeftGear() {
    
    //get the alliance were on
    side = DriverStation.getInstance().getAlliance();

    //points
    //onGear = new Point()
    beforeGear = new Point(4.5 , 0 , 0);
    nextToGear = new Point(7.5, -2 , 0);
    onGear = new Point(9 , -4 , 0);
    //segments
    straight = new PathSegment(new Point(0,0,0), beforeGear);
    getNextToGear = new PathSegment(beforeGear, nextToGear);
    getOntoGear = new PathSegment(nextToGear, onGear);
    //path
    getToGear = new RunPath(straight , getNextToGear,getOntoGear);// , getNextToGear);
  }
  
  @Override
  public void run() {
    Robot.driveT.setSolenoid(false);
    getToGear.startChange();
    getToGear.flip();
    getToGear.run();
    waitUntilDone(3, getToGear.done());
  }

  @Override
  public void end() {
  }

}
