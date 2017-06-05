package util;

import org.usfirst.frc.team2590.looper.Loop;
import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.AnalogOutput;
import edu.wpi.first.wpilibj.Timer;

/**
 * Controlls the robots LEDs
 * @author Connor_Hofenbitzer
 *
 */
public class LEDController implements RobotMap{

  private static LEDController ledInstance = null;
  public static LEDController getLED() {
    if(ledInstance == null) {
      ledInstance = new LEDController();
    }
    return ledInstance;
  }
  
  private enum patternState {
    OFF , STROBE , BLINK , SOLID , PULSE
  };
  
  private String shooterState;
  private boolean hasGearState;
  private boolean disabledState;
  private boolean climbingState;
  
  private double pulseVoltage = 0.0;
  private boolean fadeDirection = true;
  
  private static final double OFF_VOLTAGE = 0;
  private static final double SAFE_VOLTAGE = 2;
  private static final double MAX_VOLTAGE = 2.5;
  private static final double STEP_VOLTAGE = 0.1;

  private patternState pattern = patternState.OFF;
  private AnalogOutput leds;
  
  public LEDController() {
    shooterState = "OFF";
    hasGearState = false;
    disabledState = true;
    climbingState = false;
    leds = new AnalogOutput(LED_CHANNEL);
    leds.setVoltage(2);
  }
 
  private Loop ledLoop = new Loop() {

    @Override
    public void onStart() {
      
    }

    @Override
    public void loop(double deltaT) {
      switch(pattern) {
        case OFF :
          leds.setVoltage(OFF_VOLTAGE);
          break;
        case STROBE :
          leds.setVoltage(OFF_VOLTAGE);
          Timer.delay(.15);
          leds.setVoltage(SAFE_VOLTAGE);
          Timer.delay(.15);
          break;
        case BLINK :
          leds.setVoltage(OFF_VOLTAGE);
          Timer.delay(.25);
          leds.setVoltage(SAFE_VOLTAGE);
          Timer.delay(.25);
          break;
        case PULSE :
          pulseVoltage += (STEP_VOLTAGE * (fadeDirection?1:-1));
          
          if(pulseVoltage >= MAX_VOLTAGE-STEP_VOLTAGE || 
             pulseVoltage <= OFF_VOLTAGE+STEP_VOLTAGE ) {
            fadeDirection = !fadeDirection;
          }
          
          leds.setVoltage(pulseVoltage);
          break;
          
        case SOLID :
          leds.setVoltage(SAFE_VOLTAGE);
          break;
      }
      
      handleLights(disabledState , climbingState , shooterState , hasGearState);
    }

    @Override
    public void onEnd() {
    }
    
  };
  
  public Loop getLEDLoop() {
    return ledLoop;
  }
  
  /**
   * Turns the led off
   */
  public void turnOff() {
    pattern = patternState.OFF;
  }
  
  /**
   * Turns the led on
   */
  public void turnSolid() {
    pattern = patternState.SOLID;
  }
  
  /**
   * Strobes the led
   */
  public void turnStrobe() {
    pattern = patternState.STROBE;
  }
  
  /**
   * Blinks the Leds
   */
  public void turnBlink() {
    pattern = patternState.BLINK;
  }
  
  /**
   * Checks if the robot is disabled
   * @param disabled ; is robot disabled
   */
  public void updateDisabledState(boolean disabled) {
    this.disabledState = disabled;
  }
  
  /**
   * Checks if the robot is climbing
   * @param climbing : is the robot climbing
   */
  public void updateClimbingState(boolean climbing) {
    this.climbingState = climbing;
  }
  
  /**
   * Checks if the robot is shooting 
   * @param shooting : are we "REVVING" "SHOOT" or "OFF"
   */
  public void updateShooterState(String shooting) {
    this.shooterState = shooting;
  }
  
  /**
   * Checks if we have a gear
   * @param hasGear : do we have a gear
   */
  public void updateGearState(boolean hasGear) {
    this.hasGearState = hasGear;
  }
  
  /**
   * Ik really ugly , will fix
   * @param disabled : is the robot disabled
   * @param climbing : are we climbing
   * @param shooter : are we "REVVING" "SHOOT" or "OFF"
   * @param hasGear : do we have a gear
   */
  private void handleLights(boolean disabled , boolean climbing , String shooter , boolean hasGear) {
    
    if(!disabled) {
      //climber check
      if(!climbing) {
        
        // if were not climbing but the shooter is not off
        if(!shooter.equalsIgnoreCase("OFF")) {
          pattern = shooter.equalsIgnoreCase("REVVING") ? patternState.SOLID : patternState.BLINK;
        } else {
          pattern = hasGear ? patternState.SOLID : patternState.OFF;
        }
        
        //if we are climbing
      } else {
        pattern = patternState.STROBE;
      }
      
    } else {
      pattern = patternState.SOLID;
    }
    
  }
  
  
  
}
