package org.usfirst.frc.team2590.Commands;

import org.usfirst.frc.team2590.robot.Robot;

public class ShiftPosition extends Command{

  boolean high;
  public ShiftPosition(boolean high) {
    this.high = high;
  }
  
  @Override
  public void run() {
    System.out.println("shifting");
    Robot.dt.shift(high);
  }
   
  @Override
  public boolean isDone() {
    return true;
  }
}
