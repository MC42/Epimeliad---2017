package org.usfirst.frc.team2590.IRIRoutines;

import org.usfirst.frc.team2590.Commands.RunPath;
import org.usfirst.frc.team2590.Commands.TurnToAngle;
import org.usfirst.frc.team2590.Controllers.PathSegment;
import org.usfirst.frc.team2590.Controllers.Point;
import org.usfirst.frc.team2590.robot.Robot;
import org.usfirst.frc.team2590.routine.AutoRoutine;
import org.usfirst.frc.team2590.routine.FrontGearShoot;

/**
 * IRI Auto Gear : Places a gear on the front peg, shoots the 10 balls,
 * and gets ready for teleop
 * @author Connor_Hofenbitzer
 *
 */
public class IRIAutoFrontGearShoot extends AutoRoutine {

  private FrontGearShoot dropTheGearShoot;

  /**
   * Basic Movement
   */
  private TurnToAngle turnStraight;
  
  /**
   * Path points
   */
  private Point start;
  private Point endPoint;
  private Point constrainingPoint;
  
  /**
   * Path Segments
   */
  private PathSegment constToEnd;
  private PathSegment startToConst;
  
  /**
   * Paths 
   */
  private RunPath getToPlayerStation;
  
  /**
   * Drops a gear, shoots 10, drives to player station
   * @param side : left or right
   */
  public IRIAutoFrontGearShoot(boolean side) {
    turnStraight = new TurnToAngle(45);
    dropTheGearShoot = new FrontGearShoot(side);
  }
  
  @Override
  public void run() {
    dropTheGearShoot.run();
    waitUntilDone(10, dropTheGearShoot::getDone);
    
    Robot.driveT.resetSensors();
    turnStraight.run();
    waitUntilDone(2, turnStraight::done);
    
    getToPlayerStation.flip();
    Robot.driveT.resetSensors();
    getToPlayerStation.run();
  }

  @Override
  public void end() {
  }

}
