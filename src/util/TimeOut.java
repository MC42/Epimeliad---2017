package util;

import edu.wpi.first.wpilibj.Timer;

public class TimeOut {

  private double startTime;
  private double timeOutTime;
  
  public TimeOut(double timeOutTime) {
    this.startTime = 0;
    this.timeOutTime = timeOutTime;
  }
  
  public void startTimer() {
    startTime = Timer.getFPGATimestamp()*1000;
  }
  
  public boolean timeIsUp() {
    return (Timer.getFPGATimestamp()*1000 - startTime) > timeOutTime*1000;
  }
}
