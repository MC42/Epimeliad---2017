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
public class FourtyBall extends AutoRoutine {

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
  public FourtyBall(boolean turnRight) {
    
    /**
     * Points
     */
    startPoint = new Point(0, 0, 0);
    middlePoint = new Point(3.5, .5*(turnRight?1:-1)  , 0); //4 .5
    hopperFace = new Point(6,2.5*(turnRight?1:-1),0); //6 3.5
    curveOut = new  Point(2 , 2*(turnRight?1:-1));
    
    /**
     * Path segments
     */
    getToHopper = new PathSegment(middlePoint, hopperFace);
    getToMiddle =  new PathSegment(startPoint, middlePoint);
    curveOutSeg = new PathSegment(startPoint , curveOut);
    
    /**
     * Paths
     */
    getAllTheBalls = new RunPath(getToMiddle, getToHopper);
    curveOutPath = new RunPath(curveOutSeg);
    
    /**
     * Turns
     */
    driveToBoiler = new DriveAtAngle(4.5, 0);
    turnTowardsBoilerFirst = new TurnToTarget();
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
    Robot.shooter.setSetpoint(3200);
    
    //run the path
    getAllTheBalls.run();
    waitUntilDone(3, getAllTheBalls::done); //3 second timeout or path is done
    Robot.driveT.setStop();
    Robot.feeder.expellBalls(); //should clear balls away from camera
    //Robot.intake.agitate(); 
    Timer.delay(.5);
    Robot.expandBox.openBox();
    curveOutPath.flip();
    
    Timer.delay(2); //collect the balls
    
    //reset all the things
    Robot.driveT.resetPath();
    Robot.driveT.resetSensors();

    
    curveOutPath.run(); //face the boiler
    Robot.shooter.revShooter();
    waitUntilDone(2, curveOutPath::done); //3 second timeout or turn is done
    Timer.delay(.2);
    Robot.driveT.resetPath();
    Robot.driveT.resetSensors();
    Robot.intake.agitate();
    
    driveToBoiler.run(); //face the boiler
    waitUntilDone(1.05, driveToBoiler::done); //3 second timeout or turn is done
    Robot.driveT.shiftLow(); //stops the robot on a dime
    Robot.driveT.setStop();
   
    Timer.delay(.2);
    turnTowardsBoilerFirst.run();
    waitUntilDone(2, turnTowardsBoilerFirst::done); //2 second timeout or turn is done

    Timer.delay(.2);
    Robot.shooter.setSetpoint(3200);
    turnTowardsBoilerSecond.run();
    waitUntilDone(2, turnTowardsBoilerSecond::done); //2 second timeout or turn is done

    Robot.feeder.feedIntoShooter(); //"they call us baller" - RS
    
  }

  @Override
  public void end() {
    
  }

}
