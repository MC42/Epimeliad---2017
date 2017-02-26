package org.usfirst.frc.team2590.looper;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;

/**
 * The handler for update loops
 * @author Connor_Hofenbitzer
 */
public class Looper {

  private double delayT = 0;
  private double lastTime = 0;
  private double currentTime = 0;
  private boolean running_ = false;
  private Object loopLock = new Object();
  private ArrayList<Loop> loopArray;
  private Notifier notfifier;
  

  private Runnable looper_ = new Runnable() {
    @Override
    public void run() {
      if(running_) {
        synchronized (loopLock) {
            //periodically update the loops
            currentTime = Timer.getFPGATimestamp();
            for(Loop loop : loopArray) {
              loop.loop(currentTime - lastTime);
            }
            lastTime = currentTime;
          }
        }
    }
  };
 
  public Looper(double delayTime) {
    loopArray = new ArrayList<Loop>();
    delayT = delayTime;
    notfifier = new Notifier(looper_);
    

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
      notfifier.startPeriodic(delayT);
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
  

