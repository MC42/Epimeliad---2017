package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.DriveAtAngle;
import org.usfirst.frc.team2590.Commands.RunPath;
import org.usfirst.frc.team2590.navigation.PathSegment;
import org.usfirst.frc.team2590.navigation.Point;
import org.usfirst.frc.team2590.robot.Robot;

import edu.wpi.first.wpilibj.Timer;

public class RightGear extends AutoRoutine{

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
  
  public RightGear(boolean side) {
  
    //straight dash to the boiler 
    driveToBoiler = new DriveAtAngle((side?3:9.4), (side?12 : -47));

    //points
    onGear = new Point(10 , 4, 0);
    nextToGear = new Point(8, 2 , 0);
    beforeGear = new Point(4.5 , 0 , 0);
    
    //segments
    getOntoGear = new PathSegment(nextToGear, onGear);
    getNextToGear = new PathSegment(beforeGear, nextToGear);
    straight = new PathSegment(new Point(0,0,0), beforeGear);
    
    //path
    getToGear = new RunPath(straight , getNextToGear,getOntoGear);
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
    
    //open the wings just before we get there 
    Timer.delay(2.35);
    Robot.gearHold.openWings();
    Timer.delay(1);
    //start the shooter and drive over to the boiler
    Robot.shooter.setSetpoint(6650);
    Robot.shooter.revShooter();
    Robot.driveT.unInvert();
    driveToBoiler.run();
    
    //shoot when we get to the boiler
    waitUntilDone(2, driveToBoiler::done);
    Robot.shooter.shootWhenReady();
  }

  @Override
  public void end() {
    Robot.driveT.setStop();
  }

  
}
