package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.DriveAtAngle;
import org.usfirst.frc.team2590.Commands.RunPath;
import org.usfirst.frc.team2590.navigation.PathSegment;
import org.usfirst.frc.team2590.navigation.Point;
import org.usfirst.frc.team2590.robot.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.WaitUntilCommand;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class LeftGear extends AutoRoutine {
  
  //needs to change
  private Alliance side;
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
  
  public LeftGear() {
    
    //get the alliance were on
    side = DriverStation.getInstance().getAlliance();
    driveToBoiler = new DriveAtAngle(9.4, -27);
    //points
    //onGear = new Point()
    beforeGear = new Point(4.5 , 0 , 0);
    nextToGear = new Point(8, -2 , 0);
    onGear = new Point(10 , -4 , 0);
    
    //segments
    straight = new PathSegment(new Point(0,0,0), beforeGear);
    getNextToGear = new PathSegment(beforeGear, nextToGear);
    getOntoGear = new PathSegment(nextToGear, onGear);
    //path
    getToGear = new RunPath(straight , getNextToGear,getOntoGear);// , getNextToGear);
  }
  
  @Override
  public void run() {
    Robot.gearHold.closeWings();
    Robot.driveT.setSolenoid(false);
    getToGear.startChange();
    getToGear.flip();
    getToGear.run();
    Timer.delay(2.35);
    Robot.gearHold.openWings();
    Timer.delay(1);
    Robot.shooter.setSetpoint(6650, true);
    Robot.shooter.shootNow();
    Robot.driveT.unInvert();
    driveToBoiler.run();
    Timer.delay(3);
    Robot.shooter.onlyPulley();
  }

  @Override
  public void end() {
  }

}
