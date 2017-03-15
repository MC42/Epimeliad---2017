package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.RunPath;
import org.usfirst.frc.team2590.navigation.PathSegment;
import org.usfirst.frc.team2590.navigation.Point;

/**
 * 
 * @author Connor_Hofenbitzer
 *
 */
public class LeftGearPath extends AutoRoutine{

  /**
   * Gets to the peg
   */
  
  //points
  private Point driveStraightP;
  private Point gearPosition;
  
  //path segments
  private PathSegment driveStraightS;
  private PathSegment curveOntoPeg;
  
  //paths
  private RunPath getToPeg;
  
  public LeftGearPath() {
    
    //points
    gearPosition = new Point(7,1,0);
    driveStraightP = new Point(5,0,0);
    
    //path segments
    curveOntoPeg = new PathSegment(driveStraightP , gearPosition);
    driveStraightS = new PathSegment(new Point(0,0,0), driveStraightP);
   
    //path
    getToPeg = new RunPath(driveStraightS , curveOntoPeg);
    
  }
  
  @Override
  public void run() {

  }

  @Override
  public void end() {
 
  }

}
