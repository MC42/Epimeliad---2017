package org.usfirst.frc.team2590.looper;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Notifier;

/**
 * The handler for update loops
 * @author Connor_Hofenbitzer
 */
public class Looper {

  private ArrayList<Loop> loopArray;
  private boolean running_ = false;

  private Runnable looper_ = new Runnable() {
    @Override
    public void run() {
      while(true) {
        if(running_) {
          //periodically update the loops
          for(Loop loop : loopArray) {
            loop.loop();
          }
        }
      }
    }
  };


  public Looper(long delayTime) {
    loopArray = new ArrayList<Loop>();
    new Notifier(looper_).startPeriodic(delayTime/1000);
  }

  /**
   * add a new loop to the arraylist
   * @param loop : its a loop
   */
  public void register(Loop loop) {
    loopArray.add(loop);
  }

  /**
   * Starts all the loops
   */
  public void startLoops() {
    if(!running_) {
      System.out.println("starting");
      for(Loop loop : loopArray) {
        loop.onStart();
      }
      running_ = true;
    }
  }

  /**
   * Ends the loops
   */
  public void onEnd() {
    if(running_) {
      running_ = false;
      for(Loop loop : loopArray) {
        loop.onEnd();
      }
    }   
  }
}
