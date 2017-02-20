package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.DriveAtAngle;
import org.usfirst.frc.team2590.Commands.RunPath;
import org.usfirst.frc.team2590.Commands.TurnToCamera;
import org.usfirst.frc.team2590.navigation.PathSegment;
import org.usfirst.frc.team2590.navigation.Point;
import org.usfirst.frc.team2590.robot.Robot;

import edu.wpi.first.wpilibj.Timer;

public class FrontGearDrop extends AutoRoutine {

  //drive straight
  DriveAtAngle driveAway;
  DriveAtAngle driveToVision;
  DriveAtAngle driveToDropGear;

  TurnToCamera TurnToGear;
  
  //points
  Point start;
  Point front;
  Point start1;
  Point middle;
  Point end;
  

  //path
  RunPath pathToBoil;
  
  public FrontGearDrop() {
    driveAway = new  DriveAtAngle(6, 0);
    driveToDropGear = new DriveAtAngle(-7, 0);
    TurnToGear = new TurnToCamera();
    end = new Point(5.5 , -5);
    start = new Point(0, 0);
    front = new Point(4.6 , -8.2);
    middle = new Point(4 , -4.8);
    pathToBoil = new RunPath(new PathSegment(start , middle) , new PathSegment(middle, front));// , new PathSegment(front, end));
  }

  @Override
  public void run() {
    Robot.driveT.setSolenoid(false);
    //TurnToGear.run();
    //waitUntilDone(2, Robot.driveT.getTurnDone());
    driveToDropGear.run();    
    AutoRoutine.waitUntilDone(3, driveToDropGear.done());
    Timer.delay(.5);
    Robot.gearHold.openWings();
    Timer.delay(.5);
    Robot.driveT.reset();
    pathToBoil.startChange();
    pathToBoil.run();
    Timer.delay(3);
    Robot.shooter.setSetpoint(6600, true);
    Robot.shooter.shootNow();
  }

  @Override
  public void end() {

  }

}
