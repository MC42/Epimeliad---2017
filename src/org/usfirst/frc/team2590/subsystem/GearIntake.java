package org.usfirst.frc.team2590.subsystem;

import java.util.LinkedList;
import java.util.Queue;

import org.usfirst.frc.team2590.looper.Loop;
import org.usfirst.frc.team2590.navigation.VoltageController;
import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Victor;
import util.NemesisSolenoid;

public class GearIntake implements RobotMap {

  private static GearIntake gearInstance = null;
  public static GearIntake getGearHolder() {
    if(gearInstance == null) {
      gearInstance = new GearIntake();
    }
    return gearInstance;
  }

  private enum GearStates {
    INTAKING , OUTTAKING , STOPPED
  };
  private GearStates gear = GearStates.STOPPED;
  
  //constants
  private double average;
  private boolean gripping;
  private double averageTotal;
  private Queue<Double> runningAverage;
  
  PowerDistributionPanel pdp;
  
  //controller
  VoltageController controller;
  
  //manipulators
  Victor intakeVictor;
  NemesisSolenoid gearSolenoid;

  public GearIntake() {

    pdp = new PowerDistributionPanel();
    
    //constants
    average = 0;
    gripping = false;
    averageTotal = 0;
    runningAverage = new LinkedList<Double>();
    
    //manipulators
    intakeVictor = new Victor(GEARINTAKEMOTORPWM);
    gearSolenoid = new NemesisSolenoid(GEAR_HOLDER_SOLENOID);
    
    //controller
    controller = new VoltageController(intakeVictor, 4 , 0.005);
  }

  private Loop loop = new Loop() {

    @Override
    public void onStart() {
      controller.setOn(true);
    }

    @Override
    public void loop(double delta) {
      runCurrentCalc();
      
      switch(gear) {
        case STOPPED :
          //stops everything
          gearSolenoid.set(false);
          
          if(gripping) {
            controller.setMax(4, 6);
            controller.setOn(true);
            controller.calculate();
          } else {
            controller.setOn(false);
            intakeVictor.set(0);
          }
          
          break;
        case INTAKING : 
            gearSolenoid.set(true);
            controller.setOn(true);
            controller.setMax(4, 12);
          break;
        case OUTTAKING :
          
          //stops the motor and drops the geartake
          intakeVictor.set(0);
          gearSolenoid.set(true);
          
          //stops the controller
          controller.setOn(false);
          break;
       
        default :
          
          DriverStation.reportWarning("Hit default case in gear holder", false);
          break;
      }
    }

    @Override
    public void onEnd() {

    }

  };

  public Loop getGearLoop() {
    return loop;
  }

  /**
   * Does the gearTake have a gear
   * @return : does gearTake have a gear
   */
  public boolean hasGear() {
    return controller.getStalling();
  }
  
  /**
   * Stops and lifts the geartake into 
   * the default position
   */
  public void stopGearIntake() {
    gear = GearStates.STOPPED;
  }
  
  /**
   * Turns active gripping on or off
   * @param on : the state gripping should be in
   */
  public void turnOnGrip(boolean on) {
   this.gripping = on;
  }
  
  /**
   * Calculates the average current
   */
  public void runCurrentCalc() {
    double current = pdp.getCurrent(4);
    runningAverage.add(current);
    averageTotal += current;
    
    if(runningAverage.size() > 40) {
      averageTotal -= runningAverage.remove();
    }
    
    average = averageTotal / runningAverage.size();
  }
  /**
   * Gets the average current of the system
   * @return : average current
   */
  public double getAverage() {
    return average;
  }
  
  /**
   * Drops the geartake and starts 
   * the roller
   */
  public void intakeGear() {
    gear = GearStates.INTAKING;   
  }
  
  /**
   * Just Drops the gearTake
   */
  public void outTakeGear() {
    gear = GearStates.OUTTAKING;
  }
}
