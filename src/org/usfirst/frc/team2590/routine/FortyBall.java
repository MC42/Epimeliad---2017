package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.DriveAtAngle;
import org.usfirst.frc.team2590.Commands.RunPath;
import org.usfirst.frc.team2590.Commands.TurnToTarget;
import org.usfirst.frc.team2590.Controllers.PathSegment;
import org.usfirst.frc.team2590.Controllers.Point;
import org.usfirst.frc.team2590.robot.Robot;

import edu.wpi.first.wpilibj.Timer;

/**
 * Hits a hopper, turns to the boiler, and shoots
 * @author Connor_Hofenbitzer
 *
 */
public class FortyBall extends AutoRoutine {

  /*
   * Points
   */
  private Point hopperFace; //the point at which the hopper face lies on an x , y , theta plane
  private Point middlePoint; //where does the robot start on the field, should be 0,0,0
  private Point startPoint; //where does the robot start on the field, should be 0,0,0
  private Point curveOut;
  
  /**
   * Segments
   */
  private PathSegment getToHopper; //we need the balls , all of them
  private PathSegment getToMiddle; //we need the balls , all of them
  private PathSegment curveOutSeg;
  
  /**
   * Paths
   */
  private RunPath getAllTheBalls; //all of them , and I mean all of them
  private RunPath curveOutPath; //all of them , and I mean all of them
  
  /**
   * Basic movement
   */
  private DriveAtAngle driveToBoiler;
  private TurnToTarget turnTowardsBoilerFirst;
  private TurnToTarget turnTowardsBoilerSecond;

  
  /**
   * it be a 40 ball autonomous
   * @param turnRight : direction the robot should turn to
   */
  public FortyBall(boolean turnRight) {
    
    /**
     * Points
     */
    //for getting to the hopper
    startPoint = new Point(0, 0, 0); 
    middlePoint = new Point(3.75 , .5*(turnRight?1:-1)  , 0); //this x number should be changed porportionatly with the below x value, this makes sure that the path ends at a perfect 90 degrees 
    hopperFace = new Point(5.75 , 2.5*(turnRight?1:-1),0); //if the auto is too short , then increase the x on this point
    curveOut = new  Point(2 , 2*(turnRight?1:-1)); //for getting away from the hopper
    
    /**
     * Path segments
     */
    getToHopper = new PathSegment(middlePoint, hopperFace); //from constraining to hopper face
    getToMiddle =  new PathSegment(startPoint, middlePoint); //constraining segment
    curveOutSeg = new PathSegment(startPoint , curveOut); //drives away from the hopper
    
    /**
     * Paths
     */
    getAllTheBalls = new RunPath(getToMiddle, getToHopper); //get the balls
    curveOutPath = new RunPath(curveOutSeg); //get away from the hopper
    
    /**
     * Turns
     */
    driveToBoiler = new DriveAtAngle(4.5, 0); //drives to a point we can shoot from
    turnTowardsBoilerFirst = new TurnToTarget(); //turn towards the boiler using vision
    turnTowardsBoilerSecond = new TurnToTarget();

  }
  
  @Override
  public void run() {
    
    //reset all the things
    Robot.driveT.resetPath();
    Robot.driveT.resetSensors();
    
    //get ready for auto
    Robot.driveT.shiftHigh();
    Robot.intake.stopIntake();
    Robot.gearHold.averageReset();
    Robot.gearHold.stopGearIntake();
    Robot.shooter.setSetpoint(3100);
    
    //run the path
    getAllTheBalls.run();
    waitUntilDone(3, getAllTheBalls::done); //3 second timeout or path is done
    
    Robot.driveT.setStop();     //stops the robot
    Robot.feeder.agitateBalls(); //should clear balls away from camera
    Robot.intake.agitate(); 
    Timer.delay(.5);
    
    //open the box for MORE BALLS
    Robot.expandBox.openBox();
    curveOutPath.flip();
    
    Timer.delay(2); //collect the balls
   
    curveOutPath.run(); //face the boiler
    Robot.shooter.revShooter();
    waitUntilDone(2, curveOutPath::done); //2 second timeout or turn is done
    Timer.delay(.2);
    
    //run the path which gets to the boiler
    driveToBoiler.run(); 
    waitUntilDone(1.05, driveToBoiler::done); //1.05 second timeout or turn is done
    
    Robot.driveT.shiftLow(); //stops the robot on a dime
    Robot.driveT.setStop();
   
    Timer.delay(.2);
    turnTowardsBoilerFirst.run();
    waitUntilDone(2, turnTowardsBoilerFirst::done); //2 second timeout or turn is done

    Timer.delay(.2);
    Robot.shooter.setSetpoint(3100);
    turnTowardsBoilerSecond.run();
    waitUntilDone(2, turnTowardsBoilerSecond::done); //2 second timeout or turn is done

    Robot.feeder.feedIntoShooter(); //"they call us baller" - RS
    
  }

  @Override
  public void end() {
    
  }

}
