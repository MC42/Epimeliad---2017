package org.usfirst.frc.team2590.subsystem;

import org.usfirst.frc.team2590.looper.Loop;
import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.Victor;

/**
 * The Feeder Mechanism for Eris, feeds balls from the hopper to the shooter
 * @author Connor_Hofenbitzer
 *
 */
public class Feeder implements RobotMap {

  //singleton
  private static Feeder feederInstance = null;
  public static Feeder getFeeder() {
    if(feederInstance == null) {
      feederInstance = new Feeder();
    }
    return feederInstance;
  }

  //Feeder states
  private enum FeederStates {
    STOP , FEED_TO_SHOOTER , EXPELL , AGITATE
  };
  private FeederStates feed = FeederStates.STOP;

  
  private Victor feederMotor;
  private Victor leftAgitatorMotor;
  private Victor rightAgitatorMotor;

  public Feeder() {
    feederMotor = new Victor(FEEDERMOTORPWM);    
    leftAgitatorMotor = new Victor(LEFTAGITATORMOTORPWM);
    rightAgitatorMotor = new Victor(RIGHTAGITATORMOTORPWM);
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
          leftAgitatorMotor.set(0);
          rightAgitatorMotor.set(0);
          break;
        case FEED_TO_SHOOTER :
          feederMotor.set(0.8); //0.75
          leftAgitatorMotor.set(-1);
          rightAgitatorMotor.set(-1);         
          break;
        case EXPELL :
          feederMotor.set(-1);
          leftAgitatorMotor.set(1);
          rightAgitatorMotor.set(1);          
          break;
        case AGITATE : 
          leftAgitatorMotor.set(1);
          rightAgitatorMotor.set(-1);
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
  
  public void agitateBalls() {
    feed = FeederStates.AGITATE;
  }


}
