package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.RunPath;
import org.usfirst.frc.team2590.Commands.TurnToAngle;
import org.usfirst.frc.team2590.navigation.PathSegment;
import org.usfirst.frc.team2590.navigation.Point;
import org.usfirst.frc.team2590.robot.Robot;

/**
 * I was told i dont comment enough, here are the comments
 * @author Connor_Hofenbitzer
 *
 */
public class FourtyBall extends AutoRoutine {

  /*
   * Points
   */
  private Point hopperFace; //the point at which the hopper face lies on an x , y , theta plane
  private Point startPoint; //where does the robot start on the field, should be 0,0,0
  
  /**
   * Segments
   */
  private PathSegment getToHopper; //we need the balls , all of them
  
  /**
   * Paths
   */
  private RunPath getAllTheBalls; //all of them , and I mean all of them
  
  /**
   * Basic movement
   */
  private TurnToAngle turnToFaceBoiler;
  private TurnToAngle visionAdjustToBoiler;
  
  /**
   * it be a 40 ball autonomous
   * @param turnRight : direction the robot should turn to
   */
  public FourtyBall(boolean turnRight) {
    
    /**
     * Points
     */
    startPoint = new Point(0, 0, 0);
    hopperFace = new Point(5, 5 * (turnRight ? -1 : 1) , 0 , Robot.shooter::revShooter);
  
    /**
     * Path segments
     */
    getToHopper = new PathSegment(startPoint, hopperFace);
    
    /**
     * Paths
     */
    getAllTheBalls = new RunPath(getToHopper);
    
    /**
     * Turns
     */
    turnToFaceBoiler = new TurnToAngle(90);
    visionAdjustToBoiler = new TurnToAngle(0); //will be changed to introduce vision
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
    Robot.shooter.setSetpoint(3600);
    
    //run the path
    getAllTheBalls.run();
    waitUntilDone(3, getAllTheBalls::done); //3 second timeout or path is done
    Robot.driveT.shiftLow(); //stops the robot on a dime
    
    //make sure the shooter is started
    Robot.shooter.revShooter();

    turnToFaceBoiler.run(); //face the boiler
    waitUntilDone(1.5, turnToFaceBoiler::done); //3 second timeout or path is done

    Robot.feeder.feedIntoShooter(); //"they call us baller" - RS
  
  }

  @Override
  public void end() {
    
  }

}
