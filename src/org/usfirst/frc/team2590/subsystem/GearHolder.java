package org.usfirst.frc.team2590.subsystem;

import org.usfirst.frc.team2590.looper.Loop;
import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import util.NemesisSolenoid;

public class GearHolder implements RobotMap {

  private static GearHolder gearH = null;
  public static GearHolder getGearHolder() {
    if(gearH == null) {
      gearH = new GearHolder();
    }
    return gearH;
  }
  
  private enum GearStates {
    OPEN , CLOSED
  };
  private GearStates gear = GearStates.CLOSED;
  
  NemesisSolenoid gearSolenoid;
  
  public GearHolder() {
    gearSolenoid = new NemesisSolenoid(GEAR_HOLDER_SOLENOID);
  }
  
  private Loop loop = new Loop() {

    @Override
    public void onStart() {
      
    }

    @Override
    public void loop() {
      switch(gear) {
        case OPEN :
          gearSolenoid.set(true);
          break;
        case CLOSED :
          gearSolenoid.set(false);
          break;
        default :
          DriverStation.reportWarning("Hit default case in gear holder", false);
          break;
      }
      SmartDashboard.putBoolean("Gear Holder Open", gearSolenoid.get());
    }

    @Override
    public void onEnd() {
      
    }
    
  };
  
  public Loop getGearLoop() {
    return loop;
  }
  
  public void toggleWings() {
    gear = (gear == GearStates.OPEN) ? GearStates.CLOSED : GearStates.OPEN;
  }
  public void openWings() {
    gear = GearStates.OPEN;
  }
  
  public void closeWings() {
    gear = GearStates.CLOSED;
  }
}
