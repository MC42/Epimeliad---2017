package org.usfirst.frc.team2590.IRIRoutines;

import org.usfirst.frc.team2590.routine.FrontGearDrop;

public class IRIAutoFrontGear {

  private FrontGearDrop dropTheGear;
  
  /**
   * Drops a gear, and drives to the human player station
   * @param turnSide : which direction to turn (- / false goes left)
   * @param needsCurve : if the robot has to curve after this to be close to the HPS
   */
  public IRIAutoFrontGear(boolean turnSide , boolean needsCurve) {
    dropTheGear = new FrontGearDrop(); 
  }
}
