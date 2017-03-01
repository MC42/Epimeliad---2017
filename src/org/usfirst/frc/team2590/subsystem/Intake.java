package org.usfirst.frc.team2590.subsystem;

import org.usfirst.frc.team2590.looper.Loop;
import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Victor;
import util.NemesisSolenoid;

public class Intake implements RobotMap {

  private static Intake intak = null;
  public static Intake getIntake() {
    if(intak == null) {
      intak = new Intake();
    }
    return intak;
  }
  
  private enum intakeStates {
    STOP , INTAKE , EXAUST , AGIGTATE
  };
  private intakeStates intake = intakeStates.STOP;

  /**
   * Solenoid key :
   * true = intake down
   * false = intake up
   * 1 = start pulling in balls
   * -1 = start exausting balls
   */
  private Victor intakeMotor;
  private NemesisSolenoid intakeSolenoid;

  public Intake() {
    intakeMotor = new Victor(INTAKEMOTORPWM);
    intakeSolenoid = new NemesisSolenoid(INTAKE_SOLENOID);
  }

  private Loop loop_ = new Loop() {

    @Override
    public void onStart() {

    }

    @Override
    public void loop(double delta) {
      switch (intake) {
        
        //stop the intake 
        case STOP :
          intakeMotor.set(0);
          intakeSolenoid.set(false);
          break;
          
        //spit out balls
        case EXAUST :
          intakeMotor.set(1);
          intakeSolenoid.set(true);
          break;
          
        //intake balls
        case INTAKE :
          intakeMotor.set(-1);
          intakeSolenoid.set(true);
          break;
          
        //run the intake to agitate balls
        case AGIGTATE :
          intakeMotor.set(0.8);
          intakeSolenoid.set(false);
          break;
          
        default :
          DriverStation.reportWarning("Hit default case in intake", false);
          break;
       
      }
    }

    @Override
    public void onEnd() {

    }

  };

  public Loop getIntakeLoop() {
    return loop_;
  }

  /**
   * Stop the intake auto intake sol
   */
  public void stopIntake() {
    intake = intakeStates.STOP;
  }

  /**
   * Intake balls auto intake sol
   */
  public void intakeBalls() {
    intake = intakeStates.INTAKE;
  }

  /**
   * Spit out balls auto intake sol
   */
  public void outtakeBalls() {
    intake = intakeStates.EXAUST;
  }

  public void agitate() {
    intake = intakeStates.AGIGTATE;
  }
 
}
