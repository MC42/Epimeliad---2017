package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.RunPath;
import org.usfirst.frc.team2590.Commands.TurnToAngle;
import org.usfirst.frc.team2590.navigation.PathSegment;
import org.usfirst.frc.team2590.navigation.Point;
import org.usfirst.frc.team2590.robot.Robot;

import edu.wpi.first.wpilibj.Timer;


/**
 * 40 ball
 * @author Connor_Hofenbitzer
 *
 */
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
    hopperPlate = new Point(6.25, 3 , 0); //5.3 2.5 0
    parallelToHopper = new Point(3.3 , 0 , 0); //2.8
 
    //path segments
    hitTheHopper = new PathSegment(parallelToHopper, hopperPlate);
    driveBeforeTurn = new PathSegment(new Point(0,0,0), parallelToHopper);

    //path to hit down the hopper
    getToHopper = new RunPath(driveBeforeTurn , hitTheHopper);

    finalP = new Point(2.35 , 2.35 , 0 );
    curveOutToFace = new PathSegment(new Point(0,0,0), finalP);
    curveOutP = new RunPath(curveOutToFace);
    
    /**
     * Responsible for getting to the boiler
     */
    curveOut = new Point(7.65 , -1.35 , 0);
    driveStraight = new Point(3.4 , 0 , 0 , Robot.shooter::revShooter);
    
    curveToFaceBoiler = new PathSegment(driveStraight, curveOut);
    getPastHopper = new PathSegment(new Point(0,0,0), driveStraight);

    getToBoiler = new RunPath(getPastHopper, curveToFaceBoiler);//, driveIntoBoiler);
    
   
   
  }

  @Override
  public void run() {    

   
    //drive to the hopper and collect balls
    getToHopper.run();
    waitUntilDone(3, getToHopper::done);
    Timer.delay(2);
    Robot.driveT.resetSensors();
    Robot.driveT.resetPath();
    
    curveOutP.flip();
    curveOutP.run();
    waitUntilDone(1, curveOutP::done);
    
    Robot.driveT.shiftLow();
    Timer.delay(.25);
    Robot.driveT.unFlipPath();
    //turn to face the boiler
    Robot.driveT.setStop();
    Robot.driveT.resetSensors();
    Robot.driveT.resetPath();
    Robot.shooter.setSetpoint(6400);
    
    //go to the boiler
    getToBoiler.run();
    Robot.driveT.shiftHigh();
    waitUntilDone(3, getToBoiler::done); 

  }


  @Override
  public void end() {

  }


}
