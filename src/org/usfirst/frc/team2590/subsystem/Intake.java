package org.usfirst.frc.team2590.subsystem;

import org.usfirst.frc.team2590.looper.Loop;
import org.usfirst.frc.team2590.robot.Robot;
import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Victor;
import util.NemesisSolenoid;

public class Intake implements RobotMap {

  private static Intake intakeInstance = null;
  public static Intake getIntake() {
    if(intakeInstance == null) {
      intakeInstance = new Intake();
    }
    return intakeInstance;
  }

  private enum intakeStates {
    STOP , INTAKE , EXAUST , AGIGTATE , JUST_DROP
  };
  private intakeStates intake = intakeStates.STOP;

  //manipulators
  private Victor intakeMotor;
  private NemesisSolenoid intakeSolenoid;

  public Intake() {
    //manipulators
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
          intakeMotor.set(-0.8);
          break;
          
        case JUST_DROP :
          intakeSolenoid.set(true);
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
   * Stop the intake, auto intake solenoid 
   */
  public void stopIntake() {
    intake = intakeStates.STOP;
  }

  /**
   * Intake balls, auto intake solenoid
   */
  public void intakeBalls() {
    if(isLegal(true))
      intake = intakeStates.INTAKE;
  }


  /**
   * Spit out balls, auto intake solenoid
   */
  public void outtakeBalls() {
    if(isLegal(true))
      intake = intakeStates.EXAUST;
  }
  
  public void dropIntake() {
    if(isLegal(true)) {
      intake = intakeStates.JUST_DROP;
    }
  }

  /**
   * Agitates balls
   */
  public void agitate() {
    intake = intakeStates.AGIGTATE;
  }
  
  private boolean isLegal(boolean isDesiredDown) {
    return !isDesiredDown || (!Robot.gearHold.isDown());
  }
  public boolean isDown() {
    return (intake == intakeStates.EXAUST) || (intake == intakeStates.INTAKE);
  }

}
