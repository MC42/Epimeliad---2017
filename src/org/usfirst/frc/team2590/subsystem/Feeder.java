package org.usfirst.frc.team2590.subsystem;

import org.usfirst.frc.team2590.looper.Loop;
import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.Victor;

public class Feeder implements RobotMap {

  private static Feeder feederInstance = null;
  public static Feeder getFeeder() {
    if(feederInstance == null) {
      feederInstance = new Feeder();
    }
    return feederInstance;
  }

  private enum FeederStates {
    STOP , FEED_TO_SHOOTER , EXPELL
  };
  private FeederStates feed = FeederStates.STOP;

  private Victor feederMotor;

  public Feeder() {
    feederMotor = new Victor(FEEDERMOTORPWM);
  }

  private Loop loop = new Loop() {

    @Override
    public void onStart() {
    }

    @Override
    public void loop(double deltaT) {
      switch(feed) {
        case STOP :
          feederMotor.set(0);
          break;
        case FEED_TO_SHOOTER :
          feederMotor.set(0.5);
          break;
        case EXPELL :
          feederMotor.set(-1);
          break;
      }
    }

    @Override
    public void onEnd() {
    }

  };

  public Loop getFeederLoop() {
    return loop;
  }

  /**
   * Stops the feeder
   */
  public void stopFeeder() {
    feed = FeederStates.STOP;
  }

  /**
   * Feeds balls into the shooter
   */
  public void feedIntoShooter() {
    feed = FeederStates.FEED_TO_SHOOTER;
  }

  /**
   * Un-Feeds balls into the shooter
   */
  public void expellBalls() {
    feed = FeederStates.EXPELL;
  }


}
