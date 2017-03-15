package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.DriveAtAngle;
import org.usfirst.frc.team2590.Commands.RunPath;
import org.usfirst.frc.team2590.navigation.PathSegment;
import org.usfirst.frc.team2590.navigation.Point;
import org.usfirst.frc.team2590.robot.Robot;
import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;

/**
 * 60 points and shoot
 * @author Connor_Hofenbitzer
 *
 */
public class FrontGearDrop extends AutoRoutine implements RobotMap{

  //points
  private Point start;
  private Point front;
  private Point middle;

  //drive straight
  private DriveAtAngle driveToDropGear;
  private DriveAtAngle driveBackIn;
  private DriveAtAngle driveBackOut;
  private DriveAtAngle driveOut;

  private DriveAtAngle driveBackInTwice;
  private DriveAtAngle driveBackOutTwice;
  
  //path
  private RunPath pathToBoil;

  public FrontGearDrop(boolean side) {

    //drive straight
    driveOut = new DriveAtAngle(4, 0);
    driveBackIn = new DriveAtAngle(-4, 3);
    driveBackOut = new DriveAtAngle(4, 0);
    driveToDropGear = new DriveAtAngle(-((80)/12), 0);

    driveBackInTwice = new DriveAtAngle(-4, -6);
    driveBackOutTwice = new DriveAtAngle(4, 0);
    
    //points
    start = new Point(0, 0 );
    front = new Point(4.6 , -8.2 * (side?1:-1));
    middle = new Point(2 , -1 * (side?1:-1) , Robot.gearHold::outTakeGear);

    //path
    pathToBoil = new RunPath(new PathSegment(start , middle) , new PathSegment(middle, front));
  }

  @Override
  public void run() {
    
    //drive to the gear
    driveToDropGear.run();
    waitUntilDone(2, driveToDropGear::done);
    Robot.gearHold.outTakeGear();
    Timer.delay(.5);
    
    Robot.driveT.resetSensors();
    driveOut.run();
    
    Timer.delay(.5);
    Robot.gearHold.stopGearIntake();
    waitUntilDone(1, driveOut::done);
    
    if(Robot.gearHold.hasGear()) {
      Robot.driveT.resetSensors();
      driveBackIn.run();
      waitUntilDone(2, driveOut::done);
      Robot.gearHold.outTakeGear();
      Timer.delay(1);
      driveBackOut.run();
      Robot.gearHold.stopGearIntake();
      Timer.delay(1);
      
      if(Robot.gearHold.hasGear()) {
        Robot.driveT.resetSensors();
        driveBackInTwice.run();
        waitUntilDone(2, driveBackInTwice::done);
        Robot.gearHold.outTakeGear();
        Timer.delay(.5);
        driveBackOutTwice.run();
      }
    }
    
    //get to the boiler
    //Robot.driveT.resetPath();
    //pathToBoil.run();
    //waitUntilDone(3 , pathToBoil::done);

    //shoot
    //Robot.shooter.setSetpoint(6400);
    //Robot.shooter.shootWhenReady();
  }

  @Override
  public void end() {
    Robot.driveT.setStop();
    Robot.feeder.stopFeeder();
  }


}
