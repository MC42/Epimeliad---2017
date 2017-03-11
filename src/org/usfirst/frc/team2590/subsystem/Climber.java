package org.usfirst.frc.team2590.subsystem;

import org.usfirst.frc.team2590.looper.Loop;
import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Victor;

public class Climber implements RobotMap {

  private static Climber climb = null;
  public static Climber getClimber() {
    if(climb == null) {
      climb = new Climber();
    }
    return climb;
  }

  private enum climbStates {
    CLIMBING , NOT_CLIMBING
  }
  private climbStates climber = climbStates.NOT_CLIMBING;

  private Victor climbVictor;

  //0 = stop, 1 = full speed
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
    public void loop(double delta) {
      switch(climber) {
        //start climbing
        case CLIMBING :
          climbVictor.set(CLIMBSPEED);
          break;

          //stop climbing
        case NOT_CLIMBING :
          climbVictor.set(STALLSPEED);
          break;

          //default don't climb
        default :
          DriverStation.reportWarning("Hit default case in climber", false);
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
