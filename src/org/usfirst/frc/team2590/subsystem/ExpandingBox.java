package org.usfirst.frc.team2590.subsystem;

import org.usfirst.frc.team2590.looper.Loop;
import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;
import util.NemesisSolenoid;

public class ExpandingBox implements RobotMap {
  
  private static ExpandingBox expandBoxInstance = null;
  public static ExpandingBox getExpandingBox() {
    if(expandBoxInstance == null) {
      expandBoxInstance = new ExpandingBox();
    }
    return expandBoxInstance;
  }
  
  private enum expandingBoxStates {
    CLOSED , OPEN , CYCLING
  };
  private expandingBoxStates expandBox = expandingBoxStates.CLOSED;
  
  private double cycleSpeed;
  private NemesisSolenoid expandingBoxPiston;
  
  public ExpandingBox() {
    cycleSpeed = 0.25;
    expandingBoxPiston = new NemesisSolenoid(EXPANDING_BOX_SOLENOID);
  }
  
  private Loop loop = new Loop() {

    @Override
    public void onStart() {
    }

    @Override
    public void loop(double deltaT) {
      switch(expandBox) {
        case CLOSED :
          expandingBoxPiston.set(false);
          break;
        case OPEN :
          expandingBoxPiston.set(true);
          break;
        case CYCLING :
          //closes box
          expandingBoxPiston.set(false);
          //waits for box
          Timer.delay(cycleSpeed);
          //opens box
          expandingBoxPiston.set(true);
          //waits for box
          Timer.delay(cycleSpeed);
          break;
        default :
          break;
      }
    }

    @Override
    public void onEnd() {
    }
    
  };
  
  public Loop getBoxLoop() {
    return loop;
  }
  
  /**
   * UN-EXPAND!
   */
  public void closeBox() {
    expandBox = expandingBoxStates.CLOSED;
  }
  
  /**
   * EXPAND!
   */
  public void openBox() {
    expandBox = expandingBoxStates.OPEN;
  }
  
  /**
   * TOGGLE SWITCH EXPAND!
   */
  public void toggleBox() {
    expandBox = (expandBox == expandingBoxStates.OPEN) ? expandingBoxStates.CLOSED : expandingBoxStates.OPEN; 
  }
  
 
}
