package org.usfirst.frc.team2590.subsystem;

import org.usfirst.frc.team2590.looper.Loop;
import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.Victor;
import util.NemesisSolenoid;

public class Intake implements RobotMap {

  private static Intake intak = null;
  public static Intake getIntakeInstance() {
    if(intak == null) {
      intak = new Intake();
    }
    return intak;
  }
  
  private enum intakeStates {
    STOP , INTAKE , EXAUST , INTAKE_NO_PULL , OUTTAKE_NO_PULL ,  JUST_PULL , JUST_SPIT
  };
  private intakeStates intake = intakeStates.STOP;

  private Victor pullyMotor;
  private Victor intakeMotor;
  private NemesisSolenoid intakeSolenoid;

  public Intake() {
    pullyMotor = new Victor(PULLYMOTORPWM);
    intakeMotor = new Victor(INTAKEMOTORPWM);
    intakeSolenoid = new NemesisSolenoid(INTAKE_SOLENOID);
  }

  private Loop loop_ = new Loop() {

    @Override
    public void onStart() {

    }

    @Override
    public void loop() {
      switch (intake) {
        case STOP :
          pullyMotor.set(0);
          intakeMotor.set(0);
          intakeSolenoid.set(false);
          break;
        case EXAUST :
          pullyMotor.set(-1);
          intakeMotor.set(-1);
          intakeSolenoid.set(true);
          break;
        case INTAKE :
          pullyMotor.set(1);
          intakeMotor.set(1);
          intakeSolenoid.set(true);
          break;
        case INTAKE_NO_PULL :
          pullyMotor.set(0);
          intakeMotor.set(1);
          intakeSolenoid.set(true);
          break;
        case OUTTAKE_NO_PULL :
          pullyMotor.set(0);
          intakeMotor.set(-1);
          intakeSolenoid.set(true);
          break;
        case JUST_PULL :
          pullyMotor.set(1);
          intakeMotor.set(0);
          break;
        case JUST_SPIT :
          pullyMotor.set(-1);
          intakeMotor.set(0);
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
   * Stop the intake
   */
  public void stopIntake() {
    intake = intakeStates.STOP;
  }

  /**
   * Intake balls auto pully
   */
  public void intakeBalls() {
    intake = intakeStates.INTAKE;
  }

  /**
   * Spit out balls auto pully
   */
  public void outtakeBalls() {
    intake = intakeStates.EXAUST;
  }

  /**
   * Intake balls no pully
   */
  public void onlyIntake() {
    intake = intakeStates.INTAKE_NO_PULL;
  }
  
  /**
   * Intake balls no pully
   */
  public void onlyOuttake() {
    intake = intakeStates.OUTTAKE_NO_PULL;
  }


  /**
   * Only run the pully
   */
  public void onlyPullBalls() {
    intake = intakeStates.JUST_PULL;
  }

  /**
   * Only run the pully
   */
  public void onlySpitBalls() {
    intake = intakeStates.JUST_SPIT;
  }

}
