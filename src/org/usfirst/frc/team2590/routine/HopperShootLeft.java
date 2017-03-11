package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.RunPath;
import org.usfirst.frc.team2590.Commands.TurnToAngle;
import org.usfirst.frc.team2590.navigation.PathSegment;
import org.usfirst.frc.team2590.navigation.Point;
import org.usfirst.frc.team2590.robot.Robot;

import edu.wpi.first.wpilibj.Timer;

public class HopperShootLeft extends AutoRoutine {

  /**
   * Responsible to get to the hopper
   */
  //path
  private RunPath getToHopper;

  //points
  private Point hopperPlate;
  private Point parallelToHopper;

  //segments
  private PathSegment hitTheHopper;
  private PathSegment driveBeforeTurn;

  private TurnToAngle faceTheBoiler;

  /**
   * back out  
   */
  private RunPath curveOutP;

  private Point finalP;
 
  private PathSegment curveOutToFace;
  
  /**
   * Responsible for getting to the boiler
   */
  private RunPath getToBoiler;

  private Point curveOut;
  private Point boilerFace;
  private Point driveStraight;

  private PathSegment getPastHopper;
  private PathSegment driveIntoBoiler;
  private PathSegment curveToFaceBoiler;


  public HopperShootLeft() {

    /**
     * Series of points to hit the hopper open and collect balls
     */

    //points
    hopperPlate = new Point(5.2, 2.5 , 0); //5 2.5 0
    parallelToHopper = new Point(2.8 , 0 , 0);

    //path segments
    hitTheHopper = new PathSegment(parallelToHopper, hopperPlate);
    driveBeforeTurn = new PathSegment(new Point(0,0,0), parallelToHopper);

    //path to hit down the hopper
    getToHopper = new RunPath(driveBeforeTurn , hitTheHopper);

    //turns the robot to look at the boiler
    faceTheBoiler = new TurnToAngle(-90);

    
    finalP = new Point(2 , 2 , 0 );
    curveOutToFace = new PathSegment(new Point(0,0,0), finalP);
    curveOutP = new RunPath(curveOutToFace);
    
    /**
     * Responsible for getting to the boiler
     */
    curveOut = new Point(4.25 , 1 , 0);
    boilerFace = new Point(5 , 0 , 0 , Robot.feeder::feedIntoShooter);
    driveStraight = new Point(2 , 0 , 0 , Robot.shooter::revShooter);
    
    driveIntoBoiler = new PathSegment(curveOut, boilerFace);
    curveToFaceBoiler = new PathSegment(driveStraight, curveOut);
    getPastHopper = new PathSegment(new Point(0,0,0), driveStraight);

    getToBoiler = new RunPath(getPastHopper , curveToFaceBoiler , driveIntoBoiler);
  }

  @Override
  public void run() {

    //drive to the hopper and collect balls
    getToHopper.run();
    waitUntilDone(3, getToHopper::done);
    Timer.delay(2);
    Robot.driveT.shiftLow();
    Robot.driveT.resetSensors();
    Robot.driveT.reset();
    getToBoiler.flip();
    //curveOutP.run();
    
    //turn to face the boiler
    /*Robot.driveT.setStop();
    Robot.driveT.resetSensors();
    Robot.driveT.reset();
    faceTheBoiler.run();
    waitUntilDone(2, faceTheBoiler::done);
    Timer.delay(.5);
    //reset all the sensors and get ready to shoot
    Robot.driveT.shiftHigh();
    Robot.driveT.setStop();
    Robot.driveT.resetSensors();
    Robot.driveT.reset();
    Robot.shooter.setSetpoint(6400);
    Timer.delay(.1);
    
    //go to the boiler
    getToBoiler.run();
    Timer.delay(2);
    Robot.driveT.setStop();*/

  }


  @Override
  public void end() {

  }


}
