package org.usfirst.frc.team2590.subsystem;

import java.util.LinkedList;
import java.util.Queue;

import org.usfirst.frc.team2590.looper.Loop;
import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Timer;
import util.NemesisSolenoid;

/**
 * The Hopper Subsystem for Eris
 * @author Connor_Hofenbitzer
 *
 */
public class ExpandingBox implements RobotMap {
  
  private static ExpandingBox expandBoxInstance = null;
  public static ExpandingBox getExpandingBox() {
    if(expandBoxInstance == null) {
      expandBoxInstance = new ExpandingBox();
    }
    return expandBoxInstance;
  }
  
  private enum expandingBoxStates {
    CLOSED , OPEN , CYCLING , AUTOMATED_OPEN
  };
  private expandingBoxStates expandBox = expandingBoxStates.CLOSED;
  
  private final double SPIKEAMOUNT = 3; // has to be less cycles than this to be considered a spike
  
  private boolean armed;
  private double cycles;
  private double average;
  private double cycleSpeed;
  private double lastCycles;
  private double averageTotal;
  private Queue<Double> averagingList;
  private BuiltInAccelerometer accelerometer;
  private NemesisSolenoid expandingBoxPiston;
  
  public ExpandingBox() {
    average = 0;
    cycles = 0;
    lastCycles = 0;
    armed = false;
    averageTotal = 0;
    cycleSpeed = 0.25;
    averagingList = new LinkedList<Double>();
    accelerometer = new BuiltInAccelerometer();
    expandingBoxPiston = new NemesisSolenoid(EXPANDING_BOX_SOLENOID);
  }
  
  private Loop loop = new Loop() {

    @Override
    public void onStart() {
    }

    @Override
    public void loop(double deltaT) {
      System.out.println("box x " + accelerometer.getX());
      switch(expandBox) {
        case CLOSED :
          cycles = 0;
          lastCycles = 0;
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
          
        //EXPERIMENTAL
        case AUTOMATED_OPEN :
          calculateAverage();
          if(armed) {
            if(averagingList.size() > 30) {
              //we have enough cycles to get an accurate average
              
              //check if the accelerometer has gone above average
              if(accelerometer.getX() > average + 0.1) {
                cycles += 1;
              } else {
                //if it is around the average (or below) then save the amount of cycles and reset the cycles
                lastCycles = cycles;
                cycles = 0;
              }
              
              //if the spike hasnt been reset and the spike duration was less than a normal dahany acceleration, then open box
              if(lastCycles != 0 && lastCycles <= SPIKEAMOUNT) {
                armed = false;
                expandBox = expandingBoxStates.OPEN;
              }
            }
          }
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
  
  private void calculateAverage() {
    double currentAccel = accelerometer.getX();
    averagingList.add(currentAccel);
    averageTotal += currentAccel;
    
    if(averagingList.size() >= 40) {
      averageTotal -= averagingList.remove();
    }
    
    average = averageTotal / averagingList.size();
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
