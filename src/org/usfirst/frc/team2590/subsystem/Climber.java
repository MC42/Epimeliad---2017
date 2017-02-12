package org.usfirst.frc.team2590.subsystem;

import org.usfirst.frc.team2590.looper.Loop;
import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.Victor;

public class Climber implements RobotMap {

  private enum climbStates {
    CLIMBING , NOT_CLIMBING
  }
  private climbStates climber = climbStates.NOT_CLIMBING;

  private Victor climbVictor;
  private static double CLIMBSPEED = 1;
  private static double STALLSPEED = 0;

  public Climber() {
    climbVictor = new Victor(CLIMBMOTORPWM);
  }

  private Loop loop_ = new Loop() {

    @Override
    public void onStart() {
    }

    @Override
    public void loop() {
      switch(climber) {
        //start climbing
        case CLIMBING :
          climbVictor.set(CLIMBSPEED);
          break;

          //stop climbing
        case NOT_CLIMBING :
          climbVictor.set(STALLSPEED);
          break;
      }
    }

    @Override
    public void onEnd() {
    }

  };
  public Loop getClimbLoop() {
    return loop_;
  }

  public void startClimb() {
    climber = climbStates.CLIMBING;
  }

  public void stopClimb() {
    climber = climbStates.NOT_CLIMBING;
  }
}
