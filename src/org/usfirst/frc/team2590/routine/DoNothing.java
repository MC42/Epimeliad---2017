package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.RunPath;
import org.usfirst.frc.team2590.navigation.PathSegment;
import org.usfirst.frc.team2590.navigation.Point;

/**
 * Absolutely revolutionary
 * @author Connor_Hofenbitzer
 *
 */
public class DoNothing extends AutoRoutine {

  private Point firstPoint = new Point(5,0,0);
  private PathSegment getToPoint = new PathSegment(new Point(0,0,0), firstPoint);
  private RunPath runPath = new RunPath(getToPoint);
  
  public DoNothing() {
    
  }

  @Override
  public void run() {
    runPath.run();
  }

  @Override
  public void end() {

  }


}
