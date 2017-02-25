package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.DriveAtAngle;
import org.usfirst.frc.team2590.Commands.RunPath;
import org.usfirst.frc.team2590.navigation.PathSegment;
import org.usfirst.frc.team2590.navigation.Point;
import org.usfirst.frc.team2590.robot.Robot;
import org.usfirst.frc.team2590.subsystem.GearHolder;

import edu.wpi.first.wpilibj.Timer;

/**
 * From the side where the boiler is on your left (BLUE)
 * @author Connor_Hofenbitzer
 *
 */
public class LeftGear extends AutoRoutine {
  
  //needs to change
  private DriveAtAngle driveToBoiler;
  
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
  
  public LeftGear(boolean side) {
  
    //straight dash to the boiler or hopper , depending on side
    driveToBoiler = new DriveAtAngle((side?1.5:3), (side?0 : 12));//-25

    //points 
    onGear = new Point(7, -2, 0, Robot.driveT::shiftLow); //9.2 -3
    //nextToGear = new Point(7, -2 , 0);
    beforeGear = new Point(4, 0 , 0 ); //5.4
    
    //segments
    getOntoGear = new PathSegment(beforeGear, onGear);
    //getNextToGear = new PathSegment(beforeGear, nextToGear);
    straight = new PathSegment(new Point(0,0,0), beforeGear);
    
    //path
    getToGear = new RunPath(straight , getOntoGear);//getNextToGear,getOntoGear);
  }
  
  @Override
  public void run() {
    
    //get ready to go
    Robot.gearHold.closeWings();
    Robot.driveT.resetSensors();
    Robot.driveT.shiftHigh();
    
    //drive to the gear
    getToGear.startChange();
    getToGear.flip();
    getToGear.run();
    waitUntilDone(4.5, getToGear::done);
    //open the wings just before we get there 
    Timer.delay(.5);
    
    //start the shooter and drive over to the boiler
    //Robot.driveT.unInvert();
    //driveToBoiler.run();
    
    //shoot when we get to the boiler
    //Robot.shooter.shootWhenReady();
  }

  @Override
  public void end() {
    Robot.driveT.setStop();
  }

  

}
