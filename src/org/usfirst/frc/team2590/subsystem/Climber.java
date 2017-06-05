package org.usfirst.frc.team2590.subsystem;

import org.usfirst.frc.team2590.looper.Loop;
import org.usfirst.frc.team2590.robot.Robot;
import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Victor;

/**
 * The Climbing Subsystem for Eris
 * @author Connor_Hofenbitzer
 *
 */
public class Climber implements RobotMap {

  //singleton
  private static Climber climberInstance = null;
  public static Climber getClimber() {
    if(climberInstance == null) {
      climberInstance = new Climber();
    }
    return climberInstance;
  }

  //it is simple, we are either climbing, or we are not
  private enum climbStates {
    CLIMBING , INVERSE_CLIMB , NOT_CLIMBING
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
          Robot.ledController.updateClimbingState(true);
          break;
          
          //if the rachet wasnt flipped
        case INVERSE_CLIMB :
          climbVictor.set(-CLIMBSPEED);
          Robot.ledController.updateClimbingState(true);
          break;
          
          //stop climbing
        case NOT_CLIMBING :
          climbVictor.set(STALLSPEED);
          Robot.ledController.updateClimbingState(false);
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

  /**
   * Starts the climbing motor using positive motor
   * power
   */
  public void startClimb() {
    climber = climbStates.CLIMBING;
  }
  
  /**
   * Starts the climbing motor using negitive motor power
   */
  public void inverseClimb() {
    climber = climbStates.INVERSE_CLIMB;
  }
 
  /**
   * Stops the climbing motor
   */
  public void stopClimb() {
    climber = climbStates.NOT_CLIMBING;
  }
}
