package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.DriveAtAngle;
import org.usfirst.frc.team2590.Commands.RunPath;
import org.usfirst.frc.team2590.Commands.TurnToTarget;
import org.usfirst.frc.team2590.Controllers.PathSegment;
import org.usfirst.frc.team2590.Controllers.Point;
import org.usfirst.frc.team2590.robot.Robot;

import edu.wpi.first.wpilibj.Timer;

public class FourtyBallRefined extends AutoRoutine{

  /*
   * Points
   */
  private Point curveOut;
  private Point hopperFace; //the point at which the hopper face lies on an x , y , theta plane
  private Point startPoint; //where does the robot start on the field, should be 0,0,0
  private Point middlePoint; //where does the robot start on the field, should be 0,0,0
  
  /**
   * Segments
   */
  private PathSegment getToHopper; //we need the balls , all of them
  private PathSegment getToMiddle; //we need the balls , all of them
  private PathSegment curveOutSeg;
  
  /**
   * Paths
   */
  private RunPath curveOutPath; //face the boiler
  private RunPath getAllTheBalls; //all of them , and I mean all of them
  
  /**
   * Basic movement
   */
  private DriveAtAngle driveToBoiler;
  private TurnToTarget turnTowardsBoilerVision;
  
public FourtyBallRefined(boolean turnRight) {
    
    /**
     * Points
     */
    startPoint = new Point(0, 0, 0);
    hopperFace = new Point(5.75,2.5*(turnRight?1:-1),0); //6 3.5
    middlePoint = new Point(3.75, .5*(turnRight?1:-1)  , 0); //4 .5
    curveOut = new  Point(1 , 1*(turnRight?1:-1));
    
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
    driveToBoiler = new DriveAtAngle(4, 0);
    turnTowardsBoilerVision = new TurnToTarget();

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
    Robot.shooter.setSetpoint(3400);
    
    //run the path
    getAllTheBalls.run();
    waitUntilDone(3, getAllTheBalls::done); //3 second timeout or path is done
    Robot.driveT.setStop();
    Robot.intake.agitate(); 
    Robot.feeder.agitateBalls(); //should clear balls away from camera
    Timer.delay(.5);
    
    Robot.expandBox.openBox();
    curveOutPath.flip();
    Timer.delay(2); //collect the balls
    
    //reset
    Robot.driveT.resetPath();
    Robot.shooter.revShooter();
    Robot.driveT.resetSensors();

    curveOutPath.run(); //face the boiler
    waitUntilDone(2, curveOutPath::done); //2 second timeout or turn is done
    Timer.delay(.2);
    
    //reset all the things
    Robot.driveT.resetPath();
    Robot.driveT.resetSensors();
    
    driveToBoiler.run();
    waitUntilDone(2, driveToBoiler::done); //2 second timeout or turn is done

 
    Robot.driveT.shiftLow(); //stops the robot on a dime
    Robot.driveT.setStop();
   
    Timer.delay(.2);
    Robot.shooter.setSetpoint(3400);
    turnTowardsBoilerVision.run();
    waitUntilDone(2, turnTowardsBoilerVision::done); //2 second timeout or turn is done

    Robot.feeder.feedIntoShooter(); //"they call us baller" - RS
    
  }

  @Override
  public void end() {
    
  }

  
}
