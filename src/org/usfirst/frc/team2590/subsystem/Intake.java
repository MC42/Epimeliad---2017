package org.usfirst.frc.team2590.subsystem;

import org.usfirst.frc.team2590.looper.Loop;
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
    intake = intakeStates.INTAKE;
  }

  /**
   * Just put the intake down
   */
  public void dropIntake() {
    intake = intakeStates.JUST_DROP;
  }

  /**
   * Spit out balls, auto intake solenoid
   */
  public void outtakeBalls() {
    intake = intakeStates.EXAUST;
  }

  /**
   * Agitates balls
   */
  public void agitate() {
    intake = intakeStates.AGIGTATE;
  }

}
